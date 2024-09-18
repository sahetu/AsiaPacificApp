package asiapacific.app;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomListSecondActivity extends AppCompatActivity {

    GridView gridView;
    String[] nameArray = {"Fashion","Beauty","Electronics","Jewellery","Footwear","Toys","Furniture","Mobiles"};
    String[] colorArray = {"#6DD6C7","#FCA88B","#9CBEF5","#F59FD5","#BCA1F1","#8DC6D3","#A1D59B","#F4E168"};
    int[] imageArray = {R.drawable.fashion,R.drawable.beauty,R.drawable.electronics,R.drawable.jewelry,R.drawable.footwear,R.drawable.toys,R.drawable.furniture,R.drawable.smartphone};

    ArrayList<CustomList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list_second);

        gridView = findViewById(R.id.custom_list_second_grid);

        arrayList = new ArrayList<>();
        for(int i=0; i<nameArray.length;i++){
            CustomList list = new CustomList();
            list.setName(nameArray[i]);
            list.setColor(colorArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
        }

        CustomListSecondAdapter adapter = new CustomListSecondAdapter(CustomListSecondActivity.this,arrayList);
        gridView.setAdapter(adapter);
    }
}