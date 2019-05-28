package Cars;

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

import daymos.lodz.uni.math.pl.mobilefleet.R;
import courses.CoursesManagerActivity;

import static users.StaticVariable.CAR_INFORMATION;
import static users.StaticVariable.NIP_INFORMATION;

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
    FirebaseUser user;
    private String userID;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog mProgresDiaolog;
    private String nip;
    private String plate;

    private static final String TAG = "ViewDatabase";
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);
        mProgresDiaolog = new ProgressDialog(this);

        init();
        loadCarInformation();

        user = mAuth.getCurrentUser();
        userID = user.getUid();

        txtDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteCar(plate);
                Intent intent = new Intent(CarInformationActivity.this, CoursesManagerActivity.class);
                intent.putExtra(NIP_INFORMATION,nip);
                startActivity(intent);

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

        carBrand.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateCarBrand(plate);
                return false;
            }
        });

        carMileage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateCarMileage(plate);
                return false;
            }
        });
        termOC.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateOC(plate);
                return false;
            }
        });
        technicalExamination.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateTechnicalExamination(plate);
                return false;
            }
        });
        engineCapacity.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateEngineCapacity(plate);
                return false;
            }
        });

        motorPower.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdateMotorPower(plate);
                return false;
            }
        });

        /*
        plateNumber.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UpdatePlateNumber(plate);
                plate=plateNumber.getText().toString();
                toastMessage(plate);
                return false;
            }
        });
        */


    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
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
        plate=carInformation.get(2);
        vinNumber.setText(carInformation.get(3));
        carMileage.setText(carInformation.get(4)+" km");
        engineCapacity.setText(carInformation.get(5));
        motorPower.setText(carInformation.get(6)+" kw");
        yearProduction.setText(carInformation.get(7));
        technicalExamination.setText("Technical Examination "+carInformation.get(8));
        termOC.setText("Term OC/AC "+ carInformation.get(9));
        nip=carInformation.get(10);


    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void deleteCar(String plate) {

        DatabaseReference carData = FirebaseDatabase.getInstance().getReference(nip+"/Cars/").child(plate);

        carData.removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Car deleted.");
                            toastMessage("Car deleted");
                        }
                    }
                });
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

            final StorageReference filepath = mStorage.child("car_img").child(plate).child("car_picture.jpg");
            filepath.putFile(mImageProfileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgresDiaolog.dismiss();
                    toastMessage("Photo loaded ");
                    Picasso.with(CarInformationActivity.this).load(mImageProfileUri.toString()).into(image);
                }
            });


            //dodanie do bazy danych
            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUri) {
                    String uploadId = downloadUri.toString();
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("carUrl").setValue(uploadId);

                }
            });
        }
    }

    private boolean UpdateCarBrand(final String plate) {

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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("carBrand").setValue(name);
                    toastMessage("Car brand update");
                    b.dismiss();
                    carBrand.setText(name);
                }
            }
        });

        return true;
    }

    /*
    private boolean UpdatePlateNumber(final String plate) {
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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("plateNumber").setValue(name);
                    toastMessage("Plate number update");
                    b.dismiss();
                    plateNumber.setText(name);
                }
            }
        });

        return true;
    }
    */
    private boolean UpdateCarMileage(final String plate) {

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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("carMileage").setValue(name);
                    toastMessage("Car mileage update");
                    b.dismiss();
                    carMileage.setText(name + " km");
                }
            }
        });

        return true;
    }

    private boolean UpdateOC(final String plate) {

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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("oc").setValue(name);
                    toastMessage("Term oc/ac update");
                    b.dismiss();
                    termOC.setText("Term OC/AC "+name);
                }
            }
        });

        return true;
    }

    private boolean UpdateTechnicalExamination(final String plate) {
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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("technicalExamination").setValue(name);
                    toastMessage("Term technical examination update");
                    b.dismiss();
                    technicalExamination.setText("Technical Examination "+ name);
                }
            }
        });

        return true;
    }

    private boolean UpdateEngineCapacity(final String plate) {
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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("engineCapacity").setValue(name);
                    toastMessage("Engine Capacity update");
                    b.dismiss();
                    engineCapacity.setText(name);
                }
            }
        });

        return true;
    }
    private boolean UpdateMotorPower(final String plate) {
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
                    DatabaseReference dR = FirebaseDatabase.getInstance().getReference(nip+"/Cars/"+plate);
                    dR.child("motorPower").setValue(name);
                    toastMessage("Motor power update");
                    b.dismiss();
                    motorPower.setText(name+ " kw");
                }
            }
        });

        return true;
    }


}
