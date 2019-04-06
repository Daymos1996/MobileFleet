package profile;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static users.StaticVariable.DRIVER_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CALL =1 ;
    private ArrayList<String> DriverInformation;
    private ImageView image;
    private TextView txtName;
    private TextView txtPhone;
    private CircleImageView phone;
    private CircleImageView map;
    private CircleImageView chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
        loadUserInfo();

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCallPhone();
            }
        });


    }

    private void init() {

        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        image = findViewById(R.id.image);
        phone=findViewById(R.id.phone);
        chat=findViewById(R.id.chat);
        map=findViewById(R.id.map);


    }
    private void loadUserInfo(){
        DriverInformation =(ArrayList<String>)getIntent().getSerializableExtra(DRIVER_INFORMATION);
        txtName.setText(DriverInformation.get(1));
        txtPhone.setText(DriverInformation.get(2));
        Picasso.with(this).load(DriverInformation.get(3)).into(image);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                makeCallPhone();
            }
            else {
                toastMessage("Permission DENIED");
            }
        }
    }

    private void makeCallPhone(){
        String number=txtPhone.getText().toString();
        if(number.trim().length() >0) {
            if(ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else{
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }

        }else {
            toastMessage("wrong number");
        }

    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
