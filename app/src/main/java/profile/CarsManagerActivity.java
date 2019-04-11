package profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import chat.ChatActivity;
import chat.ChatListActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import drivers.DriversActivity;

import static users.StaticVariable.CHAT_EMPLOYEE_ID_LIST;
import static users.StaticVariable.DRIVERS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.POSITION_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class CarsManagerActivity extends AppCompatActivity {

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
    private String userID;
    private ArrayList<String> UserInformation;
    private ArrayList<String> driversIdList;
    private ArrayList<String>  chatEmployeeList;

    private String nip;
    private String position;



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_manager);
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);


        driversIdList=new ArrayList<>();
        chatEmployeeList=new ArrayList<>();

        init();
        loadUserInfo();

        user = mAuth.getCurrentUser();
        userID = user.getUid();
        mProgresDiaolog = new ProgressDialog(this);



        profilURL.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openFileChooser(userID);
                return false;
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent course = new Intent(CarsManagerActivity.this, CoursesManagerActivity.class);
                        course.putExtra(NIP_INFORMATION,nip);
                        course.putExtra(POSITION_INFORMATION,position);
                        course.putExtra(DRIVERS_ID_LIST,driversIdList);
                        course.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(course);
                        return true;
                    case R.id.navigation_dashboard:
                        Intent map = new Intent(CarsManagerActivity.this, MapManagerActivity.class);
                        map.putExtra(USER_INFORMATION, UserInformation);
                        map.putExtra(DRIVERS_ID_LIST,driversIdList);
                        map.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(map);
                        return true;
                    case R.id.navigation_notifications:
                        Intent chat = new Intent(CarsManagerActivity.this, ChatListActivity.class);
                        chat.putExtra(USER_INFORMATION, UserInformation);
                        chat.putExtra(DRIVERS_ID_LIST,driversIdList);
                        chat.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(chat);
                        return true;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(CarsManagerActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        drivers.putExtra(DRIVERS_ID_LIST,driversIdList);
                        drivers.putExtra(CHAT_EMPLOYEE_ID_LIST, chatEmployeeList);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        break;

                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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
        chatEmployeeList=(ArrayList<String>)getIntent().getSerializableExtra(CHAT_EMPLOYEE_ID_LIST);
        nip=UserInformation.get(0);
        position=UserInformation.get(1);
        first_nameTextView.setText(UserInformation.get(2));
        last_nameTextView.setText(UserInformation.get(3));
        Picasso.with(this).load(UserInformation.get(4)).into(profilURL);
    }

    private void openFileChooser(final String userID) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        // intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri mImageProfileUri = data.getData();
            /*
            CropImage.activity(imagePath)
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setAspectRatio(1, 1)
                    .start(ProfilActivity.this);
        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_OK) {
                mImageProfileUri = result.getUri();

                Picasso.with(this).load(mImageProfileUri).into(profilURL);
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
            */

            // DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(userID);
            // dR.child("profilURl").setValue(mImageProfileUri);
            // toastMessage("Username update");

            mProgresDiaolog.setMessage("Uploading...");
            mProgresDiaolog.show();

            final StorageReference filepath = mStorage.child("profile_img").child(userID).child("profile_picture.jpg");
            filepath.putFile(mImageProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgresDiaolog.dismiss();
                    toastMessage("Zaladowano zdjecie ");
                }
            });


            //dodanie do bazy danych
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUri) {
                    String uploadId = downloadUri.toString();
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"Employee/").child(userID);
                    dR.child("profilURl").setValue(uploadId);
                }
            });
        }
    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}


