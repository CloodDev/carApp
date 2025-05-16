package com.example.carapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private final List<MainActivity.Car> cars;

    public CarAdapter(List<MainActivity.Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        MainActivity.Car car = cars.get(position);
        holder.image.setImageResource(car.src);
        holder.title.setText(car.alt);
        holder.details.setText(car.year + ", " + car.color + ", " + car.price + " PLN");
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, details;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.carImage);
            title = itemView.findViewById(R.id.carTitle);
            details = itemView.findViewById(R.id.carDetails);
        }
    }
}