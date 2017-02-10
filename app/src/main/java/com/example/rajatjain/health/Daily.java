package com.example.rajatjain.health;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by rajatjain on 07/01/17.
 */

public class Daily extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button nextbutton ;
    private Button previousbutton;
    private TextView weektitle;
    int index=1;
    int getdataloop=0;
    int count =0;
    int weekcount;
    String value;
    private ProgressDialog progressbar;
    private static String LOG_TAG = "CardViewActivity";
    private FirebaseAuth firebaseAuth;
    private Firebase mRef;
    ArrayList results;
    ArrayList User  = new ArrayList<>();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.daily, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter1(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        weektitle =(TextView)v.findViewById(R.id.daystitle);
        weektitle.setText("Day1 ");
        mRecyclerView.setVisibility(View.GONE);
        /*if(!isNetworkAvailable()){
            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
            progressbar.dismiss();
        }*/
        previousbutton = (Button)v.findViewById(R.id.previousbutton);
        nextbutton  = (Button)v.findViewById(R.id.nextbutton);
        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settitle(v.getId());
            }
        });
        previousbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settitle(v.getId());
            }
        });
        
        return v;
    }
    private void settitle(int id) {


        weektitle =(TextView)getView().findViewById(R.id.daystitle);
        if (id==R.id.nextbutton&&index<30&&index>0){
            index++;

            mAdapter=new MyRecyclerViewAdapter1(getDataSet());

            mRecyclerView.setAdapter(mAdapter);

            weektitle.setText("Day"+index);

        }
        else if(id==R.id.previousbutton&&index<=30&&index>1){
            index--;

            mAdapter = new MyRecyclerViewAdapter1(getDataSet());

            mRecyclerView.setAdapter(mAdapter);
            weektitle.setText("Day"+index);

        }


    }
    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter1) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter1
                .MyClickListener() {

            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject1> getDataSet() {

        results = new ArrayList<DataObject1>();

        final Firebase mRefChild;
        firebaseAuth = FirebaseAuth.getInstance();

        Firebase.setAndroidContext(getContext());
        try {
            Calendar c = Calendar.getInstance();
            Calendar c2= Calendar.getInstance();
            c2.add(Calendar.DATE,index-1);
            int day= c2.get(Calendar.DAY_OF_WEEK);
            String day_string = getday(day);
            int exactweeks = (c2.get(Calendar.WEEK_OF_YEAR)-c.get(Calendar.WEEK_OF_YEAR))+1;
            //Toast.makeText(getContext(), ""+exactweeks+""+day_string, Toast.LENGTH_SHORT).show();
            mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/" + firebaseAuth.getCurrentUser().getUid()+"/weeks"+exactweeks+"/"+day_string);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<String> lst = new ArrayList<String>(); // Result will be holded Here
                    for(DataSnapshot dsp : dataSnapshot.getChildren()){
                        lst.add(String.valueOf(dsp.getValue())); //add result into array list

                    }
                    Toast.makeText(getContext(), ""+lst.get(0), Toast.LENGTH_SHORT).show();
                    DataObject1 obj = new DataObject1(""+lst.get(0) ,""+lst.get(1),""+lst.get(2),""+lst.get(3));
                    results.add(obj);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    mRecyclerView.setVisibility(getView().VISIBLE);

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


















            /*if(mRef==null){
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }*/

            /*mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    value = dataSnapshot.getValue(String.class);
                   *//* for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        User.add( i, postSnapshot.getValue(ArrayList.class));
                        i++;
                    }*//*
                    Toast.makeText(getContext(), "heyy "+value, Toast.LENGTH_SHORT).show();

                   *//* for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        User.add(String.valueOf(dsp.getValue()));
                    }*//*

                    DataObject1 obj = new DataObject1("hgfuy","","","");
                    results.add(obj);
                    // progressbar.setVisibility(View.GONE);

                    //progressbar.dismiss();
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    mRecyclerView.setVisibility(getView().VISIBLE);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
*/
        }
        catch (NullPointerException e){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        return results;
    }

    private String getday(int day) {
        switch (day){
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return  "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Sunday";
        }


    }


}
