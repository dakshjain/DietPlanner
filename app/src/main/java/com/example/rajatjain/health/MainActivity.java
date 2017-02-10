package com.example.rajatjain.health;

import android.app.ProgressDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.Chronology;
import org.joda.time.LocalDate;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.icu.text.RelativeDateTimeFormatter.RelativeUnit.WEEKS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //defining view objects
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignup;

    private Firebase mRef,mRef1,mRef2,mRef3,mRef5;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;
    int index=0;
    String weekdays[] = new String[]{"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
    /*String weekdays[] = new String[]{"1","2","3","4","5","6","7"};*/
    String time[] = new String[]{"8:00","1:00","5:00","10:00"};
    //defining firebaseauth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        /*if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }*/

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
        buttonSignup.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
    }

    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        //if the email and password are not empty
        //displaying a progress dialog

        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            Firebase.setAndroidContext(getApplication().getApplicationContext());
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();

                            Toast.makeText(MainActivity.this, "verification email sent", Toast.LENGTH_SHORT).show();
                            //DateFormat format = new SimpleDateFormat("MM/dd/yyyy");

                             Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");
                            String date = sdf1.format(new Date());
                           /* String onemonth;
                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yy");*/
                            Calendar c = Calendar.getInstance();
                            Calendar c2= Calendar.getInstance();
                            c2.add(Calendar.DATE,30);
                            /*try {
                                c.setTime(sdf2.parse(date));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //c.add(Calendar.DATE, 30);  // number of days to add
                            onemonth = sdf2.format(c.getTime());
                            LocalDate localDate = new LocalDate();
                            LocalDate enddate = localDate.plusDays(30);*/
                            //int weeks = Weeks.weeksBetween(localDate, enddate).getWeeks();
                             //15/10/2013



                            int exactweeks = (c2.get(Calendar.WEEK_OF_YEAR)-c.get(Calendar.WEEK_OF_YEAR))+1;


                            //String month = new SimpleDateFormat("MMM").format(cal.getTime());
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                            Date d = new Date();
                            String dayOfTheWeek = sdf.format(d);
                            for(int i=0;i<7;i++){
                                if(weekdays[i].equals(dayOfTheWeek)){
                                    index=i;
                                }else{}
                            }


                            //mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/weeks");
                            //Firebase mRefChild = mRef.child(email);
                            mRef1 = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/profile");

                            Firebase mRefChild,mRefchild1,mRefChild2,mRefChild3,mRefChild4;
                            mRefChild=mRef1.child("name");
                            mRefChild2=mRef1.child("weeks count");
                            mRefchild1=mRef1.child("date registered");
                            mRefchild1.setValue(date);
                            mRefChild2.setValue(exactweeks);
                            mRefChild.setValue(firebaseAuth.getCurrentUser().getEmail());
                            //mRefChild.setValue(email);
                           // mRef.child(firebaseAuth.getCurrentUser().getUid());
                            int countdays=0;
                            int count = 1 ;
                            outerloop:
                            for(int j=1;j<=exactweeks;j++) {
                                mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/weeks"+count);
                                mRef5 = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/weeklydata"+"/weeks"+count);
                                for (int i = index; i < 7; i++) {
                                    countdays++;
                                        if(countdays==31){
                                            break outerloop;
                                        }
                                        mRef3=  new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/weeks"+count+"/"+weekdays[index]);
                                        /*mRef2 = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid()+"/weeklydata"+"/weeks"+count);*/
                                        mRefChild4 = mRef5.child(weekdays[index]);
                                        mRefChild4.setValue("yes");
                                        for(int k=0;k<4;k++) {
                                            mRefChild3 = mRef3.child(time[k]);
                                            mRefChild3.setValue("burger");
                                        }
                                        index++;


                                }
                                count++;
                                index=0;
                            }
                            //Firebase mRefChild = mRef.child(editTextEmail.getText().toString());

                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }else{
                            //display some message here
                            Toast.makeText(MainActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onClick(View view) {

        if(view == buttonSignup){
            registerUser();
        }

        if(view == textViewSignin){
            //open login activity when user taps on the already registered textview
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
