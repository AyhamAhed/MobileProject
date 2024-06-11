package com.example.project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class carInfoForAdmin extends AppCompatActivity {

    private ImageView carImageView;
    private TextView carMakeModelTextView, carYearTextView, carPriceTextView, carFeaturesTextView, totalPriceTextView;
    private EditText dateEditText, daysEditText;
    private ImageButton datePickerButton;
    private Button confirmRentalButton;
    private Calendar calendar;
    private int dailyPrice;
    private int carId;
    private int accountId = 1; // Replace with actual user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        carImageView = findViewById(R.id.carImageView);
        carMakeModelTextView = findViewById(R.id.carMakeModelTextView);
        carYearTextView = findViewById(R.id.carYearTextView);
        carPriceTextView = findViewById(R.id.carPriceTextView);
        carFeaturesTextView = findViewById(R.id.carFeaturesTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        dateEditText = findViewById(R.id.dateEditText);
        daysEditText = findViewById(R.id.daysEditText);
        datePickerButton = findViewById(R.id.datePickerButton);
        confirmRentalButton = findViewById(R.id.confirmRentalButton);

        calendar = Calendar.getInstance();

        carId = getIntent().getIntExtra("car_id", -1);
        if (carId != -1) {
            fetchCarDetails(carId);
        } else {
            Toast.makeText(this, "Invalid car ID", Toast.LENGTH_SHORT).show();
        }

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        datePickerButton.setOnClickListener(v -> new DatePickerDialog(carInfoForAdmin.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        daysEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    calculateTotalPrice();
                }
            }
        });

        confirmRentalButton.setOnClickListener(v -> {
            if (validateInputs()) {
                String daysString = daysEditText.getText().toString();
                int days = Integer.parseInt(daysString);
                int totalPrice = days * dailyPrice;
                String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(calendar.getTime());
                Calendar endCalendar = (Calendar) calendar.clone();
                endCalendar.add(Calendar.DAY_OF_YEAR, days);
                String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(endCalendar.getTime());
                bookCar(carId, accountId, totalPrice, startDate, endDate);
            }
        });
    }

    private void fetchCarDetails(int carId) {
        String url = "http://10.0.2.2/Project/fetch_car_details.php?car_id=" + carId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CarDetailsResponse", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")) {
                        JSONObject carObject = jsonResponse.getJSONObject("data");
                        String company = carObject.getString("company");
                        String modelYear = carObject.getString("Model , year");
                        int mileage = carObject.getInt("Mileage");
                        int seatsNumber = carObject.getInt("Seats number");
                        int monthlyPrice = carObject.getInt("MonthlyPrice");
                        dailyPrice = carObject.getInt("DailyPrice");
                        int price = carObject.getInt("price");
                        String color = carObject.getString("color");
                        String statuss = carObject.getString("status");
                        String image = carObject.getString("image");

                        carMakeModelTextView.setText(company + " " + modelYear);
                        carYearTextView.setText("Year: " + modelYear);
                        carPriceTextView.setText("Price: $" + dailyPrice);
                        carFeaturesTextView.setText("Features: " + color + ", " + seatsNumber + " seats" + " => " + status);

                        String imageUrl = "http://10.0.2.2/" + image;
                        Picasso.get().load(imageUrl).into(carImageView);
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(carInfoForAdmin.this, "Failed to fetch car details: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(carInfoForAdmin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(carInfoForAdmin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; // In which you need to display your date
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void calculateTotalPrice() {
        String daysString = daysEditText.getText().toString();
        if (!daysString.isEmpty()) {
            int days = Integer.parseInt(daysString);
            int totalPrice = days * dailyPrice;
            totalPriceTextView.setText("Total Price: $" + totalPrice);
        }
    }

    private boolean validateInputs() {
        if (dateEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (daysEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter the number of days", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void bookCar(int carId, int accountId, int totalPrice, String startDate, String endDate) {
        String url = "http://10.0.2.2/Project/book_car.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BookCarResponse", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(carInfoForAdmin.this, "Car booked successfully!", Toast.LENGTH_SHORT).show();
                        // Optionally, navigate to booking history or another activity
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(carInfoForAdmin.this, "Booking failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(carInfoForAdmin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(carInfoForAdmin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("accountID", String.valueOf(accountId)); // Replace with the actual user ID
                params.put("carID", String.valueOf(carId));
                params.put("totalPrice", String.valueOf(totalPrice));
                params.put("startDate", startDate);
                params.put("endDate", endDate);
                params.put("status", "Booked");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
