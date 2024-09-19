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

import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {

    Context context;
    ArrayList<CustomList> arrayList;

    public CategoryAdapter(Context context, ArrayList<CustomList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
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

        linearLayout.setBackgroundColor(Color.parseColor(arrayList.get(i).getColor()));

        name.setText(arrayList.get(i).getName());
        imageView.setImageResource(arrayList.get(i).getImage());

        return view;
    }
}
