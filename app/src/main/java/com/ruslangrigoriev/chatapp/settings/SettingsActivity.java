package com.ruslangrigoriev.chatapp.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

public class SettingsActivity extends AppCompatActivity implements SettingsActivityContract.View {

    public SettingsPresenter settingsPresenter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsPresenter = new SettingsPresenter();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        setFragment(SettingsFragment.newInstance(),"Settings");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, SettingsFragment.newInstance(), SettingsFragment.newInstance().getTag())
                .commit();
        getSupportActionBar().setTitle("Settings");


    }

    public void setFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onProfileClick() {
        setFragment(ProfileFragment.newInstance(),"Profile");
    }

    public void onSettingsFragmentCreate() {
        settingsPresenter.getCurrentUser();
    }

    public void setSettingsFragmentData(User user) {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Fragment fragment = fragments.get(fragments.size() - 1);
        if (fragment != null) {
            ((SettingsFragment) fragment).setUserInfo(user);
        }
    }



    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        settingsPresenter.attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingsPresenter.deAttach();
    }
}