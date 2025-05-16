package com.example.carapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.slider.RangeSlider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static class Car {
        int id;
        int src;
        String alt;
        String color;
        int year;
        int price;
        boolean isRented = false;
        String rentedByPesel = null;

        public Car(int id, int src, String alt, String color, int year, int price) {
            this.id = id;
            this.src = src;
            this.alt = alt;
            this.color = color;
            this.year = year;
            this.price = price;
        }
    }

    private final List<Car> cars = new ArrayList<>();
    private final List<Car> filteredCars = new ArrayList<>();

    private Spinner colorSpinner;
    private RangeSlider yearRangeSlider;
    private TextView yearRangeText;
    private EditText minPriceInput, maxPriceInput;
    private TextView priceRangeText;
    private RecyclerView carsRecyclerView;

    private int yearFrom = 2000;
    private int yearTo = 2024;
    private int priceFrom = 0;
    private int priceTo = 500;

    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCarData();
        initViews();
        setupFilters();
        filterCars();
    }

    private void initCarData() {
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
        colorSpinner = findViewById(R.id.colorSpinner);
        yearRangeSlider = findViewById(R.id.yearRangeSlider);
        yearRangeText = findViewById(R.id.yearRangeText);
        minPriceInput = findViewById(R.id.minPriceInput);
        maxPriceInput = findViewById(R.id.maxPriceInput);
        priceRangeText = findViewById(R.id.priceRangeText);
        carsRecyclerView = findViewById(R.id.carsRecyclerView);

        carsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        carAdapter = new CarAdapter(filteredCars);
        carsRecyclerView.setAdapter(carAdapter);

        minPriceInput.setText(String.valueOf(priceFrom));
        maxPriceInput.setText(String.valueOf(priceTo));
        priceRangeText.setText(priceFrom + " - " + priceTo + " PLN");
    }

    private void setupFilters() {
        String[] colors = {"Wszystkie", "Biały", "Czarny", "Czerwony", "Srebrny"};
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setSelection(0);
        colorSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                filterCars();
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        yearRangeSlider.setValueFrom(2000);
        yearRangeSlider.setValueTo(2024);
        yearRangeSlider.setValues((float) yearFrom, (float) yearTo);

        yearRangeSlider.addOnChangeListener((slider, value, fromUser) -> {
            yearFrom = Math.round(slider.getValues().get(0));
            yearTo = Math.round(slider.getValues().get(1));
            yearRangeText.setText(yearFrom + " - " + yearTo);
            filterCars();
        });

        TextWatcher priceWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                try {
                    priceFrom = Integer.parseInt(minPriceInput.getText().toString());
                } catch (NumberFormatException e) {
                    priceFrom = 0;
                }
                try {
                    priceTo = Integer.parseInt(maxPriceInput.getText().toString());
                } catch (NumberFormatException e) {
                    priceTo = 500;
                }
                priceRangeText.setText(priceFrom + " - " + priceTo + " PLN");
                filterCars();
            }
        };
        minPriceInput.addTextChangedListener(priceWatcher);
        maxPriceInput.addTextChangedListener(priceWatcher);
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
        carAdapter.notifyDataSetChanged();
    }
}