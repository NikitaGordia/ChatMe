package com.nikitagordia.chatme.module.postdetail.view.commentsdialog;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutCommentHolderBinding;
import com.nikitagordia.chatme.module.postdetail.model.Comment;
import com.nikitagordia.chatme.module.profile.view.ProfileActivity;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitagordia on 3/31/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> list;

    private Context context;
    private Activity activity;
    private RecyclerView recyclerView;

    public CommentAdapter(Context context, Activity activity) {
        list = new ArrayList<>();
        this.context = context;
        this.activity = activity;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void addComment(Comment comment) {
        list.add(0, comment);
        notifyItemInserted(0);
        if (recyclerView != null) recyclerView.scrollToPosition(0);
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentHolder(LayoutCommentHolderBinding.inflate(LayoutInflater.from(context), parent, false));
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        private Comment comment;

        private View.OnClickListener onClickShowUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(ProfileActivity.getIntent(comment.getOwner_id(), context), ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                        new Pair<View, String>(bind.nickname, "nickname"),
                        new Pair<View, String>(bind.photo, "photo")
                ).toBundle());
            }
        };

        private LayoutCommentHolderBinding bind;

        public CommentHolder(LayoutCommentHolderBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        public void bind(Comment comment) {
            this.comment = comment;
            bind.nickname.setText(comment.getOwner_name());
            bind.context.setText(comment.getContext());

            bind.nickname.setOnClickListener(onClickShowUser);
            bind.photo.setOnClickListener(onClickShowUser);

            if (comment.getOwner_photo_url() != null) Picasso.with(context).load(comment.getOwner_photo_url()).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_L, ImageUtils.SIZE_L).into(bind.photo);
        }
    }
}
