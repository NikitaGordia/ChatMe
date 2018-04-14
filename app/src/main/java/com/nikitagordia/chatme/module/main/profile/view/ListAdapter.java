package com.nikitagordia.chatme.module.main.profile.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutBlogPostBinding;
import com.nikitagordia.chatme.module.postdetail.view.PostDetailActivity;
import com.nikitagordia.chatme.module.main.profile.model.BlogPost;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.nikitagordia.chatme.utils.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitagordia on 3/28/18.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.PostHolder> {

    private List<BlogPost> list;
    private Activity activity;
    private Context context;
    private RecyclerView view;
    private ProfileFragment.OnLikeCallback likeCallback;

    public ListAdapter(Context context, Activity activity, RecyclerView view, ProfileFragment.OnLikeCallback likeCallback) {
        list = new ArrayList<>();
        this.activity = activity;
        this.context = context;
        this.view = view;
        this.likeCallback = likeCallback;
    }

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(LayoutBlogPostBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        holder.bindData(list.get(position));
    }

    public void addPost(BlogPost post) {
        list.add(0, post);
        notifyDataSetChanged();
        view.scrollToPosition(0);
    }

    public void updateLike(String postId, long likes) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId().equals(postId)) {
                list.get(i).setLike(likes);
                notifyItemChanged(i);
                return;
            }
    }

    public void updatePost(String post, long like, long comment, long view) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getId().equals(post)) {
                list.get(i).setLike(like);
                list.get(i).setComment(comment);
                list.get(i).setView(view);
                notifyItemChanged(i);
                return;
            }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder {

        private LayoutBlogPostBinding bind;
        private BlogPost post;

        private View.OnClickListener onClickShowBlog = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivityForResult(PostDetailActivity.getIntent(context, post), 0, ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        new Pair<View, String>(bind.nickname, "nickname"),
                        new Pair<View, String>(bind.photo, "photo"),
                        new Pair<View, String>(bind.date, "date"),
                        new Pair<View, String>(bind.content, "content"),
                        new Pair<View, String>(bind.like, "like"),
                        new Pair<View, String>(bind.comment, "comment"),
                        new Pair<View, String>(bind.view, "view"),
                        new Pair<View, String>(bind.postBody, "post_body")).toBundle());
            }
        };

        private View.OnClickListener onClickShowUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(ProfileActivity.getIntent(post.getOwner_id(), context), ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        new Pair<View, String>(bind.photo, "photo"),
                        new Pair<View, String>(bind.nickname, "nickname")).toBundle());
            }
        };

        public PostHolder(LayoutBlogPostBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            bind.content.setOnClickListener(onClickShowBlog);
            bind.date.setOnClickListener(onClickShowBlog);

            bind.nickname.setOnClickListener(onClickShowUser);
            bind.photo.setOnClickListener(onClickShowUser);
            bind.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeCallback.onLike(post.getId());
                }
            });
        }

        public void bindData(BlogPost post) {
            this.post = post;
            bind.nickname.setText(post.getOwner_name());
            bind.content.setText(StringUtils.cut(post.getContent(), 500));
            bind.date.setText(post.getDate());
            bind.like.setText(context.getResources().getString(R.string.like_cnt, post.getLike()));
            bind.comment.setText(context.getResources().getString(R.string.comment_cnt, post.getComment()));
            bind.view.setText(context.getResources().getString(R.string.view_cnt, post.getView()));
            if (post.getOwner_photo_url() != null) Picasso.with(context).load(post.getOwner_photo_url()).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_L, ImageUtils.SIZE_L).into(bind.photo);
        }

    }
}
