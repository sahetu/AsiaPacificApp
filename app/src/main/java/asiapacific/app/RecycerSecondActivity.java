package asiapacific.app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class RecycerSecondActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] nameArray = {"Fashion","Beauty","Electronics","Jewellery","Footwear","Toys","Furniture","Mobiles"};
    String[] colorArray = {"#6DD6C7","#FCA88B","#9CBEF5","#F59FD5","#BCA1F1","#8DC6D3","#A1D59B","#F4E168"};
    int[] imageArray = {R.drawable.fashion,R.drawable.beauty,R.drawable.electronics,R.drawable.jewelry,R.drawable.footwear,R.drawable.toys,R.drawable.furniture,R.drawable.smartphone};
    ArrayList<CustomList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycer_second);

        recyclerView = findViewById(R.id.recycler_second);

        //Set Data As a List
        //recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerDemoActivity.this));

        //Set Data As a Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //Set Data As a Horizontal
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();
        for(int i=0; i<nameArray.length;i++){
            CustomList list = new CustomList();
            list.setName(nameArray[i]);
            list.setColor(colorArray[i]);
            list.setImage(imageArray[i]);
            arrayList.add(list);
        }

        RecyclerSecondAdapter adapter = new RecyclerSecondAdapter(RecycerSecondActivity.this,arrayList);
        recyclerView.setAdapter(adapter);

    }
}