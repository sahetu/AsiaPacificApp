package asiapacific.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import asiapacific.app.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    EditText mainEmail, mainPass;
    Button loginButton;
    TextView new_user;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SQLiteDatabase db;
    SharedPreferences sp;

    ApiInterface apiInterface;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

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
                    //doLoginSqlite(v);
                    if(new ConnectionDetector(MainActivity.this).networkConnected()){
                        //new doLogin().execute();
                        pd = new ProgressDialog(MainActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        doLoginRetrofit();
                    }
                    else{
                        new ConnectionDetector(MainActivity.this).networkDisconnected();
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

    private void doLoginRetrofit() {
        Call<GetLoginData> call  = apiInterface.getLoginData(mainEmail.getText().toString(),mainPass.getText().toString());
        call.enqueue(new Callback<GetLoginData>() {
            @Override
            public void onResponse(Call<GetLoginData> call, Response<GetLoginData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        sp.edit().putString(ConstantSp.USERID,response.body().userDetails.userid).commit();
                        sp.edit().putString(ConstantSp.NAME,response.body().userDetails.name).commit();
                        sp.edit().putString(ConstantSp.EMAIL,response.body().userDetails.email).commit();
                        sp.edit().putString(ConstantSp.CONTACT,response.body().userDetails.contact).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                        sp.edit().putString(ConstantSp.GENDER,response.body().userDetails.gender).commit();

                        new ToastCommonMethod(MainActivity.this,response.body().message);
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                    else{
                        new ToastCommonMethod(MainActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(MainActivity.this,ConstantSp.SERVER_ERROR_MESSAGE+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetLoginData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(MainActivity.this,t.getMessage());
            }
        });
    }

    private void doLoginSqlite(View v) {
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

    private class doLogin extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("email",mainEmail.getText().toString());
            hashMap.put("password",mainPass.getText().toString());
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"login.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    JSONObject jsonObject = object.getJSONObject("UserDetails");
                    sp.edit().putString(ConstantSp.USERID,jsonObject.getString("userid")).commit();
                    sp.edit().putString(ConstantSp.NAME,jsonObject.getString("name")).commit();
                    sp.edit().putString(ConstantSp.EMAIL,jsonObject.getString("email")).commit();
                    sp.edit().putString(ConstantSp.CONTACT,jsonObject.getString("contact")).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                    sp.edit().putString(ConstantSp.GENDER,jsonObject.getString("gender")).commit();

                    new ToastCommonMethod(MainActivity.this,object.getString("message"));
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);

                }
                else{
                    new ToastCommonMethod(MainActivity.this,object.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}