package profile;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import Cars.CarsManagerActivity;
import chat.ChatActivity;
import chat.ChatListActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import login.LoginActivity;
import users.BubbleTransformation;
import users.FindDrivers;
import users.StaticVariable;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class MapManagerActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private Marker currentFriendLocationMarker;
    private LatLng latLngUser;
    private Target mTarget;
    private Thread thread;
    private FindDrivers driver = new FindDrivers();



    private double latitide, longitude;
    private double[] latLngDouble = new double[2];
    private static final int Request_User_Location_Code = 99;


    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private DatabaseReference myRef;
    private DatabaseReference reference;

    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgresDiaolog;
    FirebaseUser user;
    private String userID;
    private ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private ArrayList<String>  chatEmployeeList;
    private ArrayList<String> DriverInformation;
    private ArrayList<String> carsList;

    private String nip;



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_manager);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("");

        driversIdList=new ArrayList<>();
        chatEmployeeList=new ArrayList<>();
        DriverInformation=new ArrayList<>();
        carsList=new ArrayList<>();

        init();
        loadUserInfo();

        user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

        mProgresDiaolog = new ProgressDialog(this);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();

        }

        updateLocationFriendsOnMap();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent course = new Intent(MapManagerActivity.this, CoursesManagerActivity.class);
                        course.putExtra(NIP_INFORMATION,nip);
                        course.putExtra(DRIVERS_ID_LIST,driversIdList);
                        course.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        course.putExtra(CARS_ID_LIST,carsList);
                        startActivity(course);
                        return true;
                    case R.id.navigation_dashboard:
                        break;
                    case R.id.navigation_notifications:
                        Intent chat = new Intent(MapManagerActivity.this, ChatListActivity.class);
                        chat.putExtra(USER_INFORMATION, UserInformation);
                        chat.putExtra(DRIVERS_ID_LIST,driversIdList);
                        chat.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        chat.putExtra(CARS_ID_LIST,carsList);
                        startActivity(chat);
                        return true;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(MapManagerActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        drivers.putExtra(DRIVERS_ID_LIST,driversIdList);
                        drivers.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        drivers.putExtra(CARS_ID_LIST,carsList);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(MapManagerActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                        cars.putExtra(DRIVERS_ID_LIST,driversIdList);
                        cars.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        cars.putExtra(CARS_ID_LIST,carsList);
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
            Intent intent = new Intent(MapManagerActivity.this, EditProfilInformationActivity.class);
            intent.putExtra(USER_INFORMATION, UserInformation);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.main_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MapManagerActivity.this, LoginActivity.class);
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
        chatEmployeeList =(ArrayList<String>)getIntent().getSerializableExtra(CHAT_EMPLOYEE_ID_LIST);
        carsList =(ArrayList<String>)getIntent().getSerializableExtra(CARS_ID_LIST);


        nip=UserInformation.get(0);
        first_nameTextView.setText(UserInformation.get(1));
        last_nameTextView.setText(UserInformation.get(2));
        Picasso.with(this).load(UserInformation.get(3)).into(profilURL);

        reference = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee");

    }


    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void updateLocationFriendsOnMap() {
        thread = new Thread() {


            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(10000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mMap.clear();
                                for (int i = 0; i < driversIdList.size(); i++) {
                                    setLocationFriendsInMap(driversIdList.get(i));
                                }
                            }
                        });
                    }

                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    String id = marker.getSnippet();

                    showActionsDialog(id);
                }
            });

    }


    public boolean checkUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    toastMessage("Permission denied");
                }
                return;
        }
    }


    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        setUserPositionInMap(location);
    }

    private void setUserPositionInMap(Location location) {
        latitide = location.getLatitude();
        longitude = location.getLongitude();
        lastLocation = location;
        
        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }
        latLngUser = new LatLng(location.getLatitude(), location.getLongitude());

        shareLocation();



        for (int i = 0; i < driversIdList.size(); i++) {
            if(driversIdList.get(i).equals(userID)){
                driversIdList.remove(i);
            }
            setLocationFriendsInMap(driversIdList.get(i));

        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLngUser);
        markerOptions.title("Obecna pozycja");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentUserLocationMarker = mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLngUser, 12);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngUser));
        mMap.animateCamera(cameraUpdate);

        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    private void shareLocation() {
        reference.child(user.getUid()).child("is_sharing").setValue("true");
        reference.child(user.getUid()).child("lat").setValue(String.valueOf(latitide));
        reference.child(user.getUid()).child("lng").setValue(String.valueOf(longitude))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(MapManagerActivity.this, "Błąd udostepnienia lokalizacji", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void setLocationFriendsInMap(String userID) {
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/"+userID);
        readData(new FirebaseCallback() {

            @Override
            public void onCallback(double[] d, final FindDrivers driver) {
                if (d.length > 1) {
                    final LatLng latLng = new LatLng(d[0], d[1]);
                    setTargetOnImage(latLng, driver);
                }

            }
        });
    }

    private void readData(final FirebaseCallback firebaseCallback) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("lat").exists() && dataSnapshot.child("lng").exists()) {
                    setLatAndLngToDoubleTable(dataSnapshot);
                    driver = setDriverData(dataSnapshot);
                    firebaseCallback.onCallback(latLngDouble, driver);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        reference.addValueEventListener(valueEventListener);

    }

    private void setLatAndLngToDoubleTable(DataSnapshot dataSnapshot) {
        String lat = dataSnapshot.child("lat").getValue(String.class);
        String lng = dataSnapshot.child("lng").getValue(String.class);
        latLngDouble[0] = Double.parseDouble(lat);
        latLngDouble[1] = Double.parseDouble(lng);
    }

    private FindDrivers setDriverData(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey();
        String name = dataSnapshot.child("first_name").getValue(String.class);
        String lastname = dataSnapshot.child("last_name").getValue(String.class);
        String profilURl = dataSnapshot.child("profilURl").getValue(String.class);
        String sharing = dataSnapshot.child("is_sharing").getValue(String.class);

        boolean is_sharing = Boolean.parseBoolean(sharing);
        FindDrivers driver = new FindDrivers(profilURl, name,lastname,id, is_sharing);

        return driver;
    }

    private void setTargetOnImage(final LatLng latLng, final FindDrivers f) {
        boolean isSharing = true;
        isSharing = f.isIs_sharing();

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                currentFriendLocationMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng).icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title(f.getFirst_name()+" "+f.getLast_name())
                );

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("picasso", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        if (isSharing) {
            Picasso.with(MapManagerActivity.this)
                    .load(f.getProfilURl())
                    .resize(249, 250)
                    .centerCrop()
                    .transform(new BubbleTransformation(5, Color.GREEN))
                    .into(mTarget);

        } else {
            Picasso.with(MapManagerActivity.this)
                    .load(f.getProfilURl())
                    .resize(248, 250)
                    .centerCrop()
                    .transform(new BubbleTransformation(5, Color.RED))
                    .into(mTarget);
        }
    }

    private interface FirebaseCallback {
        void onCallback(double[] d, FindDrivers driver);
    }


    private void showActionsDialog(final String id) {


            CharSequence colors[] = new CharSequence[]{"Wyświetl profil", "Rozpocznij czat"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setItems(colors, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        //  Toast.makeText(MapsActivity.this, id, Toast.LENGTH_SHORT).show();
                        showProfil(id);
                    } else {
                        showChat(id);

                    }
                }
            });
            builder.show();


    }


    private void showProfil(String id) {
        myRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/"+ id);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DriverInformation(dataSnapshot);
                Intent intent = new Intent(MapManagerActivity.this, ProfileActivity.class);
                intent.putExtra(StaticVariable.DRIVER_INFORMATION, DriverInformation);
                intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showChat(String id) {
        Intent intent = new Intent(MapManagerActivity.this, ChatActivity.class);
        intent.putExtra(StaticVariable.KEY_CHAT, id);
        intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
        startActivity(intent);
    }

    public void DriverInformation(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.getKey();
        String name = dataSnapshot.child("first_name").getValue(String.class);
        String lastname = dataSnapshot.child("last_name").getValue(String.class);
        String phone = dataSnapshot.child("phone").getValue(String.class);
        String profilURl = dataSnapshot.child("profilURl").getValue(String.class);
        String fullname= name +" " +lastname;
        DriverInformation.add(id);
        DriverInformation.add(fullname);
        DriverInformation.add(phone);
        DriverInformation.add(profilURl);

    }


    @Override
    protected void onDestroy() {

        myRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee");
        myRef.child(user.getUid()).child("is_sharing").setValue("false")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MapManagerActivity.this, "Udostepnianie lokalizacji zostało wstrzymane", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MapManagerActivity.this, "Udostepnianie lokalizacji nie zostało zatrzymane", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        super.onDestroy();
    }

}


