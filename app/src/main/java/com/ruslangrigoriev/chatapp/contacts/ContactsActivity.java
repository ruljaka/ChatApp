package com.ruslangrigoriev.chatapp.contacts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.main.UserAdapter;

import java.util.List;

public class ContactsActivity extends AppCompatActivity implements ContactsActivityContract.View {

    public ContactsPresenter contactsPresenter;
    private UserAdapter usersAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private EditText searchUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        contactsPresenter = new ContactsPresenter();
        progressBar = findViewById(R.id.progress_bar);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Contacts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.contacts_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //contactsPresenter.getContactList();
        contactsPresenter.searchUsers("");

        searchUser = findViewById(R.id.search_user);
        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contactsPresenter.searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setContactsRV(List<User> userList) {
        usersAdapter = new UserAdapter(this, userList,false);
        recyclerView.setAdapter(usersAdapter);
    }


    @Override
    public void notifyContactsAdapter() {
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.invite:
                //TODO
                return true;
            case R.id.contacts:
                //TODO
                return true;
            case R.id.help:
                //TODO
                return true;
        }
        return false;
    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        contactsPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contactsPresenter.deAttach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactsPresenter.changeStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        contactsPresenter.changeStatus("offline");
    }
}