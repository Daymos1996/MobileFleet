package chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import drivers.DriversRecyclerViewAdapter;
import login.LoginActivity;
import profile.CarsManagerActivity;
import profile.CoursesManagerActivity;
import profile.EditProfilInformationActivity;
import profile.MapManagerActivity;

import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.POSITION_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class ChatListActivity extends AppCompatActivity {

    private static final String TAG = "ViewDatabase";
    public static final int PICK_IMAGE = 1;


    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference myRef;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgresDiaolog;
    FirebaseUser user;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;



    private String userID;
    private ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private ArrayList<String> chatEmployeeList;
    private String nip;



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        driversIdList = new ArrayList<>();
        chatEmployeeList=new ArrayList<>();

        init();
        loadUserInfo();


        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mProgresDiaolog = new ProgressDialog(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent course = new Intent(ChatListActivity.this, CoursesManagerActivity.class);
                        course.putExtra(NIP_INFORMATION,nip);
                        course.putExtra(DRIVERS_ID_LIST,driversIdList);
                        course.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(course);
                        return true;
                    case R.id.navigation_dashboard:
                        Intent map = new Intent(ChatListActivity.this, MapManagerActivity.class);
                        map.putExtra(USER_INFORMATION, UserInformation);
                        map.putExtra(DRIVERS_ID_LIST,driversIdList);
                        map.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(map);
                        return true;
                    case R.id.navigation_notifications:
                        break;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(ChatListActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        drivers.putExtra(DRIVERS_ID_LIST,driversIdList);
                        drivers.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(ChatListActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                        cars.putExtra(DRIVERS_ID_LIST,driversIdList);
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
            Intent intent = new Intent(ChatListActivity.this, EditProfilInformationActivity.class);
            intent.putExtra(USER_INFORMATION, UserInformation);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.main_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(ChatListActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        return true;
    }



    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();


        first_nameTextView = findViewById(R.id.txtFirstName);
        last_nameTextView = findViewById(R.id.txtLastName);
        profilURL = findViewById(R.id.avatar);

    }
    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getIntent().getSerializableExtra(USER_INFORMATION);
        driversIdList =(ArrayList<String>)getIntent().getSerializableExtra(DRIVERS_ID_LIST);
        chatEmployeeList=(ArrayList<String>)getIntent().getSerializableExtra(CHAT_EMPLOYEE_ID_LIST);

        nip=UserInformation.get(0);
        first_nameTextView.setText(UserInformation.get(1));
        last_nameTextView.setText(UserInformation.get(2));
        Picasso.with(this).load(UserInformation.get(3)).into(profilURL);
    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}

