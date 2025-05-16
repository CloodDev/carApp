package com.example.carapp;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

        String details = car.year + ", " + car.color + ", " + car.price + " PLN";
        if (car.isRented && car.rentedByPesel != null) {
            details += "\nRented by: " + car.rentedByPesel;
        }
        holder.details.setText(details);

        if (car.isRented) {
            holder.rentButton.setEnabled(false);
            holder.rentButton.setText("Rented");
        } else {
            holder.rentButton.setEnabled(true);
            holder.rentButton.setText("Rent");
        }

        holder.rentButton.setOnClickListener(v -> {
            if (!car.isRented) {
                showPeselDialog(v.getContext(), car, () -> notifyItemChanged(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, details;
        Button rentButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.carImage);
            title = itemView.findViewById(R.id.carTitle);
            details = itemView.findViewById(R.id.carDetails);
            rentButton = itemView.findViewById(R.id.rentButton);
        }
    }

    private void showPeselDialog(Context context, MainActivity.Car car, Runnable onRented) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_rent, null);
        EditText peselInput = dialogView.findViewById(R.id.peselInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle("Rent Car")
            .setView(dialogView)
            .setPositiveButton("Confirm", null)
            .setNegativeButton("Cancel", null)
            .create();

        dialog.setOnShowListener(dialogInterface -> {
            Button confirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            confirm.setOnClickListener(view -> {
                String pesel = peselInput.getText().toString().trim();
                if (isValidPesel(pesel)) {
                    car.isRented = true;
                    car.rentedByPesel = pesel;
                    onRented.run();
                    dialog.dismiss();
                } else {
                    peselInput.setError("Invalid PESEL. Must be 11 digits.");
                }
            });
        });

        dialog.show();
    }

    private boolean isValidPesel(String pesel) {
        return !TextUtils.isEmpty(pesel) && pesel.length() == 11 && TextUtils.isDigitsOnly(pesel);
    }
}