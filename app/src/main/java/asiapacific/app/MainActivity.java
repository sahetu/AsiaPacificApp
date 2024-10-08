package asiapacific.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("UserApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),GENDER VARCHAR(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

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
                /*else if (!mainEmail.getText().toString().matches(emailPattern)) {
                    mainEmail.setError("Email ID Invalid");
                }*/
                else if(mainPass.getText().toString().trim().equals("")){
                    mainPass.setError("Password Required");
                }
                else if (mainPass.getText().toString().trim().length()<6) {
                    mainPass.setError("Minimum 6 character required");
                } else {
                    //Hello Prince, Good Morning
                    /*Snackbar.make(v,"Login Successfully",Snackbar.LENGTH_SHORT).show();
                    new ToastCommonMethod(MainActivity.this,"Login Successfully");
                    Intent intent = new Intent(MainActivity.this, LoginSuccess.class);
                    startActivity(intent);*/
                    String loginQuery = "SELECT * FROM USERS WHERE (EMAIL='"+mainEmail.getText().toString()+"' OR CONTACT='"+mainEmail.getText().toString()+"') AND PASSWORD='"+mainPass.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(loginQuery,null);
                    if(cursor.getCount()>0){
                        while (cursor.moveToNext()){
                            sp.edit().putString(ConstantSp.USERID,cursor.getString(0)).commit();
                            sp.edit().putString(ConstantSp.NAME,cursor.getString(1)).commit();
                            sp.edit().putString(ConstantSp.EMAIL,cursor.getString(2)).commit();
                            sp.edit().putString(ConstantSp.CONTACT,cursor.getString(3)).commit();
                            sp.edit().putString(ConstantSp.PASSWORD,cursor.getString(5)).commit();
                            sp.edit().putString(ConstantSp.GENDER,cursor.getString(4)).commit();
                        }
                        Snackbar.make(v,"Login Successfully",Snackbar.LENGTH_SHORT).show();
                        new ToastCommonMethod(MainActivity.this,"Login Successfully");
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Snackbar.make(v,"Login Unsuccessfully",Snackbar.LENGTH_SHORT).show();
                        new ToastCommonMethod(MainActivity.this,"Login Unsuccessfully");
                    }

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