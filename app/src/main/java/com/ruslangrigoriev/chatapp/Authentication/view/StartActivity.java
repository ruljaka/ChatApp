package com.ruslangrigoriev.chatapp.Authentication.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ruslangrigoriev.chatapp.Authentication.StartActivityContract;
import com.ruslangrigoriev.chatapp.Authentication.presenter.AuthPresenter;
import com.ruslangrigoriev.chatapp.Authentication.view.fragments.StartFragment;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.main.view.MainActivity;

public class StartActivity extends AppCompatActivity implements StartActivityContract.View {

    public AuthPresenter authPresenter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        authPresenter = new AuthPresenter();
        progressBar = findViewById(R.id.progress_bar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authPresenter.attach(this);
        setFragment(StartFragment.newInstance());
    }

    @Override
    protected void onStop() {
        super.onStop();
        authPresenter.deAttach();
    }

    @Override
    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.start_frame, fragment, fragment.getTag())
                .commit();
    }

    @Override
    public void setFragmentStack(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.start_frame, fragment, fragment.getTag())
                .addToBackStack(null)
                .commit();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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