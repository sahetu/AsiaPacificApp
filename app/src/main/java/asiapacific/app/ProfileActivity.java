package asiapacific.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    EditText edttxt_name, edttxt_email, edttxt_mobile, edttxt_regisPass, edttxt_confirmPass;
    //Button register_btn;
    RadioButton radiobtn_male,radiobtn_female;
    RadioGroup radioGroup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    SQLiteDatabase db;

    String sGender;
    SharedPreferences sp;

    Button logout,editProfile,submit,deleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("UserApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),GENDER VARCHAR(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        editProfile = findViewById(R.id.edit_btn);
        submit = findViewById(R.id.register_btn);
        deleteProfile = findViewById(R.id.delete_btn);

        deleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Delete Profile");
                builder.setMessage("Are You Sure Want To Delete Your Profile!");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*String deleteQuery = "DELETE FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
                        db.execSQL(deleteQuery);
                        sp.edit().clear().commit();
                        new ToastCommonMethod(ProfileActivity.this, MainActivity.class);
                        finish();*/
                        if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                            new doDelete().execute();
                        }
                        else{
                            new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                        }
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new ToastCommonMethod(ProfileActivity.this,"Rate Us");
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });

        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("Logout");
                builder.setMessage("Are You Sure Want To Logout!");
                builder.setCancelable(false);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sp.edit().clear().commit();
                        new ToastCommonMethod(ProfileActivity.this, MainActivity.class);
                        finish();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new ToastCommonMethod(ProfileActivity.this,"Rate Us");
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
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


        submit.setOnClickListener(new View.OnClickListener() {
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
                    //doUpdateSqlite();
                    if(new ConnectionDetector(ProfileActivity.this).networkConnected()){
                        new doUpdate().execute();
                    }
                    else{
                        new ConnectionDetector(ProfileActivity.this).networkDisconnected();
                    }
                }
            }
        });
        setData(false);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });
    }

    private void doUpdateSqlite() {
        String selectQuery = "SELECT * FROM USERS WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.getCount()>0){
            String updateQuery = "UPDATE USERS SET NAME='"+edttxt_name.getText().toString()+"',EMAIL='"+edttxt_email.getText().toString()+"',CONTACT='"+edttxt_mobile.getText().toString()+"',GENDER='"+sGender+"',PASSWORD='"+edttxt_regisPass.getText().toString()+"' WHERE USERID='"+sp.getString(ConstantSp.USERID,"")+"' ";
            db.execSQL(updateQuery);
            new ToastCommonMethod(ProfileActivity.this,"Profile Update Successfully");

            sp.edit().putString(ConstantSp.NAME,edttxt_name.getText().toString()).commit();
            sp.edit().putString(ConstantSp.EMAIL,edttxt_email.getText().toString()).commit();
            sp.edit().putString(ConstantSp.CONTACT,edttxt_mobile.getText().toString()).commit();
            sp.edit().putString(ConstantSp.PASSWORD,edttxt_regisPass.getText().toString()).commit();
            sp.edit().putString(ConstantSp.GENDER,sGender).commit();

            setData(false);
        }
        else{
            new ToastCommonMethod(ProfileActivity.this,"Invalid User");
        }
    }

    private void setData(boolean isEnable) {
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

        edttxt_name.setEnabled(isEnable);
        edttxt_email.setEnabled(isEnable);
        edttxt_mobile.setEnabled(isEnable);
        edttxt_regisPass.setEnabled(isEnable);
        edttxt_confirmPass.setEnabled(isEnable);
        radiobtn_male.setEnabled(isEnable);
        radiobtn_female.setEnabled(isEnable);

        if(isEnable == true){ //true
            edttxt_confirmPass.setVisibility(View.VISIBLE);
            editProfile.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
        else{
            edttxt_confirmPass.setVisibility(View.GONE);
            editProfile.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
        }

    }

    private class doUpdate extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(ProfileActivity.this);
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
            hashMap.put("userid",sp.getString(ConstantSp.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"updateProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                if(object.getBoolean("status")){
                    new ToastCommonMethod(ProfileActivity.this,object.getString("message"));
                    sp.edit().putString(ConstantSp.NAME,edttxt_name.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.EMAIL,edttxt_email.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.CONTACT,edttxt_mobile.getText().toString()).commit();
                    sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                    sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                    setData(false);
                }
                else{
                    new ToastCommonMethod(ProfileActivity.this,object.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class doDelete extends AsyncTask<String,String,String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("userid",sp.getString(ConstantSp.USERID,""));
            return new MakeServiceCall().MakeServiceCall(ConstantSp.BASE_URL+"deleteProfile.php",MakeServiceCall.POST,hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("status")){
                    sp.edit().clear().commit();
                    //Toast.makeText(ProfileActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new ToastCommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                    new ToastCommonMethod(ProfileActivity.this, MainActivity.class);
                    finish();
                }
                else{
                    new ToastCommonMethod(ProfileActivity.this,jsonObject.getString("message"));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
}