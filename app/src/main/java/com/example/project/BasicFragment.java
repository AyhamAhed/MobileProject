package com.example.project;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicFragment extends Fragment {

    private RecyclerView recyclerViewCars;
    private CarAdapter carAdapter;
    private List<Car> carList = new ArrayList<>();
    private EditText searchEditText;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        recyclerViewCars = view.findViewById(R.id.carsRecyclerView);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerViewCars.setLayoutManager(new LinearLayoutManager(getContext()));
        carAdapter = new CarAdapter(getContext(), carList);
        recyclerViewCars.setAdapter(carAdapter);
        fetchCarData();
        return view;
    }

    private void fetchCarData() {
        String url = "http://10.0.2.2/Project/fetch_cars.php"; // Replace with your server address

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Print the response to log
                Log.d("ServerResponse", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");

                    if (status.equals("success")) {
                        JSONArray carsArray = jsonResponse.getJSONArray("data");
                        carList.clear();
                        for (int i = 0; i < carsArray.length(); i++) {
                            JSONObject carObject = carsArray.getJSONObject(i);
                            Car car = new Car(
                                    carObject.getInt("ID"),
                                    carObject.getString("company"),
                                    carObject.getString("Model_year"), // Updated key name
                                    carObject.getInt("Mileage"),
                                    carObject.getInt("Seats_number"), // Updated key name
                                    carObject.getInt("MonthlyPrice"),
                                    carObject.getInt("DailyPrice"),
                                    carObject.getInt("price"),
                                    carObject.getString("color"),
                                    carObject.getString("status"),
                                    carObject.getString("image")
                            );
                            carList.add(car);
                        }
                        carAdapter.notifyDataSetChanged();
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getContext(), "Failed to fetch cars: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }
}
