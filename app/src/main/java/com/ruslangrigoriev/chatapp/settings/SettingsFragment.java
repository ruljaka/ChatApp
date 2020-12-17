package com.ruslangrigoriev.chatapp.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.messaging.view.MessageActivity;


public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";

    private RelativeLayout profileLayout;
    private ImageView profileImage;
    private TextView username;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {

        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        profileLayout = view.findViewById(R.id.profile_layout);
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity() != null) {
                    ((SettingsActivity) getActivity()).onProfileClick();
                }
            }
        });
        profileImage = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        if(getActivity() != null) {
            ((SettingsActivity) getActivity()).onSettingsFragmentCreate();
        }
        return view;
    }

    public void setUserInfo(User user) {
        username.setText(user.getUsername());
        if(user.getImageURL().equals("default")){
            profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            if(getActivity() != null) {
                Glide.with(getActivity()).load(user.getImageURL()).into(profileImage);
            }

        }
    }
}