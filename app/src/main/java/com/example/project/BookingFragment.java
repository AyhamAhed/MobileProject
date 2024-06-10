package com.example.project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class BookingFragment extends Fragment {

    private static final String ARG_PARAM1 = "";
    private static final String ARG_PARAM2 = "";
    private RecyclerView bookingsRecyclerView;
    private BookingAdapter bookingAdapter;

    private List<Booking> bookingList = new ArrayList<>();
    private int accountId = 1; // Replace with the actual user ID

    public BookingFragment() {
        // Required empty public constructor
    }

    public static BookingFragment newInstance(String param1, String param2) {
        BookingFragment fragment = new BookingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        bookingsRecyclerView = view.findViewById(R.id.carsBookedRecyclerView);
        bookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookingAdapter = new BookingAdapter(bookingList);
        bookingsRecyclerView.setAdapter(bookingAdapter);

        fetchBookingHistory(accountId);

        return view;
    }

    private void fetchBookingHistory(int accountId) {
        String url = "http://10.0.2.2/Project/fetch_bookings.php?accountID=" + accountId;

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("BookingHistoryResponse", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")) {
                        JSONArray bookingsArray = jsonResponse.getJSONArray("data");
                        bookingList.clear();
                        for (int i = 0; i < bookingsArray.length(); i++) {
                            JSONObject bookingObject = bookingsArray.getJSONObject(i);
                            Booking booking = new Booking(
                                    bookingObject.getInt("ID"),
                                    bookingObject.getInt("carID"),
                                    bookingObject.getInt("AccountID"),
                                    bookingObject.getInt("TotalPrice"),
                                    bookingObject.getString("status"),
                                    bookingObject.getString("startDate"),
                                    bookingObject.getString("endDate"),
                                    bookingObject.getString("carImage"),
                                    bookingObject.getString("carModel")
                            );
                            bookingList.add(booking);
                        }
                        bookingAdapter.notifyDataSetChanged();
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getContext(), "Failed to fetch bookings: " + message, Toast.LENGTH_SHORT).show();
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
