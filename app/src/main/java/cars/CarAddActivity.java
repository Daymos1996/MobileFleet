package cars;

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
import com.google.firebase.database.FirebaseDatabase;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import courses.CoursesManagerActivity;
import users.FindCars;

import static users.StaticVariable.NIP_INFORMATION;


public class CarAddActivity extends AppCompatActivity {

    private ImageView carUrl;
    private EditText carBrand;
    private EditText plateNumber;
    private EditText vinNumber;
    private EditText carMileage;
    private EditText engineCapacity;
    private EditText motorPower;
    private EditText yearProduction;
    private EditText technicalExamination;
    private EditText termOC;
    private String nip;

    private Button buttonAdd;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;
    private ProgressDialog mProgresDiaolog;


    private static final String TAG = "ViewDatabase";
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);
        setTitle("car add");
        init();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
            }
        });

    }

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        nip= (String) getIntent().getSerializableExtra(NIP_INFORMATION);


        carUrl = (ImageView) findViewById(R.id.carUrl);
        carBrand = (EditText) findViewById(R.id.editCarBrand);
        plateNumber = (EditText) findViewById(R.id.editRegistrationNumber);
        vinNumber = (EditText) findViewById(R.id.editVinNumber);
        carMileage = (EditText) findViewById(R.id.editCarMileage);
        engineCapacity = (EditText) findViewById(R.id.editEngineCapacity);
        motorPower = (EditText) findViewById(R.id.editMotorPower);
        yearProduction = (EditText) findViewById(R.id.editYearOfProduction);
        technicalExamination = (EditText) findViewById(R.id.editTermOfTechnicalExamination);
        termOC = (EditText) findViewById(R.id.edtiTermOC);

        buttonAdd = (Button) findViewById(R.id.buttonAdd);
    }



    private void addCar(){
        final String carBrandd = carBrand.getText().toString().trim();
        final String plateNumberr = plateNumber.getText().toString().trim();
        final String vinNumberr = vinNumber.getText().toString().trim();
        final String carMileagee  = carMileage.getText().toString().trim();
        final String engineCapacityy = engineCapacity.getText().toString().trim();
        final String motorPowerr  = motorPower.getText().toString().trim();
        final String yearProductionn  = yearProduction.getText().toString().trim();
        final String technicalExaminationn  = technicalExamination.getText().toString().trim();
        final String termOCC  = termOC.getText().toString().trim();



        if(TextUtils.isEmpty(carBrandd)){
            Toast.makeText(this,"Enter car brand",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(plateNumberr)){
            Toast.makeText(this,"Enter  plate number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(vinNumberr)){
            Toast.makeText(this,"Enter  vin number",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(carMileagee)){
            Toast.makeText(this,"Enter car mileage",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(engineCapacityy)){
            Toast.makeText(this,"Enter engine capacity",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(motorPowerr)){
            Toast.makeText(this,"Enter motor power",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(yearProductionn)){
            Toast.makeText(this,"Enter year production",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(technicalExaminationn)){
            Toast.makeText(this,"Enter technical examination",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(termOCC)){
            Toast.makeText(this,"Enter term oc",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("The add proceeds...");
        progressDialog.show();

        FindCars car = new FindCars("https://firebasestorage.googleapis.com/v0/b/mobilefleet-547ef.appspot.com/o/truck.png?alt=media&token=b6565e1c-c14b-410e-bb64-6c98c1d2429a",carBrandd,plateNumberr,vinNumberr,carMileagee,engineCapacityy,
                motorPowerr,yearProductionn,technicalExaminationn,termOCC,nip);

        FirebaseDatabase.getInstance().getReference(nip+ "/cars/" +plateNumberr).setValue(car).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CarAddActivity.this,"Successful add car",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CarAddActivity.this, CoursesManagerActivity.class);
                    intent.putExtra(NIP_INFORMATION,nip);
                    startActivity(intent);
                } else {
                    Toast.makeText(CarAddActivity.this,"Error during add car",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
