package com.ruslangrigoriev.chatapp.Authentication.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ruslangrigoriev.chatapp.Authentication.view.StartActivity;
import com.ruslangrigoriev.chatapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartFragment extends Fragment {
    public static final String TAG = "StartFragment";

    private Button login;
    private Button register;

    public StartFragment() {
        // Required empty public constructor
    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        login = view.findViewById(R.id.login);
        register = view.findViewById(R.id.register);

        login.setOnClickListener(v -> {
            StartActivity activity = (StartActivity) getActivity();
            if (activity != null) {
                activity.setFragmentStack(LoginFragment.newInstance());
            };
        });

        register.setOnClickListener(v -> {
            StartActivity activity = (StartActivity) getActivity();
            if (activity != null) {
                activity.setFragmentStack(RegisterFragment.newInstance());
            };
        });


    }
}