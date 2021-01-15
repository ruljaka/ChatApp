package com.ruslangrigoriev.chatapp.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ruslangrigoriev.chatapp.Authentication.StartActivity;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.contacts.ContactsActivity;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.settings.SettingsActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MyTag";

    public MainPresenter mainPresenter;
    private UserAdapter chatsAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FloatingActionButton openContactsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter();


        progressBar = findViewById(R.id.progress_bar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.main_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        openContactsBtn = findViewById(R.id.openContactsBtn);
        openContactsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ContactsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.attach(this);
        if (mainPresenter.checkSignedIn()) {
            mainPresenter.getChats();
        }
    }

    @Override
    public void setChatsRV(List<User> userList) {
        chatsAdapter = new UserAdapter(this, userList, true);
        recyclerView.setAdapter(chatsAdapter);
    }

    @Override
    public void notifyChatsAdapter() {
        chatsAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    /*private void initViewPager() {
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager2 viewPager2 = findViewById(R.id.viewpager);
        viewPager2.setAdapter(new MainPagerAdapter(this));
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Chats");
                    //---------------------------------------//
                    //TODO refactor
                    BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                    badgeDrawable.setBackgroundColor(
                            ContextCompat.getColor(this, R.color.design_default_color_secondary_variant)
                    );
                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(100);
                    badgeDrawable.setMaxCharacterCount(3);
                    //---------------------------------------//
                    break;
                case 1:
                    tab.setText("Contacts");
                    break;
            }
        });
        tabLayoutMediator.attach();
        *//*viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
            }
        });*//*
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                mainPresenter.onLogout();
                return true;
            case R.id.settings:
                mainPresenter.onSettings();
                return true;
        }
        return false;
    }

    public void startStartActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
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
    protected void onResume() {
        super.onResume();
        mainPresenter.changeStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.changeStatus("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.deAttach();
        Log.d(TAG, "onDestroy MainActivity ");
    }
}