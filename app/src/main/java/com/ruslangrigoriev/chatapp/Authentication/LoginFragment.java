package com.ruslangrigoriev.chatapp.Authentication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.ruslangrigoriev.chatapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    public static final String TAG = "LoginFragment";

    private MaterialEditText email, password;
    private Button loginBtn;
    private TextView forgotTv;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity)getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if(activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Login");
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            }
        }
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        loginBtn = view.findViewById(R.id.btn_login);
        forgotTv = view.findViewById(R.id.forgot_password);

        loginBtn.setOnClickListener(v -> {
            String txt_email = email.getText().toString();
            String txt_password = password.getText().toString();

            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                ((StartActivity)getActivity()).authPresenter.onClickLogin(txt_email,txt_password);
            }
        });

        forgotTv.setOnClickListener(v -> {
            StartActivity startActivity = (StartActivity) getActivity();
            if (startActivity != null) {
                startActivity.setFragmentStack(ResetFragment.newInstance());
            };
        });
    }
}