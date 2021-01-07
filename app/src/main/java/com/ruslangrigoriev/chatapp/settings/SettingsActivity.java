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

    private User currentUser;

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
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, new SettingsFragment(), null)
                .commit();


    }


    @Override
    public void setFragmentData(User user) {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Fragment currentFragment = fragments.get(fragments.size() - 1);
        if (currentFragment instanceof SettingsFragment) {
            ((SettingsFragment) currentFragment).setUserInfo(user);
        } else if (currentFragment instanceof ProfileFragment) {
            ((ProfileFragment) currentFragment).setUserInfo(user);
        }

    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_frame, fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    public void onProfileClick() {
        setFragment(new ProfileFragment());
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
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

    @Override
    protected void onResume() {
        super.onResume();
        settingsPresenter.changeStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        settingsPresenter.changeStatus("offline");
    }

}