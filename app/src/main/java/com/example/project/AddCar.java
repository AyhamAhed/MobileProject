package com.example.project;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class AddCar extends AppCompatActivity {
    private EditText companyEditText, seatsEditText, priceEditText, dailyPriceEditText, monthlyPriceEditText, mileageEditText, colorEditText, modelEditText, yearEditText, imageURLEditText;
    private AppCompatButton saveButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_car);

        companyEditText = findViewById(R.id.company);
        seatsEditText = findViewById(R.id.seats);
        priceEditText = findViewById(R.id.price);
        dailyPriceEditText = findViewById(R.id.Dailyprice);
        monthlyPriceEditText = findViewById(R.id.Monthlyprice);
        mileageEditText = findViewById(R.id.mileage);
        colorEditText = findViewById(R.id.color);
        modelEditText = findViewById(R.id.model);
        yearEditText = findViewById(R.id.year);
        imageURLEditText = findViewById(R.id.imageURL);

        saveButton = findViewById(R.id.add);
        resetButton = findViewById(R.id.reset);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCar();

            }
        });



        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

    }

    private void saveCar() {
        String company = companyEditText.getText().toString().trim();
        String seats = seatsEditText.getText().toString().trim();
        String price = priceEditText.getText().toString().trim();
        String dailyPrice = dailyPriceEditText.getText().toString().trim();
        String monthlyPrice = monthlyPriceEditText.getText().toString().trim();
        String mileage = mileageEditText.getText().toString().trim();
        String color = colorEditText.getText().toString().trim();
        String model = modelEditText.getText().toString().trim();
        String year = yearEditText.getText().toString().trim();
        String imageURL = imageURLEditText.getText().toString().trim();

        if (company.isEmpty() || seats.isEmpty() || price.isEmpty() || dailyPrice.isEmpty() || monthlyPrice.isEmpty() || mileage.isEmpty() || color.isEmpty() || model.isEmpty() || year.isEmpty() || imageURL.isEmpty()) {
            Toast.makeText(AddCar.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        String url = "http://10.0.2.2/Project/car.php";
        RequestQueue queue = Volley.newRequestQueue(AddCar.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    String status = res.getString("status");
                    if(status.equals("success")){
                        resetFields();
                    }
                    String message = res.getString("message");
                    Toast.makeText(AddCar.this, message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(AddCar.this, "Error: " + volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("company", company);
                params.put("SeatsNumber", seats);
                params.put("price", price);
                params.put("model_year", year);
                params.put("MonthlyPrice", monthlyPrice);
                params.put("Mileage", mileage);
                params.put("DailyPrice", dailyPrice);
                params.put("color", color);
                params.put("image", imageURL);
                return params;
            }
        };

        queue.add(request);


    }

    private void resetFields() {
        companyEditText.setText("");
        seatsEditText.setText("");
        priceEditText.setText("");
        dailyPriceEditText.setText("");
        monthlyPriceEditText.setText("");
        mileageEditText.setText("");
        colorEditText.setText("");
        modelEditText.setText("");
        yearEditText.setText("");
        imageURLEditText.setText("");
    }
}