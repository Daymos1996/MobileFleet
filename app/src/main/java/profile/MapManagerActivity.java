package profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.employee;

import static users.StaticVariable.USER_INFORMATION;

public class MapManagerActivity extends AppCompatActivity {
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



    private ImageView profilURL;
    private TextView first_nameTextView;
    private TextView last_nameTextView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_manager);
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
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent course = new Intent(MapManagerActivity.this, CoursesManagerActivity.class);
                        course.putExtra(USER_INFORMATION, UserInformation);
                        startActivity(course);
                        return true;
                    case R.id.navigation_dashboard:
                        break;
                    case R.id.navigation_notifications:
                        Intent chat = new Intent(MapManagerActivity.this, ChatActivity.class);
                        chat.putExtra(USER_INFORMATION, UserInformation);
                        startActivity(chat);
                        return true;

                    case R.id.navigation_friends:
                        Intent drivers = new Intent(MapManagerActivity.this, DriversActivity.class);
                        drivers.putExtra(USER_INFORMATION, UserInformation);
                        startActivity(drivers);
                        return true;

                    case R.id.navigation_cars:
                        Intent cars = new Intent(MapManagerActivity.this, CarsManagerActivity.class);
                        cars.putExtra(USER_INFORMATION, UserInformation);
                        startActivity(cars);
                        return true;
                }
                return false;
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);




    }


    private void init() {
        reference= FirebaseDatabase.getInstance().getReference().child("Manager");
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();


        first_nameTextView = findViewById(R.id.txtFirstName);
        last_nameTextView = findViewById(R.id.txtLastName);
        profilURL = findViewById(R.id.avatar);

    }

    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getIntent().getSerializableExtra(USER_INFORMATION);
        first_nameTextView.setText(UserInformation.get(0));
        last_nameTextView.setText(UserInformation.get(1));
        Picasso.with(this).load(UserInformation.get(2)).into(profilURL);
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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference("333444555/"+"Employee/").child(userID);
                    dR.child("profilURl").setValue(uploadId);
                }
            });
        }
    }



    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}


