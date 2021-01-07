package com.ruslangrigoriev.chatapp.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.contacts.ContactsActivity;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.messaging.MessageActivity;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<User> users;
    private boolean isChat;

    public UserAdapter(Context context, List<User> users, boolean isChat) {
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        holder.usernameTV.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profileImageIv.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profileImageIv);
        }
        if(isChat){
            if(user.getStatus().equals("online")){
                holder.statusOn.setVisibility(View.VISIBLE);
                holder.statusOff.setVisibility(View.GONE);
            } else {
                holder.statusOn.setVisibility(View.GONE);
                holder.statusOff.setVisibility(View.VISIBLE);
            }
        } else {
            holder.statusOn.setVisibility(View.GONE);
            holder.statusOff.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid",user.getId());
                context.startActivity(intent);
                if(context instanceof ContactsActivity){
                    ((ContactsActivity) context).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView usernameTV;
        private ImageView profileImageIv;
        private ImageView statusOn;
        private ImageView statusOff;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameTV = itemView.findViewById(R.id.username);
            profileImageIv = itemView.findViewById(R.id.profile_image);
            statusOn = itemView.findViewById(R.id.status_on);
            statusOff = itemView.findViewById(R.id.status_off);

        }
    }
}
