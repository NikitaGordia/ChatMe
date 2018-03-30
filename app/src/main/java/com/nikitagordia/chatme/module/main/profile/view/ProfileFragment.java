package com.nikitagordia.chatme.module.main.profile.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.FragmentProfileBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.signin.view.SigninActivity;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseDatabase db;

    private FragmentProfileBinding bind;

    private ProgressDialog dialog;

    private ListAdapter adapter;

    private String userName, userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = FragmentProfileBinding.inflate(inflater, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        userId = auth.getCurrentUser().getUid();

        adapter = new ListAdapter(getContext(), getActivity(), bind.postList);
        bind.postList.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.postList.setAdapter(adapter);

        bind.addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileFragment.this.getContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_add_post, null);
                final EditText content = (EditText) view.findViewById(R.id.content);
                final TextView post = (TextView) view.findViewById(R.id.post);
                builder.setView(view);
                final AlertDialog d = builder.create();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
                post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        d.cancel();
                        createPost(content.getText().toString());
                    }
                });
            }
        });

        db.getReference().child("user").child(userId).child("post_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                db.getReference().child("post").child((String)dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.addPost(dataSnapshot.getValue(BlogPost.class));
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


        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();

        db.getReference().child("user").child(userId).child("name").runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                dialog.cancel();
                if (dataSnapshot == null) {
                    Toast.makeText(getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    return;
                }
                userName = (String) dataSnapshot.getValue();
                if (bind != null && bind.userName != null) bind.userName.setText(userName);
            }
        });

        String userEmail = "";
        if (auth.getCurrentUser().getEmail() != null) userEmail = auth.getCurrentUser().getEmail(); else userEmail = auth.getCurrentUser().getPhoneNumber();
        bind.userEmail.setText(userEmail);

        bind.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getContext(), SigninActivity.class));
                getActivity().finish();
            }
        });

        return bind.getRoot();
    }

    void createPost(String content) {
        String key = db.getReference().child("post").push().getKey();
        BlogPost post = new BlogPost(content, key, userId, userName);
        db.getReference().child("post").child(key).setValue(post);
        db.getReference().child("user").child(userId).child("post_id").push().setValue(key);
    }
}
