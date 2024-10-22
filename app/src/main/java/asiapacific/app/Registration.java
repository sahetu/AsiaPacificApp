package asiapacific.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import asiapacific.app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration extends AppCompatActivity {
    EditText edttxt_name, edttxt_email, edttxt_mobile, edttxt_regisPass, edttxt_confirmPass;
    Button register_btn;
    //RadioButton radiobtn_male,radiobtn_female;
    RadioGroup radioGroup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase db;

    String sGender;
    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        db = openOrCreateDatabase("UserApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),GENDER VARCHAR(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        edttxt_name = findViewById(R.id.edttxt_name);
        edttxt_email = findViewById(R.id.edttxt_email);
        edttxt_mobile = findViewById(R.id.edttxt_mobile);
        edttxt_regisPass = findViewById(R.id.edttxt_regisPass);
        edttxt_confirmPass = findViewById(R.id.edttxt_confirmPass);
        register_btn = findViewById(R.id.register_btn);
        /*radiobtn_male = findViewById(R.id.radiobtn_male);
        radiobtn_female = findViewById(R.id.radiobtn_female);*/

        radioGroup = findViewById(R.id.radiogrp);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                /*i = R.id.radiobtn_male;
                i = R.id.radiobtn_female;*/
                RadioButton rb = findViewById(i);
                sGender = rb.getText().toString();
                new ToastCommonMethod(Registration.this,sGender);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
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
                    new ToastCommonMethod(Registration.this,"Please Select Gender");
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
                    //doSqliteSignup();
                    if(new ConnectionDetector(Registration.this).networkConnected()){
                        //new ToastCommonMethod(Registration.this,"Internet/Wifi Connected");
                        //AsyncTask
                        //Volley
                        //Retrofit
                        //new doSignup().execute();
                        pd = new ProgressDialog(Registration.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doRetrofitSignup();
                    }
                    else{
                        new ConnectionDetector(Registration.this).networkDisconnected();
                    }
                }
            }
        });

        /*radiobtn_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastCommonMethod(Registration.this, "Male");
            }
        });

        radiobtn_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ToastCommonMethod(Registration.this, "Female");
            }
        });*/


    }

    private void doRetrofitSignup() {
        Call<GetSignupData> call = apiInterface.getSignupData(
                edttxt_name.getText().toString(),
                edttxt_email.getText().toString(),
                edttxt_mobile.getText().toString(),
                edttxt_regisPass.getText().toString(),
                sGender
        );

        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        new ToastCommonMethod(Registration.this,response.body().message);
                        Intent intent = new Intent(Registration.this, RegisterSuccess.class);
                        startActivity(intent);
                    }
                    else{
                        new ToastCommonMethod(Registration.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(Registration.this,ConstantSp.SERVER_ERROR_MESSAGE+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(Registration.this,t.getMessage());
            }
        });

    }

    private void doSqliteSignup() {
        String selectQuery = "SELECT * FROM USERS WHERE EMAIL='"+edttxt_email.getText().toString()+"' OR CONTACT='"+edttxt_mobile.getText().toString()+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            new ToastCommonMethod(Registration.this,"Email Id/Contact No. Already Registered");
        }
        else{
            String insertQuery = "INSERT INTO USERS VALUES(NULL,'"+edttxt_name.getText().toString()+"','"+edttxt_email.getText().toString()+"','"+edttxt_mobile.getText().toString()+"','"+sGender+"','"+edttxt_regisPass.getText().toString()+"')";
            db.execSQL(insertQuery);

            new ToastCommonMethod(Registration.this,"Register Successfully");
            Intent intent = new Intent(Registration.this, RegisterSuccess.class);
            startActivity(intent);
        }
    }

    private class doSignup extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Registration.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("name",edttxt_name.getText().toString());
            hashMap.put("email",edttxt_email.getText().toString());
            hashMap.put("contact",edttxt_mobile.getText().toString());
            hashMap.put("password",edttxt_regisPass.getText().toString());
            hashMap.put("gender",sGender);
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"signup.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    new ToastCommonMethod(Registration.this,object.getString("message"));
                    Intent intent = new Intent(Registration.this, RegisterSuccess.class);
                    startActivity(intent);
                }
                else{
                    new ToastCommonMethod(Registration.this,object.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}