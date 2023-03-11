package com.example.vacationhome.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationhome.R;
import com.example.vacationhome.model.Ad;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAd extends RecyclerView.Adapter<AdapterAd.MyViewHolder> {

    private List<Ad> anuncioList;
    private OnClick onClick;

    public AdapterAd(List<Ad> anuncioList, OnClick onClick) {
        this.anuncioList = anuncioList;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
Ad anuncio = anuncioList.get(position);

        Picasso.get().load(anuncio.getUrlImage()).into(holder.image_ad);
        holder.text_title.setText(anuncio.getTitle());
        holder.text_description.setText(anuncio.getDescription());


        holder.itemView.setOnClickListener(view -> onClick.OnClickListener(anuncio));
    }

    @Override
    public int getItemCount() {
        return anuncioList.size();
    }

    public interface OnClick {

            public void OnClickListener(Ad ad);

    }

   static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView image_ad;
        TextView text_title, text_description, text_data;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image_ad = itemView.findViewById(R.id.image_ad);
            text_title = itemView.findViewById(R.id.text_title);
            text_description = itemView.findViewById(R.id.text_description);
            text_data = itemView.findViewById(R.id.text_data);
        }
    }
}
