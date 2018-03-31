package com.nikitagordia.chatme.module.postdetail.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.databinding.LayoutCommentHolderBinding;
import com.nikitagordia.chatme.module.postdetail.model.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitagordia on 3/31/18.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private List<Comment> list;

    private Context context;
    private RecyclerView recyclerView;

    public CommentAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
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

        private LayoutCommentHolderBinding bind;

        public CommentHolder(LayoutCommentHolderBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        public void bind(Comment comment) {
            bind.nickname.setText(comment.getOwner_name());
            bind.context.setText(comment.getContext());
            //TODO show user on photo
        }
    }
}
