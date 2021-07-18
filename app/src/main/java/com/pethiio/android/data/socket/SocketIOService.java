package com.pethiio.android.data.socket;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;


import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pethiio.android.PethiioApplication;
import com.pethiio.android.data.EventBus.ChatEvent;
import com.pethiio.android.data.model.chat.ChatListResponse;
import com.pethiio.android.data.model.chat.ChatRoomResponse;
import com.pethiio.android.data.model.socket.ChatSendMessage;
import com.pethiio.android.ui.main.util.NotificationUtils;
import com.pethiio.android.utils.Constants;
import com.pethiio.android.utils.PreferenceHelper;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.socket.client.IO;
import io.socket.client.Socket;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;


/**
 * Created by planet on 6/8/2017.
 */

public class SocketIOService extends Service implements SocketEventListener.Listener, HeartBeat.HeartBeatListener {
    public static final String KEY_BROADCAST_MESSAGE = "b_message";
    public static final int EVENT_TYPE_JOIN = 1, EVENT_TYPE_MESSAGE = 2, EVENT_TYPE_TYPING = 3;
    private static final String EVENT_MESSAGE = "message";
    private static final String EVENT_JOIN = "join";
    private static final String EVENT_RECEIVED = "received";
    private static final String EVENT_TYPING = "typing";
    public static final String EXTRA_DATA = "extra_data_message";
    public static final String EXTRA_MEMBER_NAME = "extra_data_member";
    public static final String EXTRA_USER_NAME = "extra_user_name";
    public static final String EXTRA_EVENT_TYPE = "extra_event_type";
    public static final String SOCKET_PRIVATE_CHAT = "/topic/private.chat.";
    public static int ROOM_ID = 9;


    private static final String TAG = SocketIOService.class.getSimpleName();
    private Socket mSocket;
    private Boolean isConnected = true;
    private boolean mTyping;
//    private Queue<Message> chatQueue;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private HeartBeat heartBeat;
    private String mUserId;


//    public void setListenersMap(List<ChatListResponse> chatList) {
//        for (ChatListResponse chat :
//                chatList) {
//            listenersMap.put(SOCKET_PRIVATE_CHAT + chat.getId(), new SocketEventListener(SOCKET_PRIVATE_CHAT + chat.getId(), this));
//
//        }
//    }

    private ConcurrentHashMap<String, SocketEventListener> listenersMap;

    public void changeRoomId(int roomId) {
        ROOM_ID = roomId;

        listenersMap = new ConcurrentHashMap<>();

//        getSocketListener();
        // TODO: 14.07.2021 varsa ekleme
        listenersMap.put(SOCKET_PRIVATE_CHAT + ROOM_ID, new SocketEventListener(SOCKET_PRIVATE_CHAT + ROOM_ID, this));

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.arg1) {
                case 1:
                    Log.w(TAG, "Connected");
                    /*Toast.makeText(SocketIOService.this,
                            R.string.connect, Toast.LENGTH_LONG).show();*/
                    break;
                case 2:
                    Log.w(TAG, "Disconnected");
                    /*Toast.makeText(SocketIOService.this,ss
                            R.string.disconnect, Toast.LENGTH_LONG).show();*/
                    break;
                case 3:
                    Log.w(TAG, "Error in Connection");
                    /*Toast.makeText(SocketIOService.this,
                            R.string.error_connect, Toast.LENGTH_LONG).show();*/
                    break;
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        chatQueue = new LinkedList<>();
        listenersMap = new ConcurrentHashMap<>();
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread(TAG + "Args",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        IO.Options options = new IO.Options();
        String token = "Bearer " + PreferenceHelper.SharedPreferencesManager.Companion.getInstance().getAccessToken();

        try {
            mSocket = IO.socket(Constants.SOCKET_URL + URLEncoder.encode(token, StandardCharsets.UTF_8.displayName()), options);
        } catch (URISyntaxException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        getSocketListener();
//            mSocket.on("/topic/private.chat.4");

        for (Map.Entry<String, SocketEventListener> entry : listenersMap.entrySet()) {
            mSocket.on(entry.getKey(), entry.getValue());
        }
        /*mSocket.on("user joined", new SocketEventListener("user joined", this));
        mSocket.on("user left", new SocketEventListener("user left", this));
        mSocket.on("typing", new SocketEventListener("typing", this));
        mSocket.on("stop typing", new SocketEventListener("stop typing", this));*/
        mSocket.connect();
        heartBeat = new HeartBeat(this);
        heartBeat.start();
    }

    private void getSocketListener() {
        listenersMap.put(Socket.EVENT_CONNECT, new SocketEventListener(Socket.EVENT_CONNECT, this));
        listenersMap.put(Socket.EVENT_DISCONNECT, new SocketEventListener(Socket.EVENT_DISCONNECT, this));
        listenersMap.put(Socket.EVENT_CONNECT_ERROR, new SocketEventListener(Socket.EVENT_CONNECT_ERROR, this));
        listenersMap.put(Socket.EVENT_CONNECT_TIMEOUT, new SocketEventListener(Socket.EVENT_CONNECT_TIMEOUT, this));
//        listenersMap.put(EVENT_MESSAGE, new SocketEventListener(EVENT_MESSAGE, this));
//        listenersMap.put(EVENT_MESSAGE, new SocketEventListener(Constants.SOCKET_TOPIC + 9, this));
        listenersMap.put(SOCKET_PRIVATE_CHAT + ROOM_ID, new SocketEventListener(SOCKET_PRIVATE_CHAT + ROOM_ID, this));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (intent != null) {

            ChatSendMessage chat = intent.getParcelableExtra(EXTRA_DATA);

//            Message chat = intent.getParcelableExtra(EXTRA_DATA);
            int eventType = intent.getIntExtra(EXTRA_EVENT_TYPE, EVENT_TYPE_JOIN);

            switch (eventType) {
                case EVENT_TYPE_JOIN:
                    mUserId = intent.getStringExtra(EXTRA_USER_NAME);
                    if (!mSocket.connected()) {
                        mSocket.connect();
                        Log.i(TAG, "connecting socket...");
                    } else {
                        joinChat();
                    }
                    break;
                case EVENT_TYPE_MESSAGE:
                    if (isSocketConnected()) {
                        try {
                            sendMessage(chat, eventType);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // TODO: 12.07.2021 mesajla bizi
//                    {
//                        if (chat.getMessageType() == MessageType.PICTURE) {
////                            sendPictureImage(chat, eventType);
//                        } else {
////                            sendMessage(chat, eventType);
//                        }
////                        QueryUtils.saveMessage(this, chat);
//                    } else {
////                        chatQueue.add(chat);
//                    }
                    break;
                case EVENT_TYPE_TYPING:
                    if (isSocketConnected()) {
//                        sendMessage(chat, eventType);
                    }
                    break;
            }
        }
        return START_STICKY;
    }


    private boolean isSocketConnected() {
        if (null == mSocket) {
            return false;
        }
        if (!mSocket.connected()) {
            mSocket.connect();
            Log.i(TAG, "reconnecting socket...");
            return false;
        }

        return true;
    }

    @Override
    public void onHeartBeat() {
        if (mSocket != null && !mSocket.connected()) {
            mSocket.connect();
            Log.i(TAG, "connecting socket...");
        }
    }

    private void joinChat() {
        if (TextUtils.isEmpty(mUserId)) {
            // user name is null can not join chat
            return;
        }

        JSONObject data = new JSONObject();
        try {
            data.put("user_id", mUserId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit(EVENT_JOIN, data);
//        resendQueueMessages();
    }

    private void sendMessage(ChatSendMessage message, int event) throws JSONException {
//        JSONObject chat = new JSONObject();
//        try {
//            chat.put("message_id", message.getId());
//            chat.put("sender_id", message.getSenderId());
//            chat.put("sender_name", message.getSenderName());
//            chat.put("receiver_id", message.getReceiverId());
//            if (!TextUtils.isEmpty(message.getChatMessage())) {
//                chat.put("message", message.getChatMessage());
//            }
//            if (!TextUtils.isEmpty(message.getImageUrl())) {
//                chat.put("image_url", CHAT_IMAGE_URL + message.getImageUrl());
//            }
//            chat.put("message_type", message.getMessageType().getValue());
//            chat.put("date", message.getTime());
//            chat.put("event", event);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Gson gson = new Gson();
        String obj = gson.toJson(message, ChatSendMessage.class);
        mSocket.emit(Constants.SOCKET_PRIVATE_CHAT, new JSONObject(obj));
        Log.w(TAG, "message sent " + obj.toString());

        mSocket.emit(SOCKET_PRIVATE_CHAT + ROOM_ID, new JSONObject(obj));


//        mSocket.on("/topic/private.chat.9", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                NotificationHelper.generateNotification(this, "senderName", "message");
//
//            }
//        });
    }

//    private void resendQueueMessages() {
//        Message chat = chatQueue.poll();
//        if (chat != null) {
//            sendMessage(chat, EVENT_TYPE_MESSAGE);
//            resendQueueMessages();
//        }
//    }

//    public void sendPictureImage(Message data, int eventType) {
//
//        MessageService request = (MessageService) RetrofitCall.createRequest(MessageService.class);
//
//        MultipartBody.Part fileBody = null;
//        if (data.getImageUri() != null) {
//            String path = FileUtils.getPath(this, data.getImageUri());
//            Uri destUri = FileUtils.saveFile(path);
//            fileBody = RetrofitCall.prepareFilePart(this, "image", data.getImageUri());
//        }
//
//        RequestBody senderId = RetrofitCall.prepareStringPart(data.getSenderId());
//        RequestBody senderName = RetrofitCall.prepareStringPart(data.getSenderName());
//        RequestBody receiverId = RetrofitCall.prepareStringPart(data.getReceiverId());
//        RequestBody message = RetrofitCall.prepareStringPart(data.getChatMessage());
//        RequestBody messageType = RetrofitCall.prepareStringPart(String.valueOf(data.getMessageType().getValue()));
//        RequestBody event = RetrofitCall.prepareStringPart(String.valueOf(eventType));
//
//        request.sendPictureImage(senderId,
//                senderName,
//                receiverId,
//                message,
//                messageType,
//                event,
//                fileBody).enqueue(new RetrofitCallback<Message>() {
//            @Override
//            public void onResponse(Message response) {
//                if (response.getStatus() == SUCCESS) {
//                    sendMessage(response.getData().get(0), EVENT_TYPE_MESSAGE);
//                } else {
//                    Toast.makeText(SocketIOService.this, response.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Toast.makeText(SocketIOService.this, "Failed to send picture message", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mSocket.disconnect();
//        heartBeat.stop();
//        mUserId = null;
//        // clear chat queue if service stop
//        //chatQueue.clear();
//        for (Map.Entry<String, SocketEventListener> entry : listenersMap.entrySet()) {
//            mSocket.off(entry.getKey(), entry.getValue());
//        }
//        Log.w(TAG, "onStop Service");
        /*mSocket.off("user joined", new SocketEventListener("user joined", this));
        mSocket.off("user left", new SocketEventListener("user left", this));
        mSocket.off("typing", new SocketEventListener("typing", this));
        mSocket.off("stop typing", new SocketEventListener("stop typing", this));*/
    }


    @Override
    public void onEventCall(String event, Object... args) {

        android.os.Message msg = mServiceHandler.obtainMessage();

        if (event.equals(Socket.EVENT_CONNECT)) {
            msg.arg1 = 1;
            mServiceHandler.sendMessage(msg);
            isConnected = true;
        } else if (event.equals(Socket.EVENT_DISCONNECT)) {
            Log.w(TAG, "socket disconnected");
            isConnected = false;
            msg = mServiceHandler.obtainMessage();
            msg.arg1 = 2;
            mServiceHandler.sendMessage(msg);
        } else if (event.equals(Socket.EVENT_DISCONNECT)) {
            msg = mServiceHandler.obtainMessage();
            msg.arg1 = 3;
            mServiceHandler.sendMessage(msg);
            // reconnect
            mSocket.connect();
        } else if (event.equals(Socket.EVENT_CONNECT_ERROR)) {
            isConnected = false;
            msg = mServiceHandler.obtainMessage();
            msg.arg1 = 3;
            mServiceHandler.sendMessage(msg);
            // reconnect
            mSocket.connect();


        } else if (event.equals(Socket.EVENT_CONNECT_TIMEOUT)) {
            if (!mTyping) return;

            mTyping = false;
            mSocket.emit("stop typing");

        } else if (event.equals(SOCKET_PRIVATE_CHAT + ROOM_ID)) {
            JSONObject object = (JSONObject) args[0];
            JsonParser parser = new JsonParser();
            JsonElement mJson = parser.parse(object.toString());
            Gson gson = new Gson();
            ChatRoomResponse response = gson.fromJson(mJson, ChatRoomResponse.class);
            System.out.println("Message :" + object.toString());
//                EventBus.getDefault().post(new ChatEvent(roomId,response.getId(),response.getContent(),response.getSenderMemberId(),response.getTime()));

            // TODO: 8.07.2021 Eventbusla mesaj güncelle

            // TODO: 8.07.2021 match bildiri ile tekrar subscribinggg

            JSONObject data = (JSONObject) args[0];
            Log.w(TAG, "message : " + data.toString());
//                Intent intent = new Intent();
//                intent.setAction(KEY_BROADCAST_MESSAGE);
//                intent.putExtra("test", "test");
//                    intent.putExtra("sender_id", senderId);
//                    intent.putExtra("sender_name", senderName);
//                    intent.putExtra("event", messageEvent);
//                    intent.putExtra("message_type", messageType.getValue());
//                sendBroadcast(intent);

            // TODO: 14.07.2021 rooma sub ol ama otekileri backgroundda al

            EventBus.getDefault().post(new ChatEvent(9, response.getId(), response.getContent(), response.getSenderMemberId(), response.getTime()));

//                Bekir bekir = new Bekir();
//                bekir.notifyWithExtra(PethiioApplication.context,"3");
//            NotificationUtils notificationUtils = new NotificationUtils(PethiioApplication.context);
//            notificationUtils.showNotificationMessage(String.valueOf(response.getSenderMemberId()), response.getContent(), null, null);
//
//                    int messageEvent = data.getInt("event");
////                    MessageType messageType = MessageType.getMessageType(data.getInt("message_type"));
//                    String messageId = data.has("message_id") ? data.getString("message_id") : "";
//                    String senderId = data.getString("sender_id");
//                    String senderName = data.getString("sender_name");
//                    String message = data.has("message") ? data.getString("message") : "";
//                    String imageUrl = data.has("image_url") ? data.getString("image_url") : "";
//                    String receiverId = data.getString("receiver_id");
//                    long date = data.getLong("date");

//                    if (messageEvent == EVENT_TYPE_MESSAGE) {
//                        Message chat = new Message.Builder()
//                                .messageId(messageId)
//                                .receiverId(receiverId)
//                                .senderId(senderId)
//                                .senderName(senderName)
//                                .message(message)
//                                .imageUrl(imageUrl)
//                                .messageType(messageType)
//                                .time(date)
//                                .build();
//                        QueryUtils.saveMessage(this, chat);
            //MessageUtils.playNotificationRingtone(getApplicationContext()); // play notification sound
//                        if (!Utils.isChatActivityRunning(ChatActivity.class.getClass())) {

            //                NotificationHelper.generateNotification( "senderName", "message");
//                        }
//                    }

        }
    }
}
