package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class admin_home extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Car> carList = new ArrayList<>();
    private CarAdapter carAdapter; // Changed from carAdapterForAdmin to CarAdapter
    private static final String BASE_URL = "http://10.0.2.2/Project/fetch_cars.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        recyclerView = findViewById(R.id.view1);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(this, carList); // Changed adapter instantiation
        recyclerView.setAdapter(carAdapter);
        loadItems();
    }

    private void loadItems() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String statuss = jsonResponse.getString("status");

                            if (statuss.equals("success")) {
                                JSONArray carsArray = jsonResponse.getJSONArray("data");
                                carList.clear();
                                for (int i = 0; i < carsArray.length(); i++) {
                                    JSONObject carObject = carsArray.getJSONObject(i);

                                    int id = carObject.getInt("ID");
                                    String company = carObject.getString("company");
                                    int mileage = carObject.getInt("Mileage");
                                    int seats_number = carObject.getInt("Seats_number");
                                    int monthlyPrice = carObject.getInt("MonthlyPrice");
                                    int dailyPrice = carObject.getInt("DailyPrice");
                                    int price = carObject.getInt("price");
                                    String model_year = carObject.getString("Model , year");
                                    String color = carObject.getString("color");
                                    String status = carObject.getString("status");
                                    String image = carObject.getString("image");

                                    Car car = new Car(id, company, model_year, mileage, seats_number, monthlyPrice, dailyPrice, price, color, status, image);
                                    carList.add(car);
                                }
                                carAdapter.notifyDataSetChanged();
                            } else {
                                String message = jsonResponse.getString("message");
                                Toast.makeText(admin_home.this, "Failed to fetch cars: " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(admin_home.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(admin_home.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(admin_home.this).add(stringRequest);
    }

    public void addnew(View view) {
        Intent intent = new Intent(this, AddCar.class);
        startActivity(intent);
    }

    public void report(View view) {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }
}
