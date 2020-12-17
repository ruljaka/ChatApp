package com.ruslangrigoriev.chatapp.main.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruslangrigoriev.chatapp.R;
import com.ruslangrigoriev.chatapp.main.view.MainActivity;

public class ContactsFragment extends Fragment {
    public static final String TAG = "ContactsFragment";

    public RecyclerView recyclerView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if(getActivity() != null) {
            ((MainActivity) getActivity()).onContactsFragmentCreate();
        }
        return view;
    }
}