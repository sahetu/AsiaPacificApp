package asiapacific.app;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CustomListActivity extends AppCompatActivity {

    GridView listView;
    String[] nameArray = {"Fashion","Beauty","Electronics","Jewellery","Footwear","Toys","Furniture","Mobiles"};
    String[] colorArray = {"#6DD6C7","#FCA88B","#9CBEF5","#F59FD5","#BCA1F1","#8DC6D3","#A1D59B","#F4E168"};
    int[] imageArray = {R.drawable.fashion,R.drawable.beauty,R.drawable.electronics,R.drawable.jewelry,R.drawable.footwear,R.drawable.toys,R.drawable.furniture,R.drawable.smartphone};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        listView = findViewById(R.id.custom_list);

        CustomListAdapter adapter = new CustomListAdapter(CustomListActivity.this,nameArray,colorArray,imageArray);
        listView.setAdapter(adapter);

    }
}