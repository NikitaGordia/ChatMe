package com.nikitagordia.chatme.module.chat.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.databinding.LayoutMessageHolderRightBinding;
import com.nikitagordia.chatme.module.chat.model.Message;
import com.nikitagordia.chatme.databinding.LayoutMessageHolderLeftBinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nikitagordia on 4/3/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private RecyclerView view;
    private String auth;

    private List<Message> list;

    public MessageAdapter(RecyclerView view, String auth) {
        this.view = view;
        this.auth = auth;
        list = new ArrayList<>();
    }

    @Override
    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) return new LeftMessageHolder(LayoutMessageHolderLeftBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
                      else return new RightMessageHolder(LayoutMessageHolderRightBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void addMessage(Message message) {
        list.add(0, message);
        notifyItemInserted(0);
        view.scrollToPosition(0);
    }

    @Override
    public void onBindViewHolder(MessageHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getOwner_id().equals(auth)) return 0; else return 1;
    }

    public class RightMessageHolder extends MessageHolder {

        private LayoutMessageHolderRightBinding bind;

        public RightMessageHolder(LayoutMessageHolderRightBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        @Override
        void bind(Message message) {
            bind.content.setText(message.getContent());
            bind.date.setText(message.getDate());
            bind.nickname.setText(message.getOwner_nickname());
        }
    }

    public class LeftMessageHolder extends MessageHolder {

        private LayoutMessageHolderLeftBinding bind;

        public LeftMessageHolder(LayoutMessageHolderLeftBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
        }

        @Override
        void bind(Message message) {
            bind.content.setText(message.getContent());
            bind.date.setText(message.getDate());
            bind.nickname.setText(message.getOwner_nickname());
        }
    }

    public abstract class MessageHolder extends RecyclerView.ViewHolder {

        public MessageHolder(View itemView) {
            super(itemView);
        }

        abstract void bind(Message message);
    }
}
