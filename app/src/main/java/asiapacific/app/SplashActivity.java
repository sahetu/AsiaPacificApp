package asiapacific.app;

import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        imageView = findViewById(R.id.splash_image);

        AlphaAnimation animation = new AlphaAnimation(0,1);
        animation.setDuration(2700);
        //animation.setRepeatCount(2);
        imageView.startAnimation(animation);

        //Thread.sleep(2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new ToastCommonMethod(SplashActivity.this,CategoryListActivity.class);
                finish();
            }
        },3000);
    }
}