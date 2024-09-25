package asiapacific.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class ActivityToFragmentActivity extends AppCompatActivity {

    Button openFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_fragment);

        openFragment = findViewById(R.id.open_fragment);

        openFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(ActivityToFragmentActivity.this, SplashActivity.class);
                startActivity(intent);*/
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.activity_relative,new DemoFragment()).addToBackStack("").commit();
            }
        });

    }

}