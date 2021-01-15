package com.ruslangrigoriev.chatapp.Authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.ruslangrigoriev.chatapp.R;

public class ResetFragment extends Fragment {
    public static final String TAG = "ResetFragment";

    private EditText sendEmailET;
    private Button resetBtn;

    public ResetFragment() {
        // Required empty public constructor
    }

    public static ResetFragment newInstance() {

        return new ResetFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle("Reset password");
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
            }
        }

        sendEmailET = view.findViewById(R.id.send_email);
        resetBtn = view.findViewById(R.id.btn_reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = sendEmailET.getText().toString();

                if(email.equals("")){
                    Toast.makeText(getContext(), "All fields are required!", Toast.LENGTH_SHORT).show();
                } else {
                    StartActivity startActivity = (StartActivity) getActivity();
                    if (startActivity != null) {
                        startActivity.authPresenter.onClickReset(email);
                        startActivity.onBackPressed();
                    };
                }
            }
        });
    }
}