package asiapacific.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDemoAdapter extends RecyclerView.Adapter<RecyclerDemoAdapter.MyHolder> {

    Context context;
    String[] nameArray;
    String[] colorArray;
    int[] imageArray;

    public RecyclerDemoAdapter(Context context, String[] nameArray, String[] colorArray, int[] imageArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.colorArray = colorArray;
        this.imageArray = imageArray;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler,parent,false);
        return new MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView name;
        ImageView imageView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout= itemView.findViewById(R.id.custom_recycler_linear);
            name = itemView.findViewById(R.id.custom_recycler_name);
            imageView = itemView.findViewById(R.id.custom_recycler_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(nameArray[position]);
        holder.imageView.setImageResource(imageArray[position]);
        holder.linearLayout.setBackgroundColor(Color.parseColor(colorArray[position]));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("NAME",nameArray[position]);
                bundle.putInt("IMAGE",imageArray[position]);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nameArray.length;
    }

}
