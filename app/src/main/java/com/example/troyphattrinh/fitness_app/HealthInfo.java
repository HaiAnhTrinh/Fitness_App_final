package com.example.troyphattrinh.fitness_app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class HealthInfo extends Fragment {

    DatabaseHelper dbh;
    private String email;
    ListView listView;
    Button heartRateBtn, stepsBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        email = this.getArguments().getString("EMAIL_KEY").toString();

        return inflater.inflate(R.layout.activity_health_info, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        heartRateBtn = view.findViewById(R.id.heartRateButton);
        stepsBtn = view.findViewById(R.id.stepsButton);
        listView = view.findViewById(R.id.healthListView);
        dbh = new DatabaseHelper(view.getContext());

        heartRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //populate an ArrayList<String> from the database and then view it
                ArrayList<String> theList = new ArrayList<>();
                Cursor data = dbh.getHRate(email);
                if (data.getCount() == 0) {
                    Toast.makeText(view.getContext(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                    while (data.moveToNext()) {
                        buffer.append(data.getString(0));
                        buffer.append(data.getString(1));
                        ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, theList);
                        listView.setAdapter(listAdapter);
                    }
            }
        });

        stepsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //populate an ArrayList<String> from the database and then view it
                ArrayList<String> theList = new ArrayList<>();
                Cursor data = dbh.getSteps(email);
                if (data.getCount() == 0) {
                    Toast.makeText(view.getContext(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
                } else {
                    while (data.moveToNext()) {
                        theList.add(data.getString(0));
                        theList.add(data.getString(1));
                        ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, theList);
                        listView.setAdapter(listAdapter);
                    }

                }
            }
        });

        //populate an ArrayList<String> from the database and then view it
//        ArrayList<String> theList = new ArrayList<>();
//        Cursor data = dbh.getUser(email);
//        if (data.getCount() == 0) {
//            Toast.makeText(view.getContext(), "There are no contents in this list!", Toast.LENGTH_LONG).show();
//        } else {
//            while (data.moveToNext()) {
//                theList.add(data.getString(0));
//                theList.add(data.getString(1));
//                theList.add(data.getString(2));
//                ListAdapter listAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, theList);
//                listView.setAdapter(listAdapter);
//            }
//
//
//        }

    }

}
