package com.ruslangrigoriev.chatapp.main.view.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ruslangrigoriev.chatapp.main.view.fragments.ChatsFragment;
import com.ruslangrigoriev.chatapp.main.view.fragments.ContactsFragment;

import java.util.ArrayList;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return ChatsFragment.newInstance();
        }
        return ContactsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
