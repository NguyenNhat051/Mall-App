package com.example.mallapplication.adapter;

import static com.example.mallapplication.activity.GroceryItemActivity.GROCERY_ITEM_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mallapplication.GroceryItem;
import com.example.mallapplication.activity.GroceryItemActivity;
import com.example.mallapplication.R;

import java.util.ArrayList;

public class GroceryItemAdapter extends RecyclerView.Adapter<GroceryItemAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GroceryItem> items = new ArrayList<>();

    public GroceryItemAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<GroceryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grocery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.price.setText(String.valueOf(items.get(position).getPrice()) + "$");
        Glide.with(context)
                .asBitmap()
                .load(items.get(position).getImageUrl())
                .into(holder.imageView);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GroceryItemActivity.class);
                intent.putExtra(GROCERY_ITEM_KEY, items.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView parent;
        private ImageView imageView;
        private TextView price, name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            imageView = itemView.findViewById(R.id.image);
            price = itemView.findViewById(R.id.txtPrice);
            name = itemView.findViewById(R.id.txtName);
        }
    }

}
