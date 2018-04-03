package com.nikitagordia.chatme.module.postdetail.view.likedialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikitagordia.chatme.databinding.DialogLikesBinding;
import com.nikitagordia.chatme.module.main.users.model.User;

/**
 * Created by nikitagordia on 4/3/18.
 */

public class LikesDialog extends BottomSheetDialogFragment {

    private static final String EXTRA_POST_ID = "com.nikitagordia.chatme.module.postdetail.view.likedialog.LikesDialog.postId";

    private DialogLikesBinding bind;
    private LikeAdapter adapter;

    private FirebaseDatabase db;

    private String postId;

    public static LikesDialog getDialogInstance(String postId) {
        LikesDialog result = new LikesDialog();
        Bundle b = new Bundle();
        b.putString(EXTRA_POST_ID, postId);
        result.setArguments(b);
        return result;
    }

    public LikesDialog() {
        db = FirebaseDatabase.getInstance();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        bind = DialogLikesBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(bind.getRoot());

        bind.likeList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapter = new LikeAdapter(getContext());
        bind.likeList.setAdapter(adapter);

        db.getReference().child("post");

        postId = getArguments().getString(EXTRA_POST_ID);

        db.getReference().child("post").child(postId).child("like_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren())
                    db.getReference().child("user").child((String)snap.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            adapter.addUser(dataSnapshot.getValue(User.class));
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
    }
}
