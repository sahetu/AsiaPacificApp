package asiapacific.app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CategoryDetailActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        name = findViewById(R.id.category_detail_name);
        imageView = findViewById(R.id.category_detail_image);

        Bundle bundle = getIntent().getExtras();
        name.setText(bundle.getString("NAME"));
        imageView.setImageResource(bundle.getInt("IMAGE"));
    }
}