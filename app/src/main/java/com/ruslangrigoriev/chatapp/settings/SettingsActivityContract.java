package com.ruslangrigoriev.chatapp.settings;

import androidx.fragment.app.Fragment;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.User;

public interface SettingsActivityContract {

    interface View extends IView {

        void setSettingsFragmentData(User user);

        void onProfileClick();
    }


    interface Presenter {
        void getCurrentUser();

    }

}
