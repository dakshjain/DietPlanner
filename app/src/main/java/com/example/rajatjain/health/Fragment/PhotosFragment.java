package com.example.rajatjain.health.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rajatjain.health.R;
import com.example.rajatjain.health.other.CircleTransform;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    int pressed = 0;
    int editpress = 0;
    private ImageView img;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText txt;
    private EditText edittext;
    private FirebaseAuth firebaseAuth;
    private Firebase mRef;
    private TextView previous;
    private ViewSwitcher switcher;
    private OnFragmentInteractionListener mListener;
    private TextView text;
    private Button senddata;

    public PhotosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(String param1, String param2) {
        PhotosFragment fragment = new PhotosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photos, container, false);
        Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        final Firebase mRefChild;
        img = (ImageView) v.findViewById(R.id.profile_img);
        senddata = (Button) v.findViewById(R.id.senddata);
        edittext = (EditText)v.findViewById(R.id.hidden_edit_view);
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(getContext()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);
        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        text = (TextView) v.findViewById(R.id.email);
        text.setText(user.getEmail());
        text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                switcher = (ViewSwitcher) getView().findViewById(R.id.my_switcher);
                switcher.showNext(); //or switcher.showPrevious();
                txt = (EditText) switcher.findViewById(R.id.hidden_edit_view);
                if (pressed == 0) {
                    txt.setText(user.getEmail());
                    editpress++;
                } else {
                    txt.setText(txt.getText().toString());
                    editpress++;
                }

            }
        });
        v.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                //switcher = (ViewSwitcher)getView().findViewById(R.id.my_switcher);
                if (editpress != 0) {
                    switcher.showPrevious(); //or switcher.showPrevious();
                    previous = (TextView) switcher.findViewById(R.id.email);
                    txt = (EditText) switcher.findViewById(R.id.hidden_edit_view);
                    previous.setText(txt.getText().toString());
                    pressed++;
                    editpress--;
                }


            }
        });
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        Firebase.setAndroidContext(getContext());
        mRef = new Firebase("https://health-819fc.firebaseio.com/NewUsers/"+firebaseAuth.getCurrentUser().getUid());
        mRefChild = mRef.child("email");

        switcher = (ViewSwitcher) getActivity().findViewById(R.id.my_switcher);
        txt = (EditText) getActivity().findViewById(R.id.hidden_edit_view);
        previous = (TextView) getActivity().findViewById(R.id.email);

        senddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);

                if(editpress!=0) {
                    switcher.showPrevious(); //or switcher.showPrevious();
                    previous = (TextView) switcher.findViewById(R.id.email);
                    txt = (EditText) switcher.findViewById(R.id.hidden_edit_view);
                    previous.setText(txt.getText().toString());
                    pressed++;
                    editpress--;
                    mRefChild.setValue(txt.getText().toString());
                }

                else{
                    mRefChild.setValue(text.getText().toString());
                }
                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();

            }
        });


        return v;
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
            /*Intent startNewActivityOpen = new Intent(getContext(), PhotosFragment.class);
            startActivityForResult(startNewActivityOpen, 0);*/
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
