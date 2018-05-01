package com.nikitagordia.chatme.module.main.chats.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nikitagordia.chatme.databinding.FragmentChatBinding;
import com.nikitagordia.chatme.module.main.chats.model.Chat;
import com.nikitagordia.chatme.module.main.users.model.User;

/**
 * Created by nikitagordia on 4/6/18.
 */

public class ChatsFragment extends Fragment {

    private FragmentChatBinding bind;
    private ChatAdapter adapter;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private FirebaseMessaging messaging;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentChatBinding.inflate(inflater);

        bind.chatList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatAdapter(getContext());
        bind.chatList.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        messaging = FirebaseMessaging.getInstance();

        db.getReference().child("user").child(auth.getCurrentUser().getUid()).child("chat_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final Chat chat = new Chat((String)dataSnapshot.getValue());
                db.getReference().child("chat").child(chat.getChat_id()).child("message_id").limitToLast(1).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren()) {
                            db.getReference().child("message").child((String)snap.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    chat.setLast_message((String)dataSnapshot.child("content").getValue());
                                    chat.setTime((String)dataSnapshot.child("date").getValue());
                                    db.getReference().child("chat").child(chat.getChat_id()).child("user_id").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snap : dataSnapshot.getChildren())
                                                if (!auth.getCurrentUser().getUid().equals((String)snap.getValue())) {
                                                    db.getReference().child("user").child((String)snap.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            User u = dataSnapshot.getValue(User.class);
                                                            chat.setChat_name(u.getName());
                                                            chat.setPhoto_url(u.getPhoto_url());
                                                            adapter.update(chat);
                                                            messaging.subscribeToTopic(chat.getChat_id());
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    break;
                                                }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            break;
                        }
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

        return bind.getRoot();
    }
}
