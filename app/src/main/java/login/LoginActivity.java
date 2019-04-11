package login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import daymos.lodz.uni.math.pl.mobilefleet.R;
import profile.CoursesManagerActivity;
import register.ChoiceRegisterActivity;
import register.RegisterEmployeeActivity;
import register.RegisterManagerActivity;
import static users.StaticVariable.NIP_INFORMATION;
import static users.StaticVariable.POSITION_INFORMATION;


public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText nipText;
    private TextView registerTextView;
    private Button buttunLogIn;


    private FirebaseDatabase database;
    private FirebaseUser user;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        init();


        registerTextView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, ChoiceRegisterActivity.class);
            startActivity(intent);

            }
        });


        buttunLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });


}

    private void init() {
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        buttunLogIn = findViewById(R.id.buttunLogIn);
        registerTextView = findViewById(R.id.textViewRegister);
        nipText = findViewById(R.id.editTextNIP);

    }

    private void LoginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        final String nip = nipText.getText().toString().trim();


            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                Toast.makeText(LoginActivity.this, "Loggin in", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LoginActivity.this, CoursesManagerActivity.class);
                                intent.putExtra(NIP_INFORMATION,nip);
                                startActivity(intent);
                            } else {
                                Toast.makeText(LoginActivity.this, "Error during login", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

    }

}
