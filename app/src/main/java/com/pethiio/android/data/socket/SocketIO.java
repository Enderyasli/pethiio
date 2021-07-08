package com.pethiio.android.data.socket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pethiio.android.data.EventBus.ChatEvent;
import com.pethiio.android.data.model.chat.ChatRoomResponse;
import com.pethiio.android.data.model.socket.ChatSendMessage;
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

public class SocketIO {
    Socket socket;
    public void connectSocket(int roomId) throws JSONException, InterruptedException {
        IO.Options options = new IO.Options();
        String token = "Bearer " + PreferenceHelper.SharedPreferencesManager.Companion.getInstance().getAccessToken();
        try {
            socket = IO.socket(Constants.SOCKET_URL + URLEncoder.encode(token, StandardCharsets.UTF_8.toString()), options);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        socket.on(Constants.SOCKET_TOPIC + roomId, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject object = (JSONObject) objects[0];
                JsonParser parser = new JsonParser();
                JsonElement mJson =  parser.parse(object.toString());
                Gson gson = new Gson();
                ChatRoomResponse response = gson.fromJson(mJson, ChatRoomResponse.class);
                System.out.println("Message :" + object.toString());
                EventBus.getDefault().post(new ChatEvent(roomId,response.getId(),response.getContent(),response.getSenderMemberId(),response.getTime()));

                // TODO: 8.07.2021 Eventbusla mesaj güncelle

                // TODO: 8.07.2021 match bildiri ile tekrar subscribinggg

            }
        });// TODO: 8.07.2021 forla roomlist
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println(socket);
                System.out.println("Message :" + Socket.EVENT_CONNECT);
            }
        });
        socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println("Message :" + Socket.EVENT_CONNECT_ERROR + " " + objects);
            }
        });
        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                System.out.println(socket);
                System.out.println("Message :" + Socket.EVENT_DISCONNECT + " " + objects);
            }
        });
        socket.connect();


//        int count = 0;
//        while (true) {
//            if (count > 1_000_000) {
//                break;
//            }
//            if (count % 1 == 0) {
//                Gson gson = new Gson();
//                String obj = gson.toJson(new ChatSendMessage("content", 4, 382), ChatSendMessage.class);
//
//                socket.emit(Constants.SOCKET_PRIVATE_CHAT, new JSONObject(obj));
//            }
//            Thread.sleep(60000);
//            count++;
//        }

    }

    public void sendMessage(ChatSendMessage message) throws JSONException {
        Gson gson = new Gson();
        String obj = gson.toJson(message,ChatSendMessage.class);
            socket.emit(Constants.SOCKET_PRIVATE_CHAT, new JSONObject(obj));


    }


}
