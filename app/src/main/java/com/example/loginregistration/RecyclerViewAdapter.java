package com.example.loginregistration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private String[] notifs;
    private String[] desc;
    private int[] images;
    private Context context;

    public RecyclerViewAdapter(Context context, String[] notifs, String[] desc, int[] images) {
        this.context = context;
        this.notifs = notifs;
        this.desc = desc;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View newView = layoutInflater.inflate(R.layout.notification_row, parent, false);

        return new MyViewHolder(newView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.notifTxt.setText(notifs[position]);
        holder.descTxt.setText(desc[position]);
        holder.notifImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView notifTxt, descTxt;
        ImageView notifImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            notifTxt = itemView.findViewById(R.id.notifTxt);
            descTxt = itemView.findViewById(R.id.descTxt);
            notifImage = itemView.findViewById(R.id.notifImage);

        }
    }
}
