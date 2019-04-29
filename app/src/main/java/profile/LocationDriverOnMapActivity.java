package profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import login.LoginActivity;
import users.BubbleTransformation;
import users.FindDrivers;
import users.StaticVariable;
import users.employee;

import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.DRIVER_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class LocationDriverOnMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap map;
    private String name ;
    private String lat;
    private String lng;
    private String photoUrl;
    private String otherUserID;
    private String nip;

    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private Target mTarget;

    private static final String TAG = "ViewDatabase";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locetion_driver_on_map);

        init();
        loadUserInfo();

        myRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/"+ otherUserID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showUserInformation(dataSnapshot);
                loadMap();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


    }

    private void loadMap(){
        if(lat!= null && lng != null){
            SupportMapFragment mapFragment= (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
        else
            toastMessage("Brak aktualnej lokalizacji");

    }

    private void showUserInformation(DataSnapshot ds) {
        FindDrivers uInfo = new FindDrivers();
        if (ds.child("first_name").exists()) {
            uInfo.setFirst_name(ds.child("first_name").getValue().toString());
        }
        if (ds.child("last_name").exists()) {
            uInfo.setLast_name(ds.child("last_name").getValue().toString()); //set the name
        }

        if (ds.child("profilURl").exists()) {
            uInfo.setProfilURl(ds.child("profilURl").getValue().toString());
        }
        if (ds.child("lat").exists()) {
            uInfo.setLat(ds.child("lat").getValue().toString());
        }
        if (ds.child("lng").exists()) {
            uInfo.setLng(ds.child("lng").getValue().toString());
        }



        //display all the information
        Log.d(TAG, "showData: name: " + uInfo.getFirst_name());
        Log.d(TAG, "showData: name: " + uInfo.getLast_name());
        Log.d(TAG, "showData: profile: " + uInfo.getProfilURl());
        Log.d(TAG, "showData: lat: " + uInfo.getLat());
        Log.d(TAG, "showData: lng: " + uInfo.getLng());


            name=uInfo.getFirst_name()+" "+uInfo.getLast_name();
            photoUrl=uInfo.getProfilURl();
            lat=uInfo.getLat();
            lng=uInfo.getLng();

    }

    private void loadUserInfo(){
        otherUserID = getIntent().getStringExtra(StaticVariable.KEY_CHAT);
        nip = getIntent().getStringExtra(StaticVariable.NIP_INFORMATION);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng driverLocation =  new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));


            map.addMarker(new MarkerOptions().position(driverLocation).title(name));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(driverLocation, 12);
            map.moveCamera(CameraUpdateFactory.newLatLng(driverLocation));
            map.animateCamera(cameraUpdate);


    }
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
