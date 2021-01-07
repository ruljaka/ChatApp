package com.ruslangrigoriev.chatapp.settings;

import android.net.Uri;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.User;

public interface SettingsActivityContract {

    interface View extends IView {
        void setFragmentData(User user);
    }


    interface Presenter {
        void getCurrentUser();

        void uploadImage(Uri imageUri);
    }

}
