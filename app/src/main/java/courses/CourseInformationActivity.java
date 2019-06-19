package courses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.StaticVariable;

import static users.StaticVariable.CAR_INFORMATION;
import static users.StaticVariable.COURSE_INFORMATION;

public class CourseInformationActivity extends AppCompatActivity {

    private TextView txtName,plateNumber,courseTime,toWhere,fromWhere,distance,numberOfPallets,cost,numberInvoice,delete;

    FirebaseUser user;
    private String userID;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog mProgresDiaolog;
    private String nip;
    private ArrayList<String>CourseInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_information);

        CourseInformation = new ArrayList<>();

        init();
        loadCarInformation();
    }


    private void init() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        txtName = findViewById(R.id.txtName);
        plateNumber=findViewById(R.id.plateNumber);
        courseTime=findViewById(R.id.courseTime);
        fromWhere=findViewById(R.id.fromWhere);
        toWhere=findViewById(R.id.toWhere);
        distance = findViewById(R.id.distance);
        numberOfPallets=findViewById(R.id.numberOfPallets);
        cost=findViewById(R.id.cost);
        numberInvoice=findViewById(R.id.numberInvoice);

        delete=findViewById(R.id.txtDelete);

    }

    private void loadCarInformation(){
        CourseInformation =(ArrayList<String>)getIntent().getSerializableExtra(COURSE_INFORMATION);
        txtName.setText(CourseInformation.get(0)+" "+CourseInformation.get(1));
        courseTime.setText(CourseInformation.get(2));
        fromWhere.setText(CourseInformation.get(3));
        toWhere.setText(CourseInformation.get(4));
        distance.setText(CourseInformation.get(5)+" km");
        plateNumber.setText(CourseInformation.get(6));
        numberOfPallets.setText(CourseInformation.get(7));
        cost.setText(CourseInformation.get(8)+" zl");
        numberInvoice.setText(CourseInformation.get(9));

        nip=CourseInformation.get(10);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, CoursesManagerActivity.class);
        intent.putExtra(StaticVariable.NIP_INFORMATION,nip);
        startActivity(intent);;
    }


}
