package com.pethiio.android.data.socket;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pethiio.android.PethiioApplication;
import com.pethiio.android.data.EventBus.ChatEvent;
import com.pethiio.android.data.model.chat.ChatRoomResponse;
import com.pethiio.android.data.model.socket.ChatSendMessage;
import com.pethiio.android.ui.main.util.NotificationUtils;
import com.pethiio.android.utils.Constants;
import com.pethiio.android.utils.PreferenceHelper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SocketIO {
    Socket socket;

    public void connectSocket() {
//        if (socket!=null &&!socket.connected()) {


        String token = "Bearer " + PreferenceHelper.SharedPreferencesManager.Companion.getInstance().getAccessToken();
        try {
            IO.Options options = new IO.Options();
            SocketSSL.set(options);

            socket = IO.socket(Constants.SOCKET_URL + URLEncoder.encode(token, StandardCharsets.UTF_8.displayName()), options);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        }
    }

    public void setRooms(int roomId) {

        Log.e("Socketdenemesi:", "roomID ");

//        disconnect(roomId);
//        if (!socket.hasListeners(Constants.SOCKET_TOPIC + roomId)) {

        socket.on(Constants.SOCKET_TOPIC + roomId, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                Log.e("Socketdenemesi:", "mesaja girdi");

                parseMessage(objects[0], roomId);

            }
        });
//        }
        Log.e("Sockethaslisteners:", "" + socket.hasListeners(Constants.SOCKET_TOPIC + roomId));


        socket.on(Socket.EVENT_CONNECT, objects -> {
            System.out.println(socket);
            Log.e("Socketdenemesi:", "connect girdi");

            System.out.println("Message :" + Socket.EVENT_CONNECT);
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, objects -> {
            Log.e("Socketdenemesi:", "connectError girdi");
            System.out.println("Message :" + Socket.EVENT_CONNECT_ERROR + " " + objects);

        });
        socket.on(Socket.EVENT_DISCONNECT, objects -> {
            Log.e("Socketdenemesi:", "connectDisconnect girdi");

            System.out.println(socket);
            System.out.println("Message :" + Socket.EVENT_DISCONNECT + " " + objects);
        });
        socket.connect();


    }

    public void disconnect(int roomId) {
        if (socket != null) {
            socket.off();
            socket.disconnect();
            socket.off(Constants.SOCKET_TOPIC + roomId);
        }
    }

    private void parseMessage(Object object1, int roomId) {
        JSONObject object = (JSONObject) object1;
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(object.toString());
        Gson gson = new Gson();
        ChatRoomResponse response = gson.fromJson(mJson, ChatRoomResponse.class);
        System.out.println("Message :" + object.toString());
        EventBus.getDefault().post(new ChatEvent(roomId, response.getId(), response.getContent(), response.getSenderMemberId(), response.getTime()));


    }

    public void sendMessage(ChatSendMessage message) throws JSONException {
        Gson gson = new Gson();
        String obj = gson.toJson(message, ChatSendMessage.class);
        socket.emit(Constants.SOCKET_PRIVATE_CHAT, new JSONObject(obj));
    }


}
