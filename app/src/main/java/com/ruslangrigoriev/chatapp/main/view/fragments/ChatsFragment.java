package com.ruslangrigoriev.chatapp.main.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.main.view.MainActivity;
import com.ruslangrigoriev.chatapp.main.view.adapters.UserAdapter;

import java.util.List;

public class ChatsFragment extends Fragment {
    public static final String TAG = "ChatsFragment";

    public RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> users;

    public ChatsFragment() {
        // Required empty public constructor
    }

    public static ChatsFragment newInstance() {
        return new ChatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(getActivity() != null) {
            ((MainActivity) getActivity()).onChatsFragmentCreate();
        }
        return view;
    }
}