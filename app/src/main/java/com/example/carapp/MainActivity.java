package com.example.carapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    
    // Car image resources
    // Car image resources grouped by brand
    private final int[] hondaImages = {
        R.drawable.honda_biala, 
        R.drawable.honda_czarna, 
        R.drawable.honda_czerwona,
    };
    
    private final int[] bmwImages = {
        R.drawable.bmw_biale,
        R.drawable.bmw_czarne,
        R.drawable.bmw_czerwone,
        R.drawable.bmw_silver,
    };
    
    private final int[] toyotaImages = {
        R.drawable.toyota_czarna, 
        R.drawable.toyota_czerwona, 
    };
    
    // Default images to show initially
    private int[] carImages = bmwImages;
    
    private ImageView mainCarImageView;
    private ImageView[] thumbnailViews;
    private Spinner brandSpinner, colorSpinner;
    private SeekBar yearSeekBar;
    private TextView yearRangeTextView;
    private EditText yearFromEditText, yearToEditText, priceFromEditText, priceToEditText;
    private Button searchButton;
    
    private final int MIN_YEAR = 1990;
    private final int MAX_YEAR = 2023;
    private int currentYear = 2010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        initViews();
        setupSpinners();
        setupYearSeekBar();
        setupCarImages();
        setupSearchButton();
    }
    
    private void initViews() {
        mainCarImageView = findViewById(R.id.mainCarImageView);
        // Get all potential thumbnail ImageViews
        // Initialize array to hold thumbnail views
        thumbnailViews = new ImageView[5]; // Max capacity for thumbnails
        
        // Dynamic thumbnail finding - will only be used if they exist
        for (int i = 1; i <= 5; i++) {
            int resId = getResources().getIdentifier(
            "carImageView" + i, "id", getPackageName());
            if (resId != 0) {
            thumbnailViews[i-1] = findViewById(resId);
            }
        }
        
        brandSpinner = findViewById(R.id.brandSpinner);
        colorSpinner = findViewById(R.id.colorSpinner);
        yearSeekBar = findViewById(R.id.seekBar);
        yearRangeTextView = findViewById(R.id.yearRangeTextView);
        yearFromEditText = findViewById(R.id.yearFromEditText);
        yearToEditText = findViewById(R.id.yearToEditText);
        priceFromEditText = findViewById(R.id.priceFromEditText);
        priceToEditText = findViewById(R.id.priceToEditText);
        searchButton = findViewById(R.id.searchButton);
        
        // Set initial values
        yearFromEditText.setText(String.valueOf(MIN_YEAR));
        yearToEditText.setText(String.valueOf(MAX_YEAR));
    }
    
    private void setupSpinners() {
        // Brand spinner
        String[] brands = {"Wszystkie marki", "Honda", "BMW","Toyota"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brands);
        brandSpinner.setAdapter(brandAdapter);
        
        // Color spinner
        String[] colors = {"Wszystkie kolory", "Czarny", "Biały", "Czerwony", "Niebieski", "Srebrny"};
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, colors);
        colorSpinner.setAdapter(colorAdapter);
    }
    
    private void setupYearSeekBar() {
        yearSeekBar.setMax(MAX_YEAR - MIN_YEAR);
        yearSeekBar.setProgress(currentYear - MIN_YEAR);
        
        yearSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                currentYear = MIN_YEAR + progress;
                yearRangeTextView.setText(MIN_YEAR + " - " + currentYear);
                yearToEditText.setText(String.valueOf(currentYear));
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
    
    private void setupCarImages() {
        updateCarImagesDisplay();
    }
    
    private void updateCarImagesDisplay() {
        // Load initial main image
        if (carImages.length > 0) {
            mainCarImageView.setImageResource(carImages[0]);
        }
        
        // Set up thumbnail images and click listeners
        for (int i = 0; i < thumbnailViews.length; i++) {
            if (thumbnailViews[i] != null) {
                if (i < carImages.length) {
                    final int index = i;
                    thumbnailViews[i].setImageResource(carImages[i]);
                    thumbnailViews[i].setOnClickListener(v -> {
                        mainCarImageView.setImageResource(carImages[index]);
                    });
                    thumbnailViews[i].setVisibility(View.VISIBLE);
                } else {
                    thumbnailViews[i].setVisibility(View.GONE);
                }
            }
        }
    }
    
    private void setupSearchButton() {
        searchButton.setOnClickListener(v -> {
            String brand = brandSpinner.getSelectedItem().toString();
            String color = colorSpinner.getSelectedItem().toString();
            
            String yearFrom = yearFromEditText.getText().toString();
            String yearTo = yearToEditText.getText().toString();
            String priceFrom = priceFromEditText.getText().toString();
            String priceTo = priceToEditText.getText().toString();
            
            StringBuilder searchParams = new StringBuilder();
            searchParams.append("Wyszukiwanie:\n");
            
            // Update car images based on selected brand
            if (brand.equals("Honda")) {
                carImages = hondaImages;
            } else if (brand.equals("BMW")) {
                carImages = bmwImages;
            } else if (brand.equals("Toyota")) {
                carImages = toyotaImages;
            }
            
            // Refresh images display using the new method
            updateCarImagesDisplay();
            
            searchParams.append("Marka: ").append(brand).append("\n");
            searchParams.append("Kolor: ").append(color).append("\n");
            searchParams.append("Rok od: ").append(yearFrom).append("\n");
            searchParams.append("Rok do: ").append(yearTo).append("\n");
            
            if (!priceFrom.isEmpty()) {
                searchParams.append("Cena od: ").append(priceFrom).append(" PLN\n");
            }
            
            if (!priceTo.isEmpty()) {
                searchParams.append("Cena do: ").append(priceTo).append(" PLN");
            }
            
            Toast.makeText(MainActivity.this, searchParams.toString(), Toast.LENGTH_LONG).show();
            
            // Here you would typically start a new activity or fragment to show search results
        });
    }
}

