package com.ruslangrigoriev.chatapp.messaging;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.Message;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity implements MessageActivityContract.View {

    private CircleImageView profileIv;
    private TextView username;
    private TextView status;
    private ImageButton btnSend;
    private MaterialEditText textSend;


    private MessagePresenter messagePresenter;

    private MessageAdapter messageAdapter;
    private List<Message> chatList;

    private RecyclerView recyclerView;

    private User user;

    Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messagePresenter = new MessagePresenter();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profileIv = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btnSend = findViewById(R.id.btn_send);
        textSend = findViewById(R.id.text_send);
        status = findViewById(R.id.status);

        Intent intent = getIntent();
        String receiverID = intent.getStringExtra("userid");

        messagePresenter.getUserByID(receiverID);
        messagePresenter.seenMessage(receiverID);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textSend.getText().toString();
                if(!msg.equals("")){
                    messagePresenter.onBtnSend(receiverID,msg);
                } else {
                    Toast.makeText(MessageActivity.this,"Can't send empty message", Toast.LENGTH_SHORT).show();
                }
                textSend.setText("");
            }
        });
    }


    @Override
    public void setUserInfo(User user) {
        this.user = user;
        username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            profileIv.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(MessageActivity.this).load(user.getImageURL()).into(profileIv);
        }
        status.setText(user.getStatus());
    }

    @Override
    public void setRecyclerView(List<Message> chats) {
        messageAdapter = new MessageAdapter(this,chats,user.getImageURL());
        recyclerView.setAdapter(messageAdapter);
    }

    @Override
    public void notifyAdapter(){
        messageAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(messageAdapter.getItemCount()-1 );
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagePresenter.attach(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        messagePresenter.deAttach();
    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messagePresenter.changeStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        messagePresenter.changeStatus("offline");
        messagePresenter.removeSeenListener();
    }
}