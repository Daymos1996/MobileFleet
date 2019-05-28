package profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import courses.CoursesManagerActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import register.Security;

import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class EditProfilInformationActivity extends AppCompatActivity {


    private ArrayList<String> UserInformation;
    private ImageView image;
    private TextView txtFirstName;
    private TextView txtLastName;
    private TextView txtEmail;
    private TextView txtPassword;
    private TextView txtDelete;
    private TextView txtPhone;

    private String nip;
    private ProgressDialog mProgresDiaolog;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference reference;
    private FirebaseDatabase database;
    FirebaseUser user;
    private String userID;

    private static final String TAG = "ViewDatabase";
    public static final int PICK_IMAGE = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_information);

        UserInformation = new ArrayList<>();
        mProgresDiaolog = new ProgressDialog(this);

        init();
        loadUserInfo();

        user = mAuth.getCurrentUser();
        userID = user.getUid();


        txtFirstName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateFirstName(userID);
                loadUserInfo();
                return false;
            }
        });

        txtLastName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateLastName(userID);
                return false;
            }
        });

        txtEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateEmail(userID);
                return false;
            }
        });



        txtPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdatePhone(userID);
                return false;
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openFileChooser();
                return false;
            }
        });

        txtDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               deleteUser(userID);
                return false;
            }
        });

        txtPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdatePassword(userID);
                return false;
            }
        });



    }



    private void init() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail=findViewById(R.id.txtEmail);
        image = findViewById(R.id.image);
        txtPhone=findViewById(R.id.txtPhone);
        txtPassword=findViewById(R.id.txtPassword);
        txtDelete=findViewById(R.id.txtDelete);


    }
    private void loadUserInfo(){
        UserInformation =(ArrayList<String>)getIntent().getSerializableExtra(USER_INFORMATION);
        nip=UserInformation.get(0);
        txtFirstName.setText(UserInformation.get(1));
        txtLastName.setText(UserInformation.get(2));
        Picasso.with(this).load(UserInformation.get(3)).into(image);
        txtEmail.setText(UserInformation.get(4));
        txtPhone.setText(UserInformation.get(5));
    }


    public void deleteUser(String userID) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userData = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);

        userData.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });


        user.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User account deleted.");
                        }
                    }
                });

    }


    private boolean UpdateFirstName(final String userID) {
        //getting the specified artist reference

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_data, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    dR.child("first_name").setValue(name);
                    toastMessage("First Name update");
                    b.dismiss();
                    txtFirstName.setText(name);
                }
            }
        });

        return true;
    }

    private boolean UpdateLastName(final String userID) {
        //getting the specified artist reference

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_data, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    dR.child("last_name").setValue(name);
                    toastMessage("Last name update");
                    b.dismiss();
                    txtLastName.setText(name);
                }
            }
        });

        return true;
    }




    private boolean UpdateEmail(final String userID) {
        //getting the specified artist reference

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_data, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updateEmail(name);
                    dR.child("email").setValue(name);
                    toastMessage("email update");
                    b.dismiss();
                    txtEmail.setText(name);
                }
            }
        });
        return true;
    }



    private boolean UpdatePassword(final String userID) {
        //getting the specified artist reference

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_data, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(name);
                    try {
                        dR.child("password").setValue(Security.encrypt(name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    toastMessage("password update");
                    b.dismiss();
                }
            }
        });
        return true;
    }


    private boolean UpdatePhone(final String userID) {
        //getting the specified artist reference

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_data, null);
        dialogBuilder.setView(dialogView);
        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateArtist);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                if (!TextUtils.isEmpty(name)) {
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    dR.child("phone").setValue(name);
                    toastMessage("Phone number update");
                    b.dismiss();
                    txtPhone.setText(name);
                }
            }
        });

        return true;
    }

    private void openFileChooser() {
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
            final Uri mImageProfileUri = data.getData();
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
                    toastMessage("Photo loaded ");
                    Picasso.with(EditProfilInformationActivity.this).load(mImageProfileUri.toString()).into(image);
                }
            });


            //dodanie do bazy danych
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUri) {
                    String uploadId = downloadUri.toString();
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Employee/").child(userID);
                    dR.child("profilURl").setValue(uploadId);

                }
            });
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(EditProfilInformationActivity.this, CoursesManagerActivity.class);
        intent.putExtra(NIP_INFORMATION,nip);
        startActivity(intent);
    }


}
