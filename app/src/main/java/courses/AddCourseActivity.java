package courses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Cars.CarAddActivity;
import daymos.lodz.uni.math.pl.mobilefleet.R;
import users.FindCars;
import users.courses;

import static users.StaticVariable.CARS_ID_LIST;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class AddCourseActivity extends AppCompatActivity {

    private EditText fromWhere, toWhere, distance, numberOfPallets , plateNumber, numberInvoice ,cost;
    private Button buttonAdd;
    private String nip,firstName,lastName,oldMileage;
    private int newMileageInt;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private DatabaseReference userDatabaseRef;
    private ProgressDialog progressDialog;
    private ProgressDialog mProgresDiaolog;
    private ArrayList<String> UserInformation;
    private ArrayList<String> carsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        init();



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCourse(firstName,lastName);
            }
        });

    }



    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        UserInformation =(ArrayList<String>)getIntent().getSerializableExtra(USER_INFORMATION);
        carsList=(ArrayList<String>)getIntent().getSerializableExtra(CARS_ID_LIST);
        nip=UserInformation.get(0);
        firstName=UserInformation.get(1);
        lastName= UserInformation.get(2);


        fromWhere = (EditText) findViewById(R.id.fromWhere);
        toWhere = (EditText) findViewById(R.id.toWhere);
        plateNumber = (EditText) findViewById(R.id.plateNumber);
        distance = (EditText) findViewById(R.id.distance);
        numberInvoice = (EditText) findViewById(R.id.numberInvoice);
        numberOfPallets = (EditText) findViewById(R.id.numberOfPallets);
        cost = (EditText) findViewById(R.id.cost);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);

    }

    private void changeCarMileage(final String distance, final String plateNumber){
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(nip+"/Cars");
        userDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getKey().equals(plateNumber.trim())) {
                        oldMileage=postSnapshot.child("carMileage").getValue().toString();
                        newMileageInt=Integer.parseInt(oldMileage)+Integer.parseInt(distance);
                    }
                }
                FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plateNumber).child("carMileage").
                        setValue(String.valueOf(newMileageInt)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }




    private void addCourse(String firstName, String lastName){
        final String fromWheree = fromWhere.getText().toString().trim();
        final String toWheree = toWhere.getText().toString().trim();
        final String plateNumberr = plateNumber.getText().toString().trim();
        final String distancee  = distance.getText().toString().trim();
        final String numberInvoicee = numberInvoice.getText().toString().trim();
        final String numberOfPalletss  = numberOfPallets.getText().toString().trim();
        final String costt  = cost.getText().toString().trim();


        if(TextUtils.isEmpty(fromWheree)){
            Toast.makeText(this,"Enter from where",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(toWheree)){
            Toast.makeText(this,"Enter  to where",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(plateNumberr)){
            Toast.makeText(this,"Enter  plate number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(distancee)){
            Toast.makeText(this,"Enter distance",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(numberInvoicee)){
            Toast.makeText(this,"Enter number invoice",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(numberOfPalletss)){
            Toast.makeText(this,"Enter number of pallets ",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(costt)){
            Toast.makeText(this,"Enter cost of transport",Toast.LENGTH_LONG).show();
            return;
        }




        progressDialog.setMessage("The add proceeds...");
        progressDialog.show();



        courses course = new courses(fromWheree,toWheree,plateNumberr,firstName,lastName,
                distancee,numberInvoicee,numberOfPalletss,costt);


        FirebaseDatabase.getInstance().getReference(nip+"/Courses/").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push().setValue(course).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddCourseActivity.this,"Successful course car",Toast.LENGTH_LONG).show();
                    changeCarMileage(distancee,plateNumberr);

                    Intent intent = new Intent(AddCourseActivity.this, CoursesManagerActivity.class);
                    intent.putExtra(NIP_INFORMATION,nip);
                    startActivity(intent);
                } else {
                    Toast.makeText(AddCourseActivity.this,"Error during course car",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
}
