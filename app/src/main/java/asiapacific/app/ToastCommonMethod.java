package asiapacific.app;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ToastCommonMethod {

    ToastCommonMethod(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    ToastCommonMethod(Context context, Class<?> nextClass) {
        Intent intent = new Intent(context,nextClass);
        context.startActivity(intent);
    }

}
