package com.example.primeraapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.myViewHolder> {

    private ArrayList<MyModel> models;

    public MyAdapter(ArrayList<MyModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_item_list, parent, false);


        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        final MyModel myModel = models.get(position);

        Picasso.get()
                .load(myModel.getImageRes())
                        .into(holder.ImageView);
        holder.Symbol.setText(models.get(position).getSymbol());
        holder.Name.setText(models.get(position).getName());
        holder.Price.setText(models.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        ImageView ImageView;
        TextView Symbol, Name, Price;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.coinLogo);
            Symbol = itemView.findViewById(R.id.textViewSymbol);
            Name = itemView.findViewById(R.id.textViewCoinName);
            Price = itemView.findViewById(R.id.textViewPrice);
        }
    }
}
