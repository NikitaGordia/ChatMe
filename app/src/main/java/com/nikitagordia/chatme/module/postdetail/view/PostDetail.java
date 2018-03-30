package com.nikitagordia.chatme.module.postdetail.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.ActivityPostDetailBinding;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;

public class PostDetail extends AppCompatActivity {
    
    private static final String EXTRA_NAME = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.name";
    private static final String EXTRA_DATE = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.date";
    private static final String EXTRA_CONTENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.context";
    private static final String EXTRA_LIKE = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.like";
    private static final String EXTRA_COMMENT = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.comment";
    private static final String EXTRA_VIEW = "com.nikitagordia.chatme.module.postdetail.view.PostDetail.view";

    private ActivityPostDetailBinding bind;
    
    public static Intent getIntent(Context context, BlogPost post) {
        Intent i = new Intent(context, PostDetail.class);
        i.putExtra(EXTRA_NAME, post.getOwner_name());
        i.putExtra(EXTRA_DATE, post.getDate());
        i.putExtra(EXTRA_CONTENT, post.getContent());
        i.putExtra(EXTRA_LIKE, post.getLike());
        i.putExtra(EXTRA_COMMENT, post.getComment());
        i.putExtra(EXTRA_VIEW, post.getView());
        return i;
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

            bind.like.setText(getResources().getString(R.string.like_cnt, i.getIntExtra(EXTRA_LIKE, 0)));
            bind.comment.setText(getResources().getString(R.string.comment_cnt, i.getIntExtra(EXTRA_COMMENT, 0)));
            bind.view.setText(getResources().getString(R.string.view_cnt, i.getIntExtra(EXTRA_VIEW, 0)));
        }

    }
}
