package com.nikitagordia.chatme.module.postdetail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityPostDetailBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;

public class PostDetail extends AppCompatActivity {
    
    private static final String EXTRA_NAME = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.name";
    private static final String EXTRA_OWNER_ID = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.owner_id";
    private static final String EXTRA_DATE = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.date";
    private static final String EXTRA_CONTENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.context";
    private static final String EXTRA_LIKE = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.like";
    private static final String EXTRA_COMMENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.comment";
    private static final String EXTRA_VIEW = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.view";

    private ActivityPostDetailBinding bind;

    private String ownerId;

    private View.OnClickListener onClickShowUser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ownerId == null || ownerId.isEmpty()) return;
            startActivity(ProfileActivity.getIntent(ownerId, PostDetail.this), ActivityOptionsCompat.makeSceneTransitionAnimation(PostDetail.this,
                    new Pair<View, String>(bind.nickname, "nickname"),
                    new Pair<View, String>(bind.photo, "photo")
            ).toBundle());
        }
    };
    
    public static Intent getIntent(Context context, BlogPost post) {
        return new Intent(context, PostDetail.class)
                .putExtra(EXTRA_NAME, post.getOwner_name())
                .putExtra(EXTRA_OWNER_ID, post.getOwner_id())
                .putExtra(EXTRA_DATE, post.getDate())
                .putExtra(EXTRA_CONTENT, post.getContent())
                .putExtra(EXTRA_LIKE, post.getLike())
                .putExtra(EXTRA_COMMENT, post.getComment())
                .putExtra(EXTRA_VIEW, post.getView());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind = DataBindingUtil.setContentView(this, R.layout.activity_post_detail);

        Intent i = getIntent();
        if (i != null) {
            bind.nickname.setText(i.getStringExtra(EXTRA_NAME));
            bind.date.setText(i.getStringExtra(EXTRA_DATE));
            bind.content.setText(i.getStringExtra(EXTRA_CONTENT));

            ownerId = i.getStringExtra(EXTRA_OWNER_ID);

            bind.like.setText(getResources().getString(R.string.like_cnt, i.getIntExtra(EXTRA_LIKE, 0)));
            bind.comment.setText(getResources().getString(R.string.comment_cnt, i.getIntExtra(EXTRA_COMMENT, 0)));
            bind.view.setText(getResources().getString(R.string.view_cnt, i.getIntExtra(EXTRA_VIEW, 0)));
        }

        bind.nickname.setOnClickListener(onClickShowUser);
        bind.photo.setOnClickListener(onClickShowUser);
    }
}
