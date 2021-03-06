package com.ruslangrigoriev.chatapp.messaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context context;
    private List<Message> chats;
    private String imageURL;
    String currentID = App.getInstance().authService.getCurrentUserUID();

    public MessageAdapter(Context context, List<Message> chats, String imageURL) {
        this.context = context;
        this.chats = chats;
        this.imageURL = imageURL;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = chats.get(position);

        holder.showMsgTV.setText(message.getMessageText());

        if(imageURL.equals("default")){
            holder.profileIV.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageURL).into(holder.profileIV);
        }

        if(message.getSender().equals(currentID)) {
            holder.txtSeen.setVisibility(View.VISIBLE);
            if(message.getIsSeen()){
                holder.txtSeen.setText("✓✓");
            } else {
                holder.txtSeen.setText(" ✓");
            }
        } else {
            holder.txtSeen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView showMsgTV;
        private ImageView profileIV;
        private TextView txtSeen;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            showMsgTV = itemView.findViewById(R.id.show_message);
            profileIV = itemView.findViewById(R.id.profile_image);
            txtSeen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(currentID)){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
