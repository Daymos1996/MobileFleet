package chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import drivers.DriversRecyclerViewAdapter;
import login.LoginActivity;
import cars.CarsManagerActivity;
import courses.CoursesManagerActivity;
import profile.EditProfilInformationActivity;
import profile.MapManagerActivity;
import users.StaticVariable;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
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
    private DatabaseReference userDatabaseRef;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private TabLayout mTabLayout;
    public  ArrayList<String> chatFriendsIdList;



    private String userID;
    private ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private ArrayList<String> chatEmployeeList;
    private ArrayList<String> carsList;
    private String nip;


    private RecyclerView ChatEmployeeListRecyclerView;
    private ChatFriendsRecyclerViewAdapter chatFriendsRecyclerViewAdapter;


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

        //mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
       /// mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
       // mViewPager.setAdapter(mSectionsPagerAdapter);

     //   mTabLayout = (TabLayout) findViewById(R.id.main_tabs);
      //  mTabLayout.setupWithViewPager(mViewPager);


       // driversIdList = new ArrayList<>();
        //chatEmployeeList=new ArrayList<>();
        //carsList = new ArrayList<>();

        chatFriendsIdList= new ArrayList<>();

        init();
        loadUserInfo();


        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mProgresDiaolog = new ProgressDialog(this);


         chatEmployeeIdFromDatabase();

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
                   //     course.putExtra(DRIVERS_ID_LIST,driversIdList);
                   //     course.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                     //   course.putExtra(CARS_ID_LIST,carsList);
                        startActivity(course);
                        return true;
                    case R.id.navigation_dashboard:
                        Intent map = new Intent(ChatListActivity.this, MapManagerActivity.class);
                        map.putExtra(USER_INFORMATION, UserInformation);
                      //  map.putExtra(DRIVERS_ID_LIST,driversIdList);
                       // map.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                       // map.putExtra(CARS_ID_LIST,carsList);
                        map.putExtra(NIP_INFORMATION,nip);
                        startActivity(map);
                        return true;
                    case R.id.navigation_notifications:
                        break;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(ChatListActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                    //    drivers.putExtra(DRIVERS_ID_LIST,driversIdList);
                    //    drivers.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                     //   drivers.putExtra(CARS_ID_LIST,carsList);
                        drivers.putExtra(NIP_INFORMATION,nip);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(ChatListActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                     //   cars.putExtra(DRIVERS_ID_LIST,driversIdList);
                    //    cars.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                     //   cars.putExtra(CARS_ID_LIST,carsList);
                        cars.putExtra(NIP_INFORMATION,nip);
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

        ChatEmployeeListRecyclerView=findViewById(R.id.driversRecyclerView);

        first_nameTextView = findViewById(R.id.txtFirstName);
        last_nameTextView = findViewById(R.id.txtLastName);
        profilURL = findViewById(R.id.avatar);

    }
    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getIntent().getSerializableExtra(USER_INFORMATION);
       // driversIdList =(ArrayList<String>)getIntent().getSerializableExtra(DRIVERS_ID_LIST);
       // chatEmployeeList=(ArrayList<String>)getIntent().getSerializableExtra(CHAT_EMPLOYEE_ID_LIST);
       // carsList =(ArrayList<String>)getIntent().getSerializableExtra(CARS_ID_LIST);


        nip= getIntent().getStringExtra(StaticVariable.NIP_INFORMATION);
        first_nameTextView.setText(UserInformation.get(1));
        last_nameTextView.setText(UserInformation.get(2));
        Picasso.with(this).load(UserInformation.get(3)).into(profilURL);
    }

    private void setChatEmployeeListList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            chatFriendsIdList.add(ds.getKey());
        }
        RecyclerViewLoad();
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

    private void RecyclerViewLoad(){
        if(!chatFriendsIdList.isEmpty()) {
            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/");


            chatFriendsRecyclerViewAdapter = new ChatFriendsRecyclerViewAdapter(ChatListActivity.this, userDatabaseRef,chatFriendsIdList);
            ChatEmployeeListRecyclerView.setLayoutManager( new LinearLayoutManager(this));
            ChatEmployeeListRecyclerView.setHasFixedSize(true);
            ChatEmployeeListRecyclerView.setAdapter(chatFriendsRecyclerViewAdapter);
            chatFriendsRecyclerViewAdapter.notifyDataSetChanged();

            /*
            chatFriendsRecyclerViewAdapter = new ChatFriendsRecyclerViewAdapter(ChatListActivity.this, userDatabaseRef, chatFriendsIdList);
            ChatEmployeeListRecyclerView.setLayoutManager( new LinearLayoutManager(this));
            ChatEmployeeListRecyclerView.setHasFixedSize(true);
            ChatEmployeeListRecyclerView.setAdapter(chatFriendsRecyclerViewAdapter);
            chatFriendsRecyclerViewAdapter.notifyDataSetChanged();
            */
        }

    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        chatFriendsIdList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatFriendsIdList.clear();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}

