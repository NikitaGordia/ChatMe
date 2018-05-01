package com.nikitagordia.chatme.module.main.chats.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nikitagordia.chatme.R;
import com.nikitagordia.chatme.databinding.LayoutChatHolderBinding;
import com.nikitagordia.chatme.module.chat.view.ChatActivity;
import com.nikitagordia.chatme.module.main.chats.model.Chat;
import com.nikitagordia.chatme.utils.ImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikitagordia on 4/6/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private Context context;
    private List<Chat> list;

    public ChatAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void update(Chat chat) {
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getChat_id().equals(chat.getChat_id())) {
                list.get(i).setLast_message(chat.getLast_message());
                notifyItemChanged(i);
                return;
            }
        list.add(0, chat);
        notifyItemInserted(0);
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutChatHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LayoutChatHolderBinding bind;

        private String chatId;

        public ChatHolder(LayoutChatHolderBinding bind) {
            super(bind.getRoot());
            this.bind = bind;
            this.bind.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(ChatActivity.getIntent(chatId, context));
        }

        public void bind(Chat chat) {
            chatId = chat.getChat_id();
            bind.chatName.setText(chat.getChat_name());
            bind.lastMessage.setText(chat.getLast_message());
            bind.time.setText(chat.getTime());

            if (chat.getPhoto_url() != null) {
                Picasso.with(context).load(chat.getPhoto_url()).placeholder(R.drawable.user_photo_holder).resize(ImageUtils.SIZE_XL, ImageUtils.SIZE_XL).into(bind.photo);
            }
        }
    }
}
