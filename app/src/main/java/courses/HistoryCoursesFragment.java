package courses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import at.markushi.ui.CircleButton;
import daymos.lodz.uni.math.pl.mobilefleet.R;


public class HistoryCoursesFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseUser user;

    private RecyclerView CoursesRecyclerView;
    private CoursesRecyclerViewAdapter coursesRecyclerViewAdapter;


    private DatabaseReference userDatabaseRef;
    private ArrayList<String> UserInformation;
    private ArrayList<String> HistoryUserCourses;
    private ArrayList<String> HistoryAllUserCourses;
    private ArrayList<String> carsList;
    private View mMainView;
    private String nip;
    private String position;
    private String userID;
    private CircleButton add;



    public HistoryCoursesFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_today_courses, container, false);


        HistoryUserCourses=new ArrayList<>();
        HistoryAllUserCourses=new ArrayList<>();
        carsList=new ArrayList<>();
        UserInformation = new ArrayList<>();


        init();

        HistoryUserIdCourse();




        return  mMainView;
    }

    private void init() {

        add = mMainView.findViewById(R.id.add);
        CoursesRecyclerView= (RecyclerView) mMainView.findViewById(R.id.coursesRecyclerView);
        add.setVisibility(View.INVISIBLE);
        CoursesManagerActivity activity = (CoursesManagerActivity) getActivity();
        UserInformation = activity.getMyUserInformation();
       // carsList = activity.getCarsListInformation();
        nip=UserInformation.get(0);

    }


    private void setHistoryUserList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                HistoryUserCourses.add(ds.getKey());

        }
        RecyclerViewLoad();
    }

    private void HistoryUserIdCourse() {
        DatabaseReference allTodaysCourses = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Courses/");
        allTodaysCourses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setHistoryUserList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }

    private void setHistoryAllUserList(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                HistoryAllUserCourses.add(ds.getKey());

        }

        RecyclerViewLoadAllUser();
    }

    private void historyAllUserIdCourse() {
        DatabaseReference allTodaysCourses = FirebaseDatabase.getInstance().getReference().child(nip+"/Courses/");
        allTodaysCourses.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setHistoryAllUserList(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
    }


    private void RecyclerViewLoad(){
        if(HistoryUserCourses != null && (!HistoryUserCourses.isEmpty())) {


            userDatabaseRef= FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Courses/");

            coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef, HistoryUserCourses,nip);
            CoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            CoursesRecyclerView.setHasFixedSize(true);
            CoursesRecyclerView.setAdapter(coursesRecyclerViewAdapter);
            coursesRecyclerViewAdapter.notifyDataSetChanged();

        }

    }

    private void RecyclerViewLoadAllUser(){
        if(HistoryAllUserCourses != null && (!HistoryAllUserCourses.isEmpty())) {

            userDatabaseRef= FirebaseDatabase.getInstance().getReference().child(nip+"/Courses/");

            coursesRecyclerViewAdapter = new CoursesRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef, HistoryAllUserCourses,nip);
            CoursesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            CoursesRecyclerView.setHasFixedSize(true);
            CoursesRecyclerView.setAdapter(coursesRecyclerViewAdapter);
            coursesRecyclerViewAdapter.notifyDataSetChanged();

        }

    }
    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
