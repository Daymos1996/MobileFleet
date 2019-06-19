package chat;

import android.content.Context;
import android.net.Uri;
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

import java.util.ArrayList;

import courses.CoursesManagerActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversRecyclerViewAdapter;
import users.StaticVariable;

import static users.StaticVariable.USER_INFORMATION;


public class ListMessageFromEmployessFragment extends Fragment {

    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView ChatEmployeeListRecyclerView;
    private View mMainView;

    private String userID;
    public  ArrayList<String> chatFriendsIdList;



    private DatabaseReference userDatabaseRef;
    private ArrayList<String> driversIdList;
    private ArrayList<String> UserInformation;
    private ChatFriendsRecyclerViewAdapter chatFriendsRecyclerViewAdapter;
    private String nip;
    private String position;


    public ListMessageFromEmployessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_employees, container, false);

        init();
        loadUserInfo();

        user = mAuth.getCurrentUser();
       userID=user.getUid();
       userID = getActivity().getIntent().getStringExtra(StaticVariable.KEY_FRIEND_ID);


        chatEmployeeIdFromDatabase();


        return  mMainView;
    }

    private void init() {
        first_nameTextView = mMainView.findViewById(R.id.txtFirstName);
        last_nameTextView = mMainView.findViewById(R.id.txtLastName);
        ChatEmployeeListRecyclerView=mMainView.findViewById(R.id.driversRecyclerView);
        profilURL = mMainView.findViewById(R.id.avatar);
    }

    private void loadUserInfo(){
        CoursesManagerActivity activity = (CoursesManagerActivity) getActivity();
        UserInformation = activity.getMyUserInformation();
        //  carsList = activity.getCarsListInformation();
        nip=UserInformation.get(0);


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

            //chatFriendsRecyclerViewAdapter = new ChatFriendsRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef, chatFriendsIdList,UserInformation);
            ChatEmployeeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            ChatEmployeeListRecyclerView.setHasFixedSize(true);
            ChatEmployeeListRecyclerView.setAdapter(chatFriendsRecyclerViewAdapter);
            chatFriendsRecyclerViewAdapter.notifyDataSetChanged();

        }

    }


    @Override
    public void onStart() {
        super.onStart();
        chatFriendsIdList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        chatFriendsIdList.clear();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // friendsRecyclerViewAdapter.notifyDataSetChanged();
    }




}
