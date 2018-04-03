package com.nikitagordia.chatme.module.chat.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CHAT_ID = "com.nikitagordia.chatme.module.chat.view.ChatActivity.chat_id";

    private ActivityChatBinding bind;
    private MessageAdapter adapter;

    private FirebaseDatabase db;
    private FirebaseAuth auth;

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

        bind.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMessage(bind.content.getText().toString());
                bind.content.setText("");
            }
        });

        adapter = new MessageAdapter(bind.messageList, auth.getCurrentUser().getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        bind.messageList.setLayoutManager(linearLayoutManager);
        bind.messageList.setAdapter(adapter);

        db.getReference().child("chat").child(chatId).addChildEventListener(new ChildEventListener() {
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
        db.getReference().child("chat").child(chatId).push().setValue(key);
        db.getReference().child("message").child(key).setValue(new Message(auth.getCurrentUser().getUid(), auth.getCurrentUser().getDisplayName(), content));
    }
}
