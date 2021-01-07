package com.ruslangrigoriev.chatapp.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {

    public static final int IMAGE_REQUEST = 1;

    private Uri imageUri;

    private CircleImageView profileImage;
    private TextView username;

    //private User user;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getActivity() != null) {
            ((SettingsActivity) getActivity()).settingsPresenter.getCurrentUser();
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (getActivity() != null) {
            ((SettingsActivity) getActivity()).setTitle("Profile");
        }
        username = view.findViewById(R.id.username);
        profileImage = view.findViewById(R.id.profile_image);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });

        if (getActivity() != null) {
            ((SettingsActivity) getActivity()).settingsPresenter.getCurrentUser();
        }

        /*if(user != null) {
            setUserInfo(user);
        }*/

        return view;
    }

    public void setUserInfo(User user) {
        username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            if (getActivity() != null) {
                Glide.with(getActivity()).load(user.getImageURL()).into(profileImage);
            }
        }
    }

    /*public void setUser(User user){
        this.user = user;
        setUserInfo(user);
    }*/

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == IMAGE_REQUEST) && (resultCode == RESULT_OK)
                && (data != null) && (data.getData() != null)) {
            imageUri = data.getData();
            ((SettingsActivity) getActivity()).settingsPresenter.uploadImage(imageUri);
            Toast.makeText(getContext(), "Uploading image", Toast.LENGTH_SHORT).show();
        }
    }
}