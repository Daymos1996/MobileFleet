package chat;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;
import drivers.DriversRecyclerViewAdapter;
import login.LoginActivity;
import users.StaticVariable;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.USER_INFORMATION;

public class EmployeesFragment extends Fragment {


    private FirebaseAuth mAuth;
    FirebaseUser user;
    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;

    private RecyclerView driverListRecyclerView;


    private DatabaseReference userDatabaseRef;
    private ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private DriversRecyclerViewAdapter driversRecyclerViewAdapter;
    private View mMainView;
    private String nip;
    private String userID;






    public EmployeesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_employees, container, false);
        driverListRecyclerView= (RecyclerView) mMainView.findViewById(R.id.driversRecyclerView);

        //driversIdList=new ArrayList<>();
        UserInformation=new ArrayList<>();
      //  carsList=new ArrayList<>();
        init();
        loadUserInfo();


        employeeIdFromDatabase();
/*
        if(driversIdList != null && (!driversIdList.isEmpty())) {
            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/");

            driversRecyclerViewAdapter = new DriversRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef, driversIdList);
            driverListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            driverListRecyclerView.setHasFixedSize(true);
            driverListRecyclerView.setAdapter(driversRecyclerViewAdapter);
            driversRecyclerViewAdapter.notifyDataSetChanged();

        }
*/
        return  mMainView;
    }

    private void init() {
        first_nameTextView = mMainView.findViewById(R.id.txtFirstName);
        last_nameTextView = mMainView.findViewById(R.id.txtLastName);
        profilURL = mMainView.findViewById(R.id.avatar);
    }

    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getActivity().getIntent().getSerializableExtra(USER_INFORMATION);

        nip= getActivity().getIntent().getStringExtra(StaticVariable.NIP_INFORMATION);


    }


    private void setDriversList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (!ds.getKey().equals(userID)) {
                driversIdList.add(ds.getKey());
            }

        }
        RecyclerViewLoad();
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

    private void RecyclerViewLoad(){
        if(!driversIdList.isEmpty()) {

            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/");


            driversRecyclerViewAdapter = new DriversRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef,driversIdList,UserInformation,nip);
            driverListRecyclerView.setLayoutManager( new LinearLayoutManager(getActivity().getBaseContext()));
            driverListRecyclerView.setHasFixedSize(true);
            driverListRecyclerView.setAdapter(driversRecyclerViewAdapter);
            driversRecyclerViewAdapter.notifyDataSetChanged();


        }

    }



    @Override
    public void onStart() {
        super.onStart();
        driversIdList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        driversIdList.clear();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        driversIdList.clear();
    }


}
