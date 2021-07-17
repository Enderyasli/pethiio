package com.pethiio.android.data.socket;

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
//        final TrustManager[] trustAllCerts= new TrustManager[] { new X509TrustManager() {
//            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                return new java.security.cert.X509Certificate[] {};
//            }
//
//            public void checkClientTrusted(X509Certificate[] chain,
//                                           String authType) throws CertificateException {
//            }
//
//            public void checkServerTrusted(X509Certificate[] chain,
//                                           String authType) throws CertificateException {
//            }
//        } };


        String token = "Bearer " + PreferenceHelper.SharedPreferencesManager.Companion.getInstance().getAccessToken();
        try {
//            SSLContext mySSLContext = SSLContext.getInstance("TLS");
//            mySSLContext.init(null, trustAllCerts, null);

            IO.Options options = new IO.Options();
            SocketSSL.set(options);

            socket = IO.socket(Constants.SOCKET_URL + URLEncoder.encode(token, StandardCharsets.UTF_8.displayName()), options);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void setRooms(int roomId) {
        socket.on(Constants.SOCKET_TOPIC + roomId, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                parseMessage(objects[0], roomId);

            }
        });// TODO: 8.07.2021 forla roomlist

        socket.on(Socket.EVENT_CONNECT, objects -> {
            System.out.println(socket);
            System.out.println("Message :" + Socket.EVENT_CONNECT);
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, objects ->
                System.out.println("Message :" + Socket.EVENT_CONNECT_ERROR + " " + objects)
        );
        socket.on(Socket.EVENT_DISCONNECT, objects -> {
            System.out.println(socket);
            System.out.println("Message :" + Socket.EVENT_DISCONNECT + " " + objects);
        });
        socket.connect();


    }

    private void parseMessage(Object object1, int roomId) {
        JSONObject object = (JSONObject) object1;
        JsonParser parser = new JsonParser();
        JsonElement mJson = parser.parse(object.toString());
        Gson gson = new Gson();
        ChatRoomResponse response = gson.fromJson(mJson, ChatRoomResponse.class);
        System.out.println("Message :" + object.toString());
        EventBus.getDefault().post(new ChatEvent(roomId, response.getId(), response.getContent(), response.getSenderMemberId(), response.getTime()));

//        NotificationUtils notificationUtils = new NotificationUtils(PethiioApplication.context);
//        notificationUtils.showNotificationMessage("notificationTitle", "notificationMessage", null, null);
//                NotificationHelper.generateNotification("senderName", "message");


    }

    public void sendMessage(ChatSendMessage message, int roomId) throws JSONException {
        Gson gson = new Gson();
        String obj = gson.toJson(message, ChatSendMessage.class);
        socket.emit(Constants.SOCKET_PRIVATE_CHAT, new JSONObject(obj));
    }


}
