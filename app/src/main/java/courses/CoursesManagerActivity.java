package courses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;

import cars.CarsManagerActivity;
import chat.ChatListActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import login.LoginActivity;
import profile.EditProfilInformationActivity;
import profile.MapManagerActivity;
import users.StaticVariable;
import users.employee;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.POSITION;
import static users.StaticVariable.USER_INFORMATION;


public class CoursesManagerActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    private Toolbar mToolbar;

    public static final int PICK_IMAGE = 1;


    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgresDiaolog;
    FirebaseUser user;
    private String userID;
    private String FirstName;
    private String LastName;
    private String ProfilURL;
    private String nip;
    private String position;

    private ViewPager mViewPager;
    private CoursesSectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;


    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    public ArrayList<String> UserInformation;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_manager);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new CoursesSectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);



        UserInformation = new ArrayList<>();

        init();

        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mProgresDiaolog = new ProgressDialog(this);




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


        myRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/"+ userID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showUserInformation(dataSnapshot);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent map = new Intent(CoursesManagerActivity.this, MapManagerActivity.class);
                        map.putExtra(USER_INFORMATION, UserInformation);
                        map.putExtra(NIP_INFORMATION,nip);
                        map.putExtra(POSITION,position);
                        startActivity(map);
                        return true;
                    case R.id.navigation_notifications:
                        Intent chat = new Intent(CoursesManagerActivity.this, ChatListActivity.class);
                        chat.putExtra(USER_INFORMATION, UserInformation);
                        chat.putExtra(NIP_INFORMATION,nip);
                        chat.putExtra(POSITION,position);
                        startActivity(chat);
                        return true;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(CoursesManagerActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        drivers.putExtra(NIP_INFORMATION,nip);
                        drivers.putExtra(POSITION,position);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(CoursesManagerActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                        cars.putExtra(NIP_INFORMATION,nip);
                        cars.putExtra(POSITION,position);
                        startActivity(cars);
                        return true;

                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }

    public ArrayList<String> getMyUserInformation() {
        return UserInformation;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.changeUserInformation) {
            Intent intent = new Intent(CoursesManagerActivity.this, EditProfilInformationActivity.class);
            intent.putExtra(USER_INFORMATION, UserInformation);
            startActivity(intent);
        }

        if (item.getItemId() == R.id.main_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(CoursesManagerActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        return true;
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
        if (ds.child("phone").exists()) {
            uInfo.setPhone(ds.child("phone").getValue().toString());
        }
        if (ds.child("email").exists()) {
            uInfo.setEmail(ds.child("email").getValue().toString());
        }
        if (ds.child("position").exists()){
            uInfo.setPosition(ds.child("position").getValue().toString());
        }





        if (uInfo.getFirst_name() == null) {
            first_nameTextView.setText("Nie podano imienia");
        } else {
            FirstName=uInfo.getFirst_name();
            first_nameTextView.setText(FirstName);
            UserInformation.add(FirstName);


        }
        if (uInfo.getLast_name() == null) {
            last_nameTextView.setText("Nie podano nazwiska");
        } else {
            LastName=uInfo.getLast_name();
            last_nameTextView.setText(LastName);
            UserInformation.add(LastName);


        }
        if (uInfo.getProfilURl() != null) {
            ProfilURL=uInfo.getProfilURl();
           Picasso.with(this).load(ProfilURL).into(profilURL);
            UserInformation.add(ProfilURL);

        }
        UserInformation.add(uInfo.getEmail());
        UserInformation.add(uInfo.getPhone());
        UserInformation.add(uInfo.getPosition());

    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        first_nameTextView = findViewById(R.id.txtFirstName);
        last_nameTextView = findViewById(R.id.txtLastName);
        profilURL = findViewById(R.id.avatar);

        nip=getIntent().getStringExtra(StaticVariable.NIP_INFORMATION);
        UserInformation.add(nip);


    }







    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStart() {
        super.onStart();

        myRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/"+ userID);
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



    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
