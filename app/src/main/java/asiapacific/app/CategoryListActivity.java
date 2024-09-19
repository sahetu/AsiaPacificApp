package asiapacific.app;

import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class CategoryListActivity extends AppCompatActivity {

    GridView gridView;
    String[] nameArray = {"Fashion","Beauty","Electronics","Jewellery","Footwear","Toys","Furniture","Mobiles"};
    String[] colorArray = {"#6DD6C7","#FCA88B","#9CBEF5","#F59FD5","#BCA1F1","#8DC6D3","#A1D59B","#F4E168"};
    int[] imageArray = {R.drawable.fashion,R.drawable.beauty,R.drawable.electronics,R.drawable.jewelry,R.drawable.footwear,R.drawable.toys,R.drawable.furniture,R.drawable.smartphone};
    ArrayList<CustomList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

        gridView = findViewById(R.id.category_grid);
        arrayList = new ArrayList<>();
        for(int i=0;i<nameArray.length;i++){
            CustomList list = new CustomList();
            list.setName(nameArray[i]);
            list.setColor(colorArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
        }
        CategoryAdapter adapter = new CategoryAdapter(CategoryListActivity.this,arrayList);
        gridView.setAdapter(adapter);
    }
}