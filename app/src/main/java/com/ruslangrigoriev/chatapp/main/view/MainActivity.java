package com.ruslangrigoriev.chatapp.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ruslangrigoriev.chatapp.Authentication.view.StartActivity;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.main.MainActivityContract;
import com.ruslangrigoriev.chatapp.main.presenter.MainPresenter;
import com.ruslangrigoriev.chatapp.main.view.adapters.MainPagerAdapter;
import com.ruslangrigoriev.chatapp.main.view.adapters.UserAdapter;
import com.ruslangrigoriev.chatapp.main.view.fragments.ChatsFragment;
import com.ruslangrigoriev.chatapp.main.view.fragments.ContactsFragment;
import com.ruslangrigoriev.chatapp.settings.SettingsActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View {

    public MainPresenter mainPresenter;
    private UserAdapter usersAdapter;
    private UserAdapter chatsAdapter;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainPresenter = new MainPresenter();
        progressBar = findViewById(R.id.progress_bar);

        initToolBar();
        initViewPager();
    }

    public void onContactsFragmentCreate() {
        mainPresenter.getContactList();
    }

    public void onChatsFragmentCreate() {
        mainPresenter.getChatUsersList();
    }


    @Override
    public void setContactsRV(List<User> userList) {
        usersAdapter = new UserAdapter(this, userList);
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Fragment fragment = fragments.get(fragments.size() - 1);
        if (fragment != null) {
            ((ContactsFragment) fragment).recyclerView.setAdapter(usersAdapter);
        }
    }

    @Override
    public void notifyContactsAdapter() {
        usersAdapter.notifyDataSetChanged();
    }

    @Override
    public void setChatsRV(List<User> userList) {
        chatsAdapter = new UserAdapter(this, userList);
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Fragment fragment = fragments.get(fragments.size() - 1);
        if (fragment != null) {
            ((ChatsFragment) fragment).recyclerView.setAdapter(chatsAdapter);
        }
    }

    @Override
    public void notifyChatsAdapter() {
        chatsAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainPresenter.attach(this);
        mainPresenter.checkSignedIn();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.deAttach();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // mainPresenter.deAttach();
    }

    private void initToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
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
        /*viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                BadgeDrawable badgeDrawable = tabLayout.getTabAt(position).getOrCreateBadge();
                badgeDrawable.setVisible(false);
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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


}