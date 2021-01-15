package com.ruslangrigoriev.chatapp.Authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ruslangrigoriev.chatapp.R;


public class RegisterFragment extends Fragment {
    public static final String TAG = "RegisterFragment";

    private MaterialEditText username, email, password;
    private Button registerBtn;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if(activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Register");
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            }
        }
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        registerBtn = view.findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(v -> {
            String txt_username = username.getText().toString();
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else if (txt_password.length() < 6) {
                Toast.makeText(getContext(), "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else {
                ((StartActivity)getActivity()).authPresenter.onClickRegister(txt_username,txt_email,txt_password);
            }
        });
    }

}