package courses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Cars.CarAddActivity;
import Cars.CarsManagerActivity;
import at.markushi.ui.CircleButton;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversRecyclerViewAdapter;
import users.StaticVariable;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.USER_INFORMATION;


public class TodayCoursesFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser user;
    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;

    private RecyclerView driverListRecyclerView;


    private DatabaseReference userDatabaseRef;
    private ArrayList<String> UserInformation;
    private ArrayList<String> todayCourses;
    private ArrayList<String> carsList;
    private DriversRecyclerViewAdapter driversRecyclerViewAdapter;
    private View mMainView;
    private String nip;
    private String position;
    private String userID;
    private CircleButton add;



    public TodayCoursesFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_today_courses, container, false);


        driverListRecyclerView= (RecyclerView) mMainView.findViewById(R.id.driversRecyclerView);


        todayCourses=new ArrayList<>();
        carsList=new ArrayList<>();
        UserInformation = new ArrayList<>();

        init();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddCourseActivity.class);
                intent.putExtra(StaticVariable.USER_INFORMATION,UserInformation);
                intent.putExtra(StaticVariable.CARS_ID_LIST,carsList);
                startActivity(intent);
            }
        });

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

        add = mMainView.findViewById(R.id.add);

        CoursesManagerActivity activity = (CoursesManagerActivity) getActivity();
        UserInformation = activity.getUserInformation();
        carsList = activity.getCarsListInformation();
        todayCourses=activity.getTodayCourses();



    }
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
