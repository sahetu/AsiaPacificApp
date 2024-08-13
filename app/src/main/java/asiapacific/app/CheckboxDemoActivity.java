package asiapacific.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CheckboxDemoActivity extends AppCompatActivity {

    CheckBox ahmedabad,vadodara,surat,rajkot,gandhinagar;
    Button show;
    StringBuilder sb;

    Spinner spinner;
    String[] cityArray = {"Select City","Ahmedabad","Vadodara","Rajkot","Surat","Gandhinagar"};
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox_demo);

        ahmedabad = findViewById(R.id.checkbox_ahmedabad);
        vadodara = findViewById(R.id.checkbox_vadodara);
        surat = findViewById(R.id.checkbox_surat);
        rajkot = findViewById(R.id.checkbox_rajkot);
        gandhinagar = findViewById(R.id.checkbox_gandhinagar);

        show = findViewById(R.id.checkbox_show);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sb = new StringBuilder();
                //String sCity = "";
                if(ahmedabad.isChecked()){
                    //new ToastCommonMethod(CheckboxDemoActivity.this, ahmedabad.getText().toString());
                    sb.append(ahmedabad.getText().toString()+"\n");
                }
                if(vadodara.isChecked()){
                    sb.append(vadodara.getText().toString()+"\n");
                    //new ToastCommonMethod(CheckboxDemoActivity.this, vadodara.getText().toString());
                }
                if(surat.isChecked()){
                    sb.append(surat.getText().toString()+"\n");
                    //new ToastCommonMethod(CheckboxDemoActivity.this, surat.getText().toString());
                }
                if(rajkot.isChecked()){
                    sb.append(rajkot.getText().toString()+"\n");
                    //new ToastCommonMethod(CheckboxDemoActivity.this, rajkot.getText().toString());
                }
                if(gandhinagar.isChecked()){
                    sb.append(gandhinagar.getText().toString()+"\n");
                    //new ToastCommonMethod(CheckboxDemoActivity.this, gandhinagar.getText().toString());
                }

                if(sb.toString().trim().equals("")){
                    new ToastCommonMethod(CheckboxDemoActivity.this,"Please Select City");
                }
                else{
                    new ToastCommonMethod(CheckboxDemoActivity.this, sb.toString());
                }
                /*else{
                    new ToastCommonMethod(CheckboxDemoActivity.this,"Please Select City");
                }*/
            }
        });

        /*ahmedabad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    new ToastCommonMethod(CheckboxDemoActivity.this, ahmedabad.getText().toString());
                }
            }
        });

        vadodara.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    new ToastCommonMethod(CheckboxDemoActivity.this, vadodara.getText().toString());
                }
            }
        });

        surat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    new ToastCommonMethod(CheckboxDemoActivity.this, surat.getText().toString());
                }
            }
        });

        rajkot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    new ToastCommonMethod(CheckboxDemoActivity.this, rajkot.getText().toString());
                }
            }
        });

        gandhinagar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    new ToastCommonMethod(CheckboxDemoActivity.this, gandhinagar.getText().toString());
                }
            }
        });*/

        spinner = findViewById(R.id.spinner);
        arrayList = new ArrayList<>();
        /*for(int i=0;i<cityArray.length;i++){
            arrayList.add(cityArray[i]);
        }*/
        arrayList.add("Select City");
        arrayList.add("Ahmedabad");
        arrayList.add("Vadodara");
        arrayList.add("Surat");
        arrayList.add("Demo");
        arrayList.add("Rajkt");
        arrayList.add("Gandhinagar");

        arrayList.remove(4);
        arrayList.set(4,"Rajkot");

        ArrayAdapter adapter = new ArrayAdapter(CheckboxDemoActivity.this, android.R.layout.simple_list_item_1,arrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){

                }
                else {
                    //new ToastCommonMethod(CheckboxDemoActivity.this, cityArray[i]);
                    new ToastCommonMethod(CheckboxDemoActivity.this, arrayList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}