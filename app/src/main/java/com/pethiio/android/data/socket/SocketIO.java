package com.pethiio.android.data.socket;

import com.google.gson.Gson;
import com.pethiio.android.data.model.socket.ChatSendMessage;
import com.pethiio.android.utils.PreferenceHelper;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class SocketIO {
    public void main() throws Exception {
        IO.Options options = new IO.Options();
        String token = "Bearer " + PreferenceHelper.SharedPreferencesManager.Companion.getInstance().getAccessToken();
        Socket socket = IO.socket("http://api.pawtind.com:9092/chat?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8.toString()), options);
        Integer roomId = 4;
        socket.on("/topic/private.chat." + roomId, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                JSONObject object = (JSONObject) objects[0];
                System.out.println("Message :" + object.toString());

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
        int count = 0;
        while (true) {
            if (count > 1_000_000) {
                break;
            }
            if (count % 1 == 0) {
                Gson gson= new Gson();
                String obj = gson.toJson(new ChatSendMessage("content",4,382),ChatSendMessage.class);

                socket.emit("/app/private.chat", new JSONObject(obj));
            }
            Thread.sleep(60000);
            count++;
        }
    }

}
