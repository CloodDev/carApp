package com.example.carapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CarGalleryActivity extends AppCompatActivity {

    // Car model class
    private static class Car {
        int id;
        int src;
        String alt;
        String color;
        int year;
        int price;

        public Car(int id, int src, String alt, String color, int year, int price) {
            this.id = id;
            this.src = src;
            this.alt = alt;
            this.color = color;
            this.year = year;
            this.price = price;
        }
    }

    // List of cars
    private List<Car> cars = new ArrayList<>();
    private List<Car> filteredCars = new ArrayList<>();
    private GridLayout gallery;
    
    // Filters
    private Spinner colorSpinner;
    private SeekBar yearFromSeekBar, yearToSeekBar;
    private TextView yearRangeTextView;
    private EditText priceFromEditText, priceToEditText;
    
    private int yearFrom = 2000;
    private int yearTo = 2024;
    private int priceFrom = 0;
    private int priceTo = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_gallery);
        
        initCarData();
        initViews();
        setupFilters();
        filterCars();
        updateGallery();
    }

    private void initCarData() {
        // Initialize car data
        cars.add(new Car(1, R.drawable.bmw_biale, "BMW Białe", "Biały", 2016, 200));
        cars.add(new Car(2, R.drawable.bmw_czarne, "BMW Czarne", "Czarny", 2000, 250));
        cars.add(new Car(3, R.drawable.bmw_czerwone, "BMW Czerwone", "Czerwony", 2010, 220));
        cars.add(new Car(4, R.drawable.honda_biala, "Honda Biała", "Biały", 2018, 180));
        cars.add(new Car(5, R.drawable.honda_czarna, "Honda Czarna", "Czarny", 2024, 230));
        cars.add(new Car(6, R.drawable.honda_czerwona, "Honda Czerwona", "Czerwony", 2008, 210));
        cars.add(new Car(7, R.drawable.toyota_czarna, "Toyota Czarna", "Czarny", 2018, 300));
        cars.add(new Car(8, R.drawable.toyota_czerwona, "Toyota Czerwona", "Czerwony", 2002, 270));
        cars.add(new Car(9, R.drawable.bmw_silver, "BMW Silver", "Srebrny", 2000, 320));
    }

    private void initViews() {
        // Initialize views
        colorSpinner = findViewById(R.id.colorSpinner);
        yearFromSeekBar = findViewById(R.id.yearFromSeekBar);
        yearToSeekBar = findViewById(R.id.yearToSeekBar);
        yearRangeTextView = findViewById(R.id.yearRangeTextView);
        priceFromEditText = findViewById(R.id.priceFromEditText);
        priceToEditText = findViewById(R.id.priceToEditText);
        gallery = findViewById(R.id.carGallery);
        
        // Set initial text values
        priceFromEditText.setText(String.valueOf(priceFrom));
        priceToEditText.setText(String.valueOf(priceTo));
        yearRangeTextView.setText(yearFrom + " - " + yearTo);
    }
    
    private void setupFilters() {
        // Setup Color Spinner
        String[] colors = {"Wszystkie", "Biały", "Czarny", "Czerwony", "Srebrny"};
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSpinner.setAdapter(colorAdapter);
        
        // Filter when color is selected
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterCars();
                updateGallery();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        
        // Setup Year SeekBars
        yearFromSeekBar.setMax(24); // 2000-2024
        yearToSeekBar.setMax(24);  // 2000-2024
        yearToSeekBar.setProgress(24); // Set to 2024
        
        yearFromSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yearFrom = 2000 + progress;
                if (yearFrom > yearTo) {
                    yearTo = yearFrom;
                    yearToSeekBar.setProgress(progress);
                }
                yearRangeTextView.setText(yearFrom + " - " + yearTo);
                filterCars();
                updateGallery();
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        yearToSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yearTo = 2000 + progress;
                if (yearTo < yearFrom) {
                    yearFrom = yearTo;
                    yearFromSeekBar.setProgress(progress);
                }
                yearRangeTextView.setText(yearFrom + " - " + yearTo);
                filterCars();
                updateGallery();
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        
        // Setup price filter
        Button applyPriceButton = findViewById(R.id.applyPriceButton);
        applyPriceButton.setOnClickListener(v -> {
            try {
                priceFrom = Integer.parseInt(priceFromEditText.getText().toString());
                priceTo = Integer.parseInt(priceToEditText.getText().toString());
                filterCars();
                updateGallery();
            } catch (NumberFormatException e) {
                // Handle invalid input
            }
        });
    }
    
    private void filterCars() {
        filteredCars.clear();
        
        String selectedColor = colorSpinner.getSelectedItem().toString();
        
        for (Car car : cars) {
            if ((selectedColor.equals("Wszystkie") || car.color.equals(selectedColor)) &&
                car.year >= yearFrom && car.year <= yearTo &&
                car.price >= priceFrom && car.price <= priceTo) {
                filteredCars.add(car);
            }
        }
    }
    
    private void updateGallery() {
        gallery.removeAllViews();
        
        for (Car car : filteredCars) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageResource(car.src);
            imageView.setContentDescription(car.alt);
            gallery.addView(imageView);
        }
    }
}
