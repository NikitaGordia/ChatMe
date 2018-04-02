package com.nikitagordia.chatme.module.postdetail.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.nikitagordia.chatme.databinding.DialogCommentsBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.postdetail.model.Comment;

/**
 * Created by nikitagordia on 3/31/18.
 */

public class CommentsDialog extends BottomSheetDialogFragment {

    private static final String EXTRA_POST_ID = "com.nikitagordia.chatme.module.postdetail.view.CommentsDialog.postId";

    private DialogCommentsBinding bind;
    private FirebaseDatabase db;
    private FirebaseAuth auth;

    private CommentAdapter adapter;

    private String postId;

    private boolean set = false;

    private OnCloseListener callback;



    public static CommentsDialog getDialog(String commentId) {
        CommentsDialog dialog = new CommentsDialog();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_POST_ID, commentId);
        dialog.setArguments(bundle);
        return dialog;
    }

    public CommentsDialog() {
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void setCallback(OnCloseListener callback) {
        this.callback = callback;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        callback.onClose();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        bind = DialogCommentsBinding.inflate(LayoutInflater.from(getContext()));
        dialog.setContentView(bind.getRoot());

        if (!set) {
            set = true;
            postId = getArguments().getString(EXTRA_POST_ID);
            adapter = new CommentAdapter(getContext(), getActivity());
            db.getReference().child("post").child(postId).child("comment_id").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    db.getReference().child("comment").child((String)dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (adapter != null) adapter.addComment(dataSnapshot.getValue(Comment.class));
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

        adapter.setRecyclerView(bind.commentList);
        bind.commentList.setLayoutManager(new LinearLayoutManager(getContext()));
        bind.commentList.setAdapter(adapter);

        bind.sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = db.getReference().child("comment").push().getKey();
                db.getReference().child("comment").child(s).child("context").setValue(bind.comment.getText().toString());
                bind.comment.setText("");
                db.getReference().child("comment").child(s).child("owner_id").setValue(auth.getCurrentUser().getUid());
                db.getReference().child("comment").child(s).child("owner_name").setValue(auth.getCurrentUser().getDisplayName());
                db.getReference().child("post").child(postId).child("comment_id").push().setValue(s);

                db.getReference().child("post").child(postId).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final long x = (long)dataSnapshot.getValue() + 1;
                        db.getReference().child("post").child(postId).child("comment").setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ((PostDetailActivity) getActivity()).commentAdded(x);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public interface OnCloseListener {
        void onClose();
    }
}
