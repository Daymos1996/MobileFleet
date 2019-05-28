package courses;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversRecyclerViewAdapter;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.USER_INFORMATION;


public class HistoryCoursesFragment extends Fragment {



    private RecyclerView driverListRecyclerView;
    private CircleButton add;



    private DriversRecyclerViewAdapter driversRecyclerViewAdapter;
    private View mMainView;
    private ArrayList<String> userHistoryCourses;
    private ArrayList<String> UserInformation;




    public HistoryCoursesFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_employees, container, false);


        driverListRecyclerView= (RecyclerView) mMainView.findViewById(R.id.driversRecyclerView);



        init();
    //    loadUserInfo();


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


        CoursesManagerActivity activity = (CoursesManagerActivity) getActivity();
        UserInformation = activity.getUserInformation();
        userHistoryCourses=activity.getHistoryUserCourses();
    }

}
