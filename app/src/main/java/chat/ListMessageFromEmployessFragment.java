package chat;

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
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversRecyclerViewAdapter;
import users.StaticVariable;

import static users.StaticVariable.USER_INFORMATION;


public class ListMessageFromEmployessFragment extends Fragment {

    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;

    private RecyclerView ChatEmployeeListRecyclerView;
    private View mMainView;

    private String userID;
    public static ArrayList<String> chatFriendsIdList;



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

        userID = getActivity().getIntent().getStringExtra(StaticVariable.KEY_FRIEND_ID);

        chatFriendsIdList = (ArrayList<String>) getActivity().getIntent().getSerializableExtra(StaticVariable.CHAT_EMPLOYEE_ID_LIST);

        toastMessage(String.valueOf(chatFriendsIdList.size()));

        if(!chatFriendsIdList.isEmpty()) {
            userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Employee/");

            chatFriendsRecyclerViewAdapter = new ChatFriendsRecyclerViewAdapter(getActivity().getBaseContext(), userDatabaseRef, chatFriendsIdList);
            ChatEmployeeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
            ChatEmployeeListRecyclerView.setHasFixedSize(true);
            ChatEmployeeListRecyclerView.setAdapter(chatFriendsRecyclerViewAdapter);
            chatFriendsRecyclerViewAdapter.notifyDataSetChanged();

        }

        return  mMainView;
    }

    private void init() {
        first_nameTextView = mMainView.findViewById(R.id.txtFirstName);
        last_nameTextView = mMainView.findViewById(R.id.txtLastName);
        ChatEmployeeListRecyclerView=mMainView.findViewById(R.id.driversRecyclerView);
        profilURL = mMainView.findViewById(R.id.avatar);
    }

    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getActivity().getIntent().getSerializableExtra(USER_INFORMATION);
        nip=UserInformation.get(0);
        position=UserInformation.get(1);

    }

    @Override
    public void onStart() {
        super.onStart();
        //  friendsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        //friendsRecyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // friendsRecyclerViewAdapter.notifyDataSetChanged();
    }



    private void toastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
