package profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import chat.ChatActivity;
import chat.ChatListActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import login.LoginActivity;
import users.StaticVariable;
import users.employee;

import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
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



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    public ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private ArrayList<String> chatEmployeeList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses_manager);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        UserInformation = new ArrayList<>();


        init();

        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mProgresDiaolog = new ProgressDialog(this);


        driversIdList = new ArrayList<>();
        chatEmployeeList = new ArrayList<>();
        employeeIdFromDatabase();
        chatEmployeeIdFromDatabase();




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
                        map.putExtra(DRIVERS_ID_LIST, driversIdList);
                        map.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(map);
                        return true;
                    case R.id.navigation_notifications:
                        Intent chat = new Intent(CoursesManagerActivity.this, ChatListActivity.class);
                        chat.putExtra(USER_INFORMATION, UserInformation);
                        chat.putExtra(DRIVERS_ID_LIST, driversIdList);
                        chat.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(chat);
                        return true;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(CoursesManagerActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        drivers.putExtra(DRIVERS_ID_LIST, driversIdList);
                        drivers.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(CoursesManagerActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                        cars.putExtra(DRIVERS_ID_LIST, driversIdList);
                        cars.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(cars);
                        return true;

                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




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

    /*
    public void deleteUser(String userID) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userData = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);

        userData.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });


        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });

    }
*/
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


        //display all the information
        Log.d(TAG, "showData: name: " + uInfo.getFirst_name());
        Log.d(TAG, "showData: name: " + uInfo.getLast_name());
        Log.d(TAG, "showData: profile: " + uInfo.getProfilURl());



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

    private void setDriversList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!ds.getKey().equals(userID)) {
                driversIdList.add(ds.getKey());
            }

        }
    }

    private void employeeIdFromDatabase() {
        DatabaseReference allEmployeeDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/");
        allEmployeeDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setDriversList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

    private void setChatEmployeeListList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            chatEmployeeList.add(ds.getKey());
        }
    }
    private void chatEmployeeIdFromDatabase() {
        DatabaseReference allEmployeeDatabaseRef = FirebaseDatabase.getInstance().getReference(nip+"/Employee/"+userID+"/chat");
        allEmployeeDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setChatEmployeeListList(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
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
