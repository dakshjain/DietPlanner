package com.example.rajatjain.health;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Weekly extends Fragment implements MyRecyclerViewAdapter.MyClickListener {
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
    String setday;
    private static Context context;
    private ProgressDialog progressbar;
    private static String LOG_TAG = "CardViewActivity";
    private FirebaseAuth firebaseAuth;
    private Firebase mRef;
    private static PopupWindow mPopupWindow;
    private MyRecyclerViewAdapter.MyClickListener listener;
    ArrayList results;
    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        Weekly.context = getContext();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.weekly, container, false);
        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        listener=this;
        mAdapter = new MyRecyclerViewAdapter(getDataSet(),listener);
        progressbar = new ProgressDialog(getActivity());
        progressbar.setMessage("Loading");
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.circular_progress));
        progressbar.setIndeterminate(true);
        progressbar.show();
        mRecyclerView.setAdapter(mAdapter);
        /*if(progressbar !=null)
        {
            progressbar = null;
        }*/


        //progressbar.setCanceledOnTouchOutside(false);

        //progressbar.setVisibility(View.VISIBLE);
        weektitle =(TextView)v.findViewById(R.id.weektitle);
        weektitle.setText("Week1 ");
        //mRecyclerView.setVisibility(View.GONE);
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
                mAdapter.notifyDataSetChanged();


            }
        });
        previousbutton.setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                settitle(v.getId());
                mAdapter.notifyDataSetChanged();


            }
        });
        String value;

        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }



    private void settitle(int id) {
        mAdapter.notifyDataSetChanged();
        final Firebase mRefChild;
        firebaseAuth = FirebaseAuth.getInstance();

        Firebase.setAndroidContext(getContext());
        mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/" + firebaseAuth.getCurrentUser().getUid()+"/profile/weeks count");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                  String value1 = dataSnapshot.getValue(String.class);
                    weekcount=Integer.parseInt(value1);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        weektitle =(TextView)getView().findViewById(R.id.weektitle);
        if (id==R.id.nextbutton&&index<weekcount&&index>0){
            index++;


            mAdapter=new MyRecyclerViewAdapter(getDataSet(),listener);
            //progressbar.show();
            mRecyclerView.setAdapter(mAdapter);

            weektitle.setText("Week"+index);

        }
        else if(id==R.id.previousbutton&&index<=weekcount&&index>1){
            index--;
            //progressbar.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
            mAdapter = new MyRecyclerViewAdapter(getDataSet(),listener);
            //progressbar.show();
            mRecyclerView.setAdapter(mAdapter);
            weektitle.setText("Week"+index);

        }


    }
    /* @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressbar != null) {
            progressbar.dismiss();
            progressbar = null;
        }
    }*/
    @Override
    public void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {

            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {


        results = new ArrayList<DataObject>();

        final Firebase mRefChild;
        firebaseAuth = FirebaseAuth.getInstance();

        Firebase.setAndroidContext(getContext());
        try {

            mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/" + firebaseAuth.getCurrentUser().getUid()+"/weeklydata"+"/weeks"+index);
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    value = dataSnapshot.getValue(String.class);
                    mRef.getKey();
                    //Toast.makeText(getContext(), ""+value, Toast.LENGTH_SHORT).show();
                    DataObject obj = new DataObject("" + dataSnapshot.getKey(), "Secondary ");
                    results.add(obj);
                   // progressbar.setVisibility(View.GONE);

                    progressbar.dismiss();
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                    //mRecyclerView.setVisibility(getView().VISIBLE);

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

        }
        catch (NullPointerException e){
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        return results;
    }
    public static void onClickCalled(int position) {
        Context mContext = Weekly.context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_layout,null);
        mPopupWindow = new PopupWindow(
                customView,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);

        }

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
        //mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

        Log.i(LOG_TAG,""+position);
        // Call another acitivty here and pass some arguments to it.
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onItemClick(int position, View v) {

        DialogAddToCartFragment df= new DialogAddToCartFragment();
        df.getdetails(index,position,setday);
        df.show(getFragmentManager(), "Dialog");
        Log.i(LOG_TAG,"onItemClick");
    }
    public void setday(String day){
        setday = day ;
    }
   /* private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //do your code snippet here.
            this.finish();
        }
    };*/

}
