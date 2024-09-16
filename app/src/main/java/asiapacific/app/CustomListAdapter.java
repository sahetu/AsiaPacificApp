package asiapacific.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {

    Context context;
    String[] nameArray;
    String[] colorArray;
    int[] imageArray;

    public CustomListAdapter(Context context, String[] nameArray, String[] colorArray, int[] imageArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.colorArray = colorArray;
        this.imageArray = imageArray;
    }

    @Override
    public int getCount() {
        return nameArray.length;
    }

    @Override
    public Object getItem(int i) {
        return nameArray[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.custom_list,null);
        ImageView imageView = view.findViewById(R.id.custom_list_image);
        TextView name = view.findViewById(R.id.custom_list_name);
        LinearLayout linearLayout = view.findViewById(R.id.custom_list_linear);

        linearLayout.setBackgroundColor(Color.parseColor(colorArray[i]));

        name.setText(nameArray[i]);
        imageView.setImageResource(imageArray[i]);

        return view;
    }
}
