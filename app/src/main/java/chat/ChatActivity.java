package chat;

import android.content.Intent;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.StaticVariable;



import static users.StaticVariable.CHAT_TABLE;


public class ChatActivity extends AppCompatActivity {
    private DatabaseReference myRef;
    private DatabaseReference otherUserReference;
    private DatabaseReference friendsOtherUserReference;
    private DatabaseReference myMessage;
    private String userID;
    private String otherUserID;
    private FirebaseAuth mAuth;
    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_chat;
    FloatingActionButton fab;
    private RecyclerView messagesListRecyclerView;
    private ChatRecyclerViewAdapter chatRecyclerViewAdapter;



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;

    private ArrayList<String> driversIdList;
    private ArrayList<String> UserInformation;
    private String nip;
    private String position;






    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(requestCode == RESULT_OK)
            {
                Snackbar.make(activity_chat,"Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
                displayChatMessage(userID,otherUserID);
            }
            else
            {
                Snackbar.make(activity_chat,"We couldnt sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userID = user.getUid();
        }

        otherUserID = getIntent().getStringExtra(StaticVariable.KEY_CHAT);
        nip = getIntent().getStringExtra(StaticVariable.NIP_INFORMATION);
        setOnCLickActionOnSendMessageButton();


        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            //startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_chat,"Welcome "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            displayChatMessage(userID,otherUserID);
        }
    }

    private void init() {
        activity_chat = findViewById(R.id.activity_chat);
        messagesListRecyclerView = findViewById(R.id.list_of_message);
        fab = findViewById(R.id.fab);

    }

    private void setOnCLickActionOnSendMessageButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input = findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID).child(CHAT_TABLE).child(otherUserID).push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(otherUserID).child(CHAT_TABLE).child(userID).push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));

                input.setText("");
            }
        });
    }
    private void displayChatMessage(String userID, String currentUserID) {
        FirebaseDatabase firebaseDatabase;
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference(nip+"/Employee/");
        otherUserReference = myRef.child(userID);
        friendsOtherUserReference = otherUserReference.child(CHAT_TABLE).child(otherUserID);



        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(ChatActivity.this,friendsOtherUserReference);
        messagesListRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        messagesListRecyclerView.setHasFixedSize(true);
        messagesListRecyclerView.setAdapter(chatRecyclerViewAdapter);
        chatRecyclerViewAdapter.notifyDataSetChanged();

    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}


