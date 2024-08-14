package asiapacific.app;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ListDemoActivity extends AppCompatActivity {

    GridView listView;
    ArrayList<String> arrayList;

    AutoCompleteTextView autoCompleteTextView;
    ArrayList<String> autoNameArray;

    MultiAutoCompleteTextView multiAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demo);

        listView = findViewById(R.id.listview);

        arrayList = new ArrayList<>();
        arrayList.add("Ahmedabad");
        arrayList.add("Vadodara");
        arrayList.add("Surat");
        arrayList.add("Demo");
        arrayList.add("Rajkt");
        arrayList.add("Gandhinagar");

        arrayList.remove(3);
        arrayList.set(3,"Rajkot");

        ArrayAdapter adapter = new ArrayAdapter(ListDemoActivity.this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ToastCommonMethod(ListDemoActivity.this,arrayList.get(i));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new ToastCommonMethod(ListDemoActivity.this,arrayList.get(i));
                return false;
            }
        });

        autoCompleteTextView = findViewById(R.id.autocomplete);
        autoNameArray = new ArrayList<>();
        autoNameArray.add("Prince");
        autoNameArray.add("Meet");
        autoNameArray.add("Mihir");
        autoNameArray.add("Parv");
        autoNameArray.add("Kenil");
        autoNameArray.add("Prayag");

        ArrayAdapter autoAdapter = new ArrayAdapter(ListDemoActivity.this, android.R.layout.simple_list_item_1,autoNameArray);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(autoAdapter);

        multiAutoCompleteTextView = findViewById(R.id.multiautocomplete);
        ArrayAdapter multiAdapter = new ArrayAdapter(ListDemoActivity.this, android.R.layout.simple_list_item_1,autoNameArray);
        multiAutoCompleteTextView.setThreshold(1);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        multiAutoCompleteTextView.setAdapter(multiAdapter);

    }
}