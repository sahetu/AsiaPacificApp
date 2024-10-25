package asiapacific.app;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    CircleImageView profileIv,cameraIv;

    ApiInterface apiInterface;
    ProgressDialog pd;

    int REQUEST_CODE_CHOOSE = 100;

    String[] appPermission = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] appPermission33 = {android.Manifest.permission.READ_MEDIA_IMAGES, android.Manifest.permission.READ_MEDIA_AUDIO, android.Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.CAMERA};

    private static final int PERMISSION_REQUEST_CODE = 1240;

    List<Uri> mSelected;
    String sSelectedPath;
    String sChangeImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);

        db = openOrCreateDatabase("UserApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),GENDER VARCHAR(10),PASSWORD VARCHAR(20))";
        db.execSQL(tableQuery);

        profileIv = findViewById(R.id.profile_image);
        cameraIv = findViewById(R.id.profile_camera);

        cameraIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermission()) {
                    selectImageData();
                }
            }
        });

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
                            //new doDelete().execute();
                            pd= new ProgressDialog(ProfileActivity.this);
                            pd.setMessage("Please Wait...");
                            pd.setCancelable(false);
                            pd.show();
                            doDeleteRetrofit();
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
                        //new doUpdate().execute();
                        pd= new ProgressDialog(ProfileActivity.this);
                        pd.setMessage("Please Wait...");
                        pd.setCancelable(false);
                        pd.show();
                        if(sChangeImage.equalsIgnoreCase("Yes")){
                            doUpdateImageRetrofit();
                        }
                        else{
                            doUpdateRetrofit();
                        }
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

    public boolean checkAndRequestPermission() {
        List<String> listPermission = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            for (String perm : appPermission33) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(perm);
                }
            }
            if (!listPermission.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), PERMISSION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        } else {
            for (String perm : appPermission) {
                if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                    listPermission.add(perm);
                }
            }
            if (!listPermission.isEmpty()) {
                ActivityCompat.requestPermissions(this, listPermission.toArray(new String[listPermission.size()]), PERMISSION_REQUEST_CODE);
                return false;
            } else {
                return true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            HashMap<String, Integer> permissionResult = new HashMap<>();
            int deniedCount = 0;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult.put(permissions[i], grantResults[i]);
                    deniedCount++;
                }
            }
            if (deniedCount == 0) {
                selectImageData();
            } else {
                for (Map.Entry<String, Integer> entry : permissionResult.entrySet()) {
                    String permName = entry.getKey();
                    int permResult = entry.getValue();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this, permName)) {
                        /*showDialogPermission("", "This App needs Read External Storage And Location permissions to work whithout and problems.",*/
                        showDialogPermission("", "This App needs Read External Storage And Camera permissions to work whithout and problems.",
                                "Yes, Grant permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        checkAndRequestPermission();
                                    }
                                },
                                "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //finishAffinity();
                                    }
                                }, false);
                    } else {
                        showDialogPermission("", "You have denied some permissions. Allow all permissions at [Setting] > [Permissions]",
                                "Go to Settings", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }, "No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        //finish();
                                    }
                                }, false);
                        break;
                    }
                }
            }
        }
    }

    public AlertDialog showDialogPermission(String title, String msg, String positiveLable, DialogInterface.OnClickListener positiveOnClickListener, String negativeLable, DialogInterface.OnClickListener negativeOnClickListener, boolean isCancelable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLable, positiveOnClickListener);
        builder.setNegativeButton(negativeLable, negativeOnClickListener);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

    private void selectImageData() {
        FishBun.with(ProfileActivity.this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(1)
                .isStartInAllView(false)
                .setIsUseDetailView(false)
                .setReachLimitAutomaticClose(true)
                .setSelectCircleStrokeColor(android.R.color.transparent)
                .setActionBarColor(Color.parseColor("#F44336"), Color.parseColor("#F44336"), false)
                .setActionBarTitleColor(Color.parseColor("#ffffff"))
                .startAlbumWithOnActivityResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            //mSelected = Matisse.obtainResult(data);
            mSelected = data.getParcelableArrayListExtra(FishBun.INTENT_PATH);
            Log.d("RESPONSE_IMAGE_URI",mSelected.get(0).toString());
            sSelectedPath = getImage(mSelected.get(0));
            Log.d("RESPONSE_IMAGE_ORIGINAL_PATH",sSelectedPath);
            profileIv.setImageURI(mSelected.get(0));
            sChangeImage = "Yes";
        }
    }

    private String getImage(Uri uri) {
        if (uri != null) {
            String path = null;
            String[] s_array = {MediaStore.Images.Media.DATA};
            Cursor c = managedQuery(uri, s_array, null, null, null);
            int id = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (c.moveToFirst()) {
                do {
                    path = c.getString(id);
                }
                while (c.moveToNext());
                //c.close();
                if (path != null) {
                    return path;
                }
            }
        }
        return "";
    }

    private void doDeleteRetrofit() {
        Call<GetSignupData> call = apiInterface.getDeleteProfileData(sp.getString(ConstantSp.USERID,""));
        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().clear().commit();
                        new ToastCommonMethod(ProfileActivity.this, MainActivity.class);
                        finish();
                    }
                    else{
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(ProfileActivity.this,ConstantSp.SERVER_ERROR_MESSAGE+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(ProfileActivity.this,t.getMessage());
            }
        });
    }

    private void doUpdateImageRetrofit() {
        RequestBody userPart = RequestBody.create(MultipartBody.FORM, sp.getString(ConstantSp.USERID, ""));
        RequestBody namePart = RequestBody.create(MultipartBody.FORM, edttxt_name.getText().toString());
        RequestBody emailPart = RequestBody.create(MultipartBody.FORM, edttxt_email.getText().toString());
        RequestBody contactPart = RequestBody.create(MultipartBody.FORM, edttxt_mobile.getText().toString());
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM, edttxt_regisPass.getText().toString());
        RequestBody genderPart = RequestBody.create(MultipartBody.FORM, sGender);

        File file = new File(sSelectedPath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        Call<UpdateProfileImageData> call = apiInterface.updateProfileImageData(userPart,namePart,emailPart,contactPart,passwordPart,genderPart,filePart);
        call.enqueue(new Callback<UpdateProfileImageData>() {
            @Override
            public void onResponse(Call<UpdateProfileImageData> call, Response<UpdateProfileImageData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().putString(ConstantSp.NAME,edttxt_name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,edttxt_email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,edttxt_mobile.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        sp.edit().putString(ConstantSp.PROFILE,response.body().ProfileImage).commit();
                        setData(false);
                    }
                    else{
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(ProfileActivity.this,ConstantSp.SERVER_ERROR_MESSAGE+response.code());
                }
            }

            @Override
            public void onFailure(Call<UpdateProfileImageData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(ProfileActivity.this,t.getMessage());
            }
        });
    }

    private void doUpdateRetrofit() {
        Call<GetSignupData> call = apiInterface.getUpdateProfileData(sp.getString(ConstantSp.USERID,""),edttxt_name.getText().toString(),edttxt_email.getText().toString(),edttxt_mobile.getText().toString(),edttxt_regisPass.getText().toString(),sGender);
        call.enqueue(new Callback<GetSignupData>() {
            @Override
            public void onResponse(Call<GetSignupData> call, Response<GetSignupData> response) {
                pd.dismiss();
                if(response.code()==200){
                    if(response.body().status){
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                        sp.edit().putString(ConstantSp.NAME,edttxt_name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL,edttxt_email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT,edttxt_mobile.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD,"").commit();
                        sp.edit().putString(ConstantSp.GENDER,sGender).commit();
                        setData(false);
                    }
                    else{
                        new ToastCommonMethod(ProfileActivity.this,response.body().message);
                    }
                }
                else{
                    new ToastCommonMethod(ProfileActivity.this,ConstantSp.SERVER_ERROR_MESSAGE+response.code());
                }
            }

            @Override
            public void onFailure(Call<GetSignupData> call, Throwable t) {
                pd.dismiss();
                new ToastCommonMethod(ProfileActivity.this,t.getMessage());
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

        if(sp.getString(ConstantSp.PROFILE,"").equalsIgnoreCase("")){
            profileIv.setImageResource(R.drawable.beauty);
        }
        else{
            Glide.with(ProfileActivity.this).load(sp.getString(ConstantSp.PROFILE,"")).placeholder(R.drawable.beauty).into(profileIv);
        }

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