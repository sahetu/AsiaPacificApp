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

import java.util.ArrayList;

public class RecyclerSecondAdapter extends RecyclerView.Adapter<RecyclerSecondAdapter.MyHolder> {

    Context context;
    ArrayList<CustomList> arrayList;

    public RecyclerSecondAdapter(Context context, ArrayList<CustomList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RecyclerSecondAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_recycler,parent,false);
        return new RecyclerSecondAdapter.MyHolder(view);
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
    public void onBindViewHolder(@NonNull RecyclerSecondAdapter.MyHolder holder, int position) {
        holder.name.setText(arrayList.get(position).getName());
        holder.imageView.setImageResource(arrayList.get(position).getImage());
        holder.linearLayout.setBackgroundColor(Color.parseColor(arrayList.get(position).getColor()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("NAME",arrayList.get(position).getName());
                bundle.putInt("IMAGE",arrayList.get(position).getImage());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
