package asiapacific.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class RazorpayDemoActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    EditText amount;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay_demo);

        amount = findViewById(R.id.razorpay_demo_amount);
        submit = findViewById(R.id.razorpay_demo_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().equalsIgnoreCase("")){
                    amount.setError("Amount Required");
                }
                else if(amount.getText().toString().trim().equals("0")){
                    amount.setError("Valid Amount Required");
                }
                else{
                    startPayment();
                }
            }
        });

    }

    private void startPayment() {
        final Activity activity = this;

        final Checkout co = new Checkout();

        co.setKeyID("rzp_test_xsiOz9lYtWKHgF");
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Desc");
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(amount.getText().toString())*100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@gmail.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(RazorpayDemoActivity.this);
            builder.setTitle("Error In Payment");
            builder.setMessage(e.getMessage());
            builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RazorpayDemoActivity.this);
        builder.setTitle("Payment Success");
        builder.setMessage(s);
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RazorpayDemoActivity.this);
        builder.setTitle("Payment Failed");
        builder.setMessage(s);
        builder.setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
}