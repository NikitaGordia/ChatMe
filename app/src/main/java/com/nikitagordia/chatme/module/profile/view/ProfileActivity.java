package com.nikitagordia.chatme.module.profile.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityProfileBinding;
import com.nikitagordia.chatme.module.chat.view.ChatActivity;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.main.profile.view.ListAdapter;
import com.nikitagordia.chatme.module.main.profile.view.ProfileFragment;
import com.nikitagordia.chatme.module.main.users.model.User;
import com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private static final String EXTRA_UID = "com.nikitagordia.chatme.module.profile.view.ProfileActivity.uid";

    private User user;

    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private FirebaseMessaging messaging;

    private ActivityProfileBinding bind;
    private ListAdapter adapter;
    private ProgressDialog dialog;

    private int status;

    public static Intent getIntent(String uid, Context context) {
        return new Intent(context, ProfileActivity.class).putExtra(EXTRA_UID, uid);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_profile);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        messaging = FirebaseMessaging.getInstance();

        status = -1;

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);

        if (getIntent() != null) showUserProfile(getIntent().getStringExtra(EXTRA_UID));

        bind.status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null || user.getUid() == null) return;
                if (status == 3) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.new_friend), Toast.LENGTH_SHORT).show();
                    db.getReference().child("user").child(auth.getCurrentUser().getUid()).child("friend_id").push().setValue(user.getUid());
                    db.getReference().child("user").child(user.getUid()).child("follower_id").push().setValue(auth.getCurrentUser().getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            status = 2;
                            bind.statusImg.setImageResource(R.drawable.icon_friend);
                            bind.statusTv.setText(R.string.friend);
                        }
                    });
                } else if (status == 2) {
                    Toast.makeText(ProfileActivity.this, getResources().getString(R.string.remove_friend), Toast.LENGTH_SHORT).show();
                    db.getReference().child("user").child(user.getUid()).child("follower_id").runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            for (MutableData data : mutableData.getChildren())
                                if (auth.getCurrentUser().getUid().equals((String)data.getValue())) {
                                    data.setValue(null);
                                    break;
                                }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                            status = 3;
                            bind.statusImg.setImageResource(R.drawable.icon_follow);
                            bind.statusTv.setText(R.string.follow);
                        }
                    });
                    db.getReference().child("user").child(auth.getCurrentUser().getUid()).child("friend_id").runTransaction(new Transaction.Handler() {
                        @Override
                        public Transaction.Result doTransaction(MutableData mutableData) {
                            for (MutableData data : mutableData.getChildren())
                                if (user.getUid().equals((String)data.getValue())) {
                                    data.setValue(null);
                                    break;
                                }
                            return Transaction.success(mutableData);
                        }

                        @Override
                        public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {

                        }
                    });
                }
            }
        });

        bind.postList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(this, this, bind.postList, new ProfileFragment.OnLikeCallback() {
            @Override
            public void onLike(final String postId) {
                db.getReference().child("post").child(postId).child("like_id").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren())
                            if (auth.getCurrentUser().getUid().equals((String)data.getValue())) return;
                        db.getReference().child("post").child(postId).child("like_id").push().setValue(auth.getCurrentUser().getUid());
                        db.getReference().child("post").child(postId).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final long x = (long)dataSnapshot.getValue() + 1;
                                db.getReference().child("post").child(postId).child("like").setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        adapter.updateLike(postId, x);
                                    }
                                });
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
        });
        bind.postList.setAdapter(adapter);
    }

    private void showUserProfile(final String uid) {
        dialog.show();

        db.getReference().child("user").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUid(uid);
                bind.userEmail.setText(user.getEmail());
                bind.userName.setText(user.getName());
                if (user.getPhoto_url() != null) Picasso.with(ProfileActivity.this).load(user.getPhoto_url()).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_XXL, ImageUtils.SIZE_XXL).into(bind.photo);
                setupStatus();
                dialog.cancel();
                loadPosts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.cancel();
                Toast.makeText(ProfileActivity.this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPosts() {
        db.getReference().child("user").child(user.getUid()).child("post_id").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                db.getReference().child("post").child((String)dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        BlogPost post = dataSnapshot.getValue(BlogPost.class);
                        if (post.getOwner_id().equals(user.getUid())) adapter.addPost(post);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            adapter.updatePost(
                    data.getStringExtra(PostDetailActivity.EXTRA_ID),
                    data.getLongExtra(PostDetailActivity.EXTRA_LIKE, 0),
                    data.getLongExtra(PostDetailActivity.EXTRA_COMMENT, 0),
                    data.getLongExtra(PostDetailActivity.EXTRA_VIEW, 0)
            );
        }
    }

    private void setupStatus() {
        if (user.getUid().equals(auth.getCurrentUser().getUid())) {
            status = 1;
            bind.statusImg.setImageResource(R.drawable.icon_home);
            bind.statusTv.setText(R.string.you);
            bind.status.setVisibility(View.VISIBLE);
            bind.chatBtn.setVisibility(View.INVISIBLE);
            return;
        }
        db.getReference().child("user").child(user.getUid()).child("follower_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bind.status.setVisibility(View.VISIBLE);
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    if (auth.getCurrentUser().getUid().equals((String) snap.getValue())) {
                        status = 2;
                        bind.statusImg.setImageResource(R.drawable.icon_friend);
                        bind.statusTv.setText(R.string.friend);
                        return;
                    }
                }
                status = 3;
                bind.statusImg.setImageResource(R.drawable.icon_follow);
                bind.statusTv.setText(R.string.follow);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bind.chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getReference().child("user").child(user.getUid()).child("chat_id").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            String key = db.getReference().child("chat").push().getKey();
                            messaging.subscribeToTopic(key);
                            db.getReference().child("user").child(user.getUid()).child("chat_id").child(auth.getCurrentUser().getUid()).setValue(key);
                            db.getReference().child("user").child(auth.getCurrentUser().getUid()).child("chat_id").child(user.getUid()).setValue(key);
                            db.getReference().child("chat").child(key).child("user_id").push().setValue(user.getUid());
                            db.getReference().child("chat").child(key).child("user_id").push().setValue(auth.getCurrentUser().getUid());

                            startActivity(ChatActivity.getIntent(key, ProfileActivity.this));
                        } else {
                            startActivity(ChatActivity.getIntent((String)dataSnapshot.getValue(), ProfileActivity.this));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
    }
}
