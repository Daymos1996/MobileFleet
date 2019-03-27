package profile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.employee;
import users.manager;

public class ProfileManagerActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    private String userID;


    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        init();

        user = mAuth.getCurrentUser();
        userID = user.getUid();

        /*
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    toastMessage("Successfully signed in with: " + user.getEmail());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    toastMessage("Successfully signed out.");
                }
            }
        };
        */


        myRef = FirebaseDatabase.getInstance().getReference().child("333444555/"+"Employee/"+userID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showUserInformation(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void showUserInformation(DataSnapshot ds) {
        employee uInfo = new employee();
        if (ds.child("first_name").exists()) {
            uInfo.setFirst_name(ds.child("first_name").getValue().toString());
        }
        if (ds.child("last_name").exists()) {
            uInfo.setLast_name(ds.child("last_name").getValue().toString()); //set the name
        }

        if (ds.child("profilURl").exists()) {
            uInfo.setProfilURl(ds.child("profilURl").getValue().toString());
        }



        //display all the information
        Log.d(TAG, "showData: name: " + uInfo.getFirst_name());
        Log.d(TAG, "showData: name: " + uInfo.getLast_name());
        Log.d(TAG, "showData: profile: " + uInfo.getProfilURl());



        if (uInfo.getFirst_name() == null) {
            first_nameTextView.setText("Nie podano imienia");
        } else {
            first_nameTextView.setText(uInfo.getFirst_name());
        }
        if (uInfo.getLast_name() == null) {
            last_nameTextView.setText("Nie podano nazwiska");
        } else {
            last_nameTextView.setText(uInfo.getLast_name());
        }
        if (uInfo.getProfilURl() != null) {
            Picasso.with(this).load(uInfo.getProfilURl()).into(profilURL);

        }
    }

    private void init() {
        reference= FirebaseDatabase.getInstance().getReference().child("Manager");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        first_nameTextView = findViewById(R.id.txtFirstName);
        last_nameTextView = findViewById(R.id.txtLastName);
        profilURL = findViewById(R.id.avatar);

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
