package com.ruslangrigoriev.chatapp.Authentication;

import androidx.fragment.app.Fragment;

import com.ruslangrigoriev.chatapp.base.IView;

public interface StartActivityContract {
    interface View extends IView {

        void setFragment(Fragment fragment);

        void setFragmentStack(Fragment fragment);

        void makeToast(String message);

        void startMainActivity();
    }

    interface Presenter {

        void onClickRegister(String username, String email, String password);

        void onClickLogin(String email, String password);
    }
}
