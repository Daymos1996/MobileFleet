package Cars;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import daymos.lodz.uni.math.pl.mobilefleet.R;

import static users.StaticVariable.CAR_INFORMATION;
import static users.StaticVariable.USER_INFORMATION;

public class CarInformationActivity extends AppCompatActivity {

    private ArrayList<String> carInformation;
    private ImageView image;
    private TextView carBrand;
    private TextView plateNumber;
    private TextView vinNumber;
    private TextView carMileage;
    private TextView engineCapacity;
    private TextView motorPower;
    private TextView yearProduction;
    private TextView technicalExamination;
    private TextView termOC;
    private TextView txtDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);

        init();
        loadCarInformation();


    }

    private void init() {

        //mAuth = FirebaseAuth.getInstance();
       // database = FirebaseDatabase.getInstance();
       // mStorage = FirebaseStorage.getInstance().getReference();
        //txtFirstName = findViewById(R.id.txtFirstName);
        image = findViewById(R.id.image);
        carBrand = findViewById(R.id.editCarBrand);
        plateNumber=findViewById(R.id.editRegistrationNumber);
        vinNumber=findViewById(R.id.editVinNumber);
        carMileage=findViewById(R.id.editCarMileage);
        engineCapacity = findViewById(R.id.editEngineCapacity);
        motorPower=findViewById(R.id.editMotorPower);
        yearProduction=findViewById(R.id.editYearOfProduction);
        technicalExamination=findViewById(R.id.editTermOfTechnicalExamination);
        termOC=findViewById(R.id.edtiTermOC);


        txtDelete=findViewById(R.id.txtDelete);


    }
    private void loadCarInformation(){
        carInformation =(ArrayList<String>)getIntent().getSerializableExtra(CAR_INFORMATION);
        Picasso.with(this).load(carInformation.get(0)).into(image);
        carBrand.setText(carInformation.get(1));
        plateNumber.setText(carInformation.get(2));
        vinNumber.setText(carInformation.get(3));
        carMileage.setText(carInformation.get(4));
        engineCapacity.setText(carInformation.get(5));
        motorPower.setText(carInformation.get(6));
        yearProduction.setText(carInformation.get(7));
        technicalExamination.setText(carInformation.get(8));
        termOC.setText(carInformation.get(9));

    }
}
