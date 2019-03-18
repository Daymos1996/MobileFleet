package register;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

        import daymos.lodz.uni.math.pl.mobilefleet.R;
        import login.LoginActivity;

        import static android.widget.Toast.LENGTH_LONG;

public class ChoiceRegisterActivity extends AppCompatActivity {

    private RadioButton radioButtoManager;
    private RadioButton radioButtonEmployee;
    private RadioGroup radioGroup;
    private Button buttonNext;
    private RadioButton radioGroupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_register);
        init();



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.radioButtoManager:
                        buttonNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChoiceRegisterActivity.this, RegisterManagerActivity.class);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.radioButtonEmployee:
                        buttonNext.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ChoiceRegisterActivity.this, RegisterEmployeeActivity.class);
                                startActivity(intent);
                            }
                        });
                        break;

                }
            }
        });




    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void init() {
        radioGroup=findViewById(R.id.radioGroup);
        radioButtoManager=findViewById(R.id.radioButtoManager);
        radioButtonEmployee=findViewById(R.id.radioButtonEmployee);
        buttonNext = findViewById(R.id.buttonNext);

    }
}
