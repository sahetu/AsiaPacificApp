package asiapacific.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileActivity extends AppCompatActivity {

    EditText edttxt_name, edttxt_email, edttxt_mobile, edttxt_regisPass, edttxt_confirmPass;
    //Button register_btn;
    RadioButton radiobtn_male,radiobtn_female;
    RadioGroup radioGroup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase db;

    String sGender;
    SharedPreferences sp;

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("UserApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),GENDER VARCHAR(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().clear().commit();
                new ToastCommonMethod(ProfileActivity.this, MainActivity.class);
                finish();
            }
        });

        edttxt_name = findViewById(R.id.edttxt_name);
        edttxt_email = findViewById(R.id.edttxt_email);
        edttxt_mobile = findViewById(R.id.edttxt_mobile);
        edttxt_regisPass = findViewById(R.id.edttxt_regisPass);
        edttxt_confirmPass = findViewById(R.id.edttxt_confirmPass);
        //register_btn = findViewById(R.id.register_btn);

        radiobtn_male = findViewById(R.id.radiobtn_male);
        radiobtn_female = findViewById(R.id.radiobtn_female);

        radioGroup = findViewById(R.id.radiogrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                /*i = R.id.radiobtn_male;
                i = R.id.radiobtn_female;*/
                RadioButton rb = findViewById(i);
                sGender = rb.getText().toString();
                new ToastCommonMethod(ProfileActivity.this,sGender);
            }
        });


        /*register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edttxt_name.getText().toString().trim().equals("")){
                    edttxt_name.setError("Please Enter Your Name");
                }
                else if (edttxt_email.getText().toString().trim().equals("")){
                    edttxt_email.setError("Email Id Required");
                }
                else if (!edttxt_email.getText().toString().trim().matches(emailPattern)) {
                    edttxt_email.setError("Email ID Invalid");
                } else if (edttxt_mobile.getText().toString().trim().equals("")) {
                    edttxt_mobile.setError("Please Enter Your Phone Number");
                } else if (edttxt_mobile.getText().toString().trim().length()<10) {
                    edttxt_mobile.setError("Phone Number Invalid");
                }
                else if(radioGroup.getCheckedRadioButtonId()==-1){
                    new ToastCommonMethod(ProfileActivity.this,"Please Select Gender");
                }
                else if (edttxt_regisPass.getText().toString().trim().equals("")) {
                    edttxt_regisPass.setError("Password Required");
                }
                else if (edttxt_regisPass.getText().toString().trim().length()<6) {
                    edttxt_regisPass.setError("Minimum 6 Character Required");
                }
                else if (edttxt_confirmPass.getText().toString().trim().equals("")) {
                    edttxt_confirmPass.setError("Confirm Password Required");
                }
                else if (edttxt_confirmPass.getText().toString().trim().length()<6) {
                    edttxt_confirmPass.setError("Minimum 6 Character Required");
                }
                else if(!edttxt_regisPass.getText().toString().trim().matches(edttxt_confirmPass.getText().toString().trim())){
                    edttxt_confirmPass.setError("Password Does Not Match");
                }
                else {
                    String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+edttxt_email.getText().toString()+"' OR CONTACT='"+edttxt_mobile.getText().toString()+"'";
                    Cursor cursor = db.rawQuery(selectQuery,null);
                    if(cursor.getCount()>0){
                        new ToastCommonMethod(ProfileActivity.this,"Email Id/Contact No. Already Registered");
                    }
                    else{
                        String insertQuery = "INSERT INTO USERS VALUES(NULL,'"+edttxt_name.getText().toString()+"','"+edttxt_email.getText().toString()+"','"+edttxt_mobile.getText().toString()+"','"+sGender+"','"+edttxt_regisPass.getText().toString()+"')";
                        db.execSQL(insertQuery);

                        new ToastCommonMethod(ProfileActivity.this,"Register Successfully");
                        Intent intent = new Intent(ProfileActivity.this, RegisterSuccess.class);
                        startActivity(intent);
                    }
                }
            }
        });*/
        setData();
    }

    private void setData() {
        edttxt_name.setText(sp.getString(ConstantSp.NAME,""));
        edttxt_email.setText(sp.getString(ConstantSp.EMAIL,""));
        edttxt_mobile.setText(sp.getString(ConstantSp.CONTACT,""));
        edttxt_regisPass.setText(sp.getString(ConstantSp.PASSWORD,""));
        edttxt_confirmPass.setText(sp.getString(ConstantSp.PASSWORD,""));

        sGender = sp.getString(ConstantSp.GENDER,"");

        if(sGender.equalsIgnoreCase("Male")){
            radiobtn_male.setChecked(true);
            radiobtn_female.setChecked(false);
        }
        else if(sGender.equalsIgnoreCase("Female")){
            radiobtn_male.setChecked(false);
            radiobtn_female.setChecked(true);
        }
        else{
            radiobtn_male.setChecked(false);
            radiobtn_female.setChecked(false);
        }

    }
}