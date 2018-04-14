package com.nikitagordia.chatme.module.chat.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityChatBinding;
import com.nikitagordia.chatme.module.chat.model.Message;
import com.nikitagordia.chatme.utils.Const;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CHAT_ID = "com.nikitagordia.chatme.module.chat.view.ChatActivity.chat_id";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "ChatActivity";

    private ActivityChatBinding bind;
    private MessageAdapter adapter;

    private FirebaseDatabase db;
    private FirebaseAuth auth;

    private OkHttpClient client;

    private String chatId;

    public static Intent getIntent(String chatId, Context context) {
        Intent i = new Intent(context, ChatActivity.class);
        i.putExtra(EXTRA_CHAT_ID, chatId);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bind = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        chatId = getIntent().getStringExtra(EXTRA_CHAT_ID);

        if (chatId == null) finish();

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        client = new OkHttpClient();

        bind.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bind.content.getText().toString().isEmpty()) return;
                createMessage(bind.content.getText().toString());
                bind.content.setText("");
            }
        });

        adapter = new MessageAdapter(this, bind.messageList, auth.getCurrentUser().getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        bind.messageList.setLayoutManager(linearLayoutManager);
        bind.messageList.setAdapter(adapter);

        db.getReference().child("chat").child(chatId).child("message_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                db.getReference().child("message").child((String)dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.addMessage(dataSnapshot.getValue(Message.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void createMessage(String content) {
        String key = db.getReference().child("message").push().getKey();
        db.getReference().child("chat").child(chatId).child("message_id").push().setValue(key);
        Message message = new Message(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), content, auth.getCurrentUser().getPhotoUrl().toString());
        db.getReference().child("message").child(key).setValue(message);

        try {

            JSONObject obj = new JSONObject();
            obj.put("to", "/topics/" + chatId);
            JSONObject data = new JSONObject();
            data.put("chat_id", chatId);
            data.put("owner_nickname", auth.getCurrentUser().getDisplayName());
            data.put("owner_photo_url", auth.getCurrentUser().getPhotoUrl().toString());
            data.put("content", content);
            data.put("date", message.getDate());
            data.put("owner_id", auth.getCurrentUser().getUid());
            obj.put("data", data);

            client.newCall(new Request.Builder()
                    .url(Const.NOTIFICATION_URL)
                    .addHeader("Content-Type", Const.CONTENT_TYPE)
                    .addHeader("Authorization", Const.AUTH_KEY)
                    .post(RequestBody.create(JSON, obj.toString()))
                    .build()
            ).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "Error Request " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.d(TAG, "Responce " + response.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
