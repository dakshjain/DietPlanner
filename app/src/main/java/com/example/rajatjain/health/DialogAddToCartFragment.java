package com.example.rajatjain.health;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class DialogAddToCartFragment extends DialogFragment {

    private TextView text1,text2,text3,text4;
    private FirebaseAuth firebaseAuth;
    int index;
    int position;
    String days ;
    private Firebase mRef;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_add_to_cart, new LinearLayout(getActivity()), false);

        // Retrieve layout elements
        //TextView title = (TextView) view.findViewById(R.id.text_title);

        // Set values
        //title.setText("Not perfect yet");

        // Build dialog
        firebaseAuth = FirebaseAuth.getInstance();
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        builder.setContentView(view);
        text1 = (TextView)view.findViewById(R.id.dialog1);
        text2 = (TextView)view.findViewById(R.id.dialog2);
        text3 = (TextView)view.findViewById(R.id.dialog3);
        text4 = (TextView)view.findViewById(R.id.dialog4);


        Firebase.setAndroidContext(getContext());
        mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/" + firebaseAuth.getCurrentUser().getUid()+"/weeks"+index+"/"+days);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> lst = new ArrayList<String>(); // Result will be holded Here
                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    lst.add(String.valueOf(dsp.getValue())); //add result into array list

                }
                /*text1.setText(lst.get(0));
                text2.setText(lst.get(1));
                text3.setText(lst.get(2));
                text4.setText(lst.get(3));*/
                Toast.makeText(getContext(), "index"+index+"position"+position+"day:"+days, Toast.LENGTH_SHORT).show();

                //Toast.makeText(getContext(), ""+lst.get(0), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



        return builder;

    }
    public void getdetails(int index, int position,String days){
        this.index = index;
        this.position = position;
        this.days = days;
    }
    public String getday(int i){
        String weekdays[] = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        switch (i){
            case 0 :
                return weekdays[5];
            case 1:
                return weekdays[1];
            case 2:
                return weekdays[6];
            case 3:
                return weekdays[0];
            case 4:
                return weekdays[4];
            case 5:
                return weekdays[2];
            case 6:
                return weekdays[3];
            default:
                return weekdays[1];
        }
    }
    public void setbindday(String ddays){

        days = ddays;
    }

}