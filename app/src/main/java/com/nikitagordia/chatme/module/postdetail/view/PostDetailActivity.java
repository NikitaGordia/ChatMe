package com.nikitagordia.chatme.module.postdetail.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityPostDetailBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.postdetail.view.commentsdialog.CommentsDialog;
import com.nikitagordia.chatme.module.postdetail.view.likedialog.LikesDialog;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.id";
    private static final String EXTRA_NAME = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.name";
    private static final String EXTRA_OWNER_ID = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.owner_id";
    private static final String EXTRA_OWNER_PHOTO_URL = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.owner_photo_url";
    private static final String EXTRA_DATE = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.date";
    private static final String EXTRA_CONTENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.context";
    public static final String EXTRA_LIKE = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.like";
    public static final String EXTRA_COMMENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.comment";
    public static final String EXTRA_VIEW = "com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity.view";

    private ActivityPostDetailBinding bind;

    private String ownerId, postId;

    private CommentsDialog dialogComments;
    private LikesDialog dialogLikes;

    private FirebaseDatabase db;
    private FirebaseAuth auth;

    private View.OnClickListener onClickShowUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ownerId == null || ownerId.isEmpty()) return;
            startActivity(ProfileActivity.getIntent(ownerId, PostDetailActivity.this), ActivityOptionsCompat.makeSceneTransitionAnimation(PostDetailActivity.this,
                    new Pair<View, String>(bind.nickname, "nickname"),
                    new Pair<View, String>(bind.photo, "photo")
            ).toBundle());
        }
    };
    
    public static Intent getIntent(Context context, BlogPost post) {
        return new Intent(context, PostDetailActivity.class)
                .putExtra(EXTRA_ID, post.getId())
                .putExtra(EXTRA_NAME, post.getOwner_name())
                .putExtra(EXTRA_OWNER_ID, post.getOwner_id())
                .putExtra(EXTRA_DATE, post.getDate())
                .putExtra(EXTRA_CONTENT, post.getContent())
                .putExtra(EXTRA_LIKE, post.getLike())
                .putExtra(EXTRA_COMMENT, post.getComment())
                .putExtra(EXTRA_VIEW, post.getView())
                .putExtra(EXTRA_OWNER_PHOTO_URL, post.getOwner_photo_url());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        Intent i = getIntent();
        if (i != null) {
            bind.nickname.setText(i.getStringExtra(EXTRA_NAME));
            bind.date.setText(i.getStringExtra(EXTRA_DATE));
            bind.content.setText(i.getStringExtra(EXTRA_CONTENT));

            ownerId = i.getStringExtra(EXTRA_OWNER_ID);
            postId = i.getStringExtra(EXTRA_ID);

            bind.like.setText(getResources().getString(R.string.like_cnt, i.getLongExtra(EXTRA_LIKE, 0)));
            bind.comment.setText(getResources().getString(R.string.comment_cnt, i.getLongExtra(EXTRA_COMMENT, 0)));
            bind.view.setText(getResources().getString(R.string.view_cnt, i.getLongExtra(EXTRA_VIEW, 0)));

            String photo = i.getStringExtra(EXTRA_OWNER_PHOTO_URL);
            if (photo != null) Picasso.with(this).load(photo).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_XL, ImageUtils.SIZE_XL).into(bind.photo);

            db.getReference().child("post").child(postId).child("view_id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren())
                        if (auth.getCurrentUser().getUid().equals((String)snap.getValue())) return;
                    db.getReference().child("post").child(postId).child("view_id").push().setValue(auth.getCurrentUser().getUid());
                    db.getReference().child("post").child(postId).child("view").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final long x = (long)dataSnapshot.getValue() + 1;
                            db.getReference().child("post").child(postId).child("view").setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    bind.view.setText(getResources().getString(R.string.view_cnt, x));
                                    updateResult();
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

        bind.nickname.setOnClickListener(onClickShowUser);
        bind.photo.setOnClickListener(onClickShowUser);

        dialogComments = CommentsDialog.getDialog(postId);
        dialogComments.setCallback(new CommentsDialog.OnCloseListener() {
            @Override
            public void onClose() {
                updateResult();
            }
        });

        dialogLikes = LikesDialog.getDialogInstance(postId);

        bind.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getReference().child("post").child(postId).child("like_id").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap : dataSnapshot.getChildren())
                            if (auth.getCurrentUser().getUid().equals((String)snap.getValue())) return;
                        db.getReference().child("post").child(postId).child("like_id").push().setValue(auth.getCurrentUser().getUid());
                        db.getReference().child("post").child(postId).child("like").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final long x = (long)dataSnapshot.getValue() + 1;
                                db.getReference().child("post").child(postId).child("like").setValue(x).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        bind.like.setText(getResources().getString(R.string.like_cnt, x));
                                        updateResult();
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

        bind.showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogComments.show(getSupportFragmentManager(), "mytg");
            }
        });

        bind.showLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogLikes.show(getSupportFragmentManager(), "mytg");
            }
        });
    }

    public void updateResult() {
        db.getReference().child("post").child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Intent i = new Intent();
                i.putExtra(EXTRA_LIKE, (Long)dataSnapshot.child("like").getValue());
                i.putExtra(EXTRA_COMMENT, (Long)dataSnapshot.child("comment").getValue());
                i.putExtra(EXTRA_VIEW, (Long)dataSnapshot.child("view").getValue());
                i.putExtra(EXTRA_ID, postId);
                setResult(Activity.RESULT_OK, i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void commentAdded(long x) {
        bind.comment.setText(getResources().getString(R.string.comment_cnt, x));
    }
}
