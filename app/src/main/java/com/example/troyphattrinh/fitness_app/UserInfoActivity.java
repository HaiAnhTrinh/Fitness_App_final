package com.example.troyphattrinh.fitness_app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInfoActivity extends Fragment {

    DatabaseHelper dbh;
    private String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        email = this.getArguments().getString("EMAIL_KEY").toString();

        return inflater.inflate(R.layout.activity_user_info, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ListView listView = view.findViewById(R.id.infoListView);
        dbh = new DatabaseHelper(view.getContext());

        //populate an ArrayList<String> from the database and then view it

        ArrayList<String> theList = new ArrayList<>();
        Cursor data = dbh.getUser(email);
        if (data.getCount() == 0) {
            Toast.makeText(view.getContext(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(0));
                theList.add(data.getString(1));
                theList.add(data.getString(2));
                ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
            }


        }

    }
}
