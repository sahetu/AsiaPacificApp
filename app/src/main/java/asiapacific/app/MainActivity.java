package asiapacific.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import asiapacific.app.R;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    EditText mainEmail, mainPass;
    Button loginButton;
    TextView new_user;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainEmail = findViewById(R.id.main_email);
        mainPass = findViewById(R.id.main_pass);
        loginButton = findViewById(R.id.login_btn);
        new_user = findViewById(R.id.new_user);

        //Hello Parv, Good Morning

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainEmail.getText().toString().trim().equals("")){
                    mainEmail.setError("Email Id Required");
                }
                else if (!mainEmail.getText().toString().matches(emailPattern)) {
                    mainEmail.setError("Email ID Invalid");
                }
                else if(mainPass.getText().toString().trim().equals("")){
                    mainPass.setError("Password Required");
                }
                else if (mainPass.getText().toString().trim().length()<6) {
                    mainPass.setError("Minimum 6 character required");
                } else {
                    //Hello Prince, Good Morning
                    Snackbar.make(v,"Login Successfully",Snackbar.LENGTH_SHORT).show();
                    new ToastCommonMethod(MainActivity.this,"Login Successfully");
                    Intent intent = new Intent(MainActivity.this, LoginSuccess.class);
                    startActivity(intent);
                }
            }
        });

        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

    }
}