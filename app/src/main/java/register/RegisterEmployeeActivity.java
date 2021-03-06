package register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import login.LoginActivity;
import users.employee;

public class RegisterEmployeeActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextNIP;
    private Button buttonRegister;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_employee);
        setTitle("Register");
        init();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        editTextFirstName = (EditText) findViewById(R.id.editTextFirstName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextNIP = (EditText) findViewById(R.id.editTextNIP);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
    }

    private void registerUser(){
        final String firstName = editTextFirstName.getText().toString().trim();
        final String lastName = editTextLastName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String phone  = editTextPhone.getText().toString().trim();
        final String nip = editTextNIP.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        final String position = "Employee";


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter your email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Enter your password",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this,"Enter your first name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this,"Enter your last name",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this,"Enter your phone number",Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(nip)){
            Toast.makeText(this,"Enter your company Nip",Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("The registration proceeds...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            employee employee = new employee(email,password ,phone,firstName,lastName,"https://firebasestorage.googleapis.com/v0/b/raczejpiatek-77d73.appspot.com/o/585e4bf3cb11b227491c339a.png?alt=media&token=6b9c8923-a808-44f1-8e44-473b08e9c249",nip,position);
                            try {
                                employee.setPassword(register.Security.encrypt(password));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            FirebaseDatabase.getInstance().getReference(nip).child("Employee")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(employee).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterEmployeeActivity.this,"Registration completed",Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(RegisterEmployeeActivity.this,"Registration error",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Intent intent = new Intent(RegisterEmployeeActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterEmployeeActivity.this,"Registration error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
}
