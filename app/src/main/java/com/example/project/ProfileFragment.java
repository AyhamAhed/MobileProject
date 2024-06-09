package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the
 * factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private Button saveBtn;
    private EditText nameEdt;
    private EditText passEdt;
    private EditText emailEdt;
    private EditText phoneEdt;

    private String userEmail;  // This will hold the email of the logged-in user

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve the logged-in user's email from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Hooks
        saveBtn = view.findViewById(R.id.saveBtn);
        nameEdt = view.findViewById(R.id.nameEdt);
        passEdt = view.findViewById(R.id.passEdt);
        emailEdt = view.findViewById(R.id.emailEdt);
        phoneEdt = view.findViewById(R.id.phoneEdt);

        fetchProfileData();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        return view;
    }

    private void fetchProfileData() {
        if (userEmail == null) {
            Toast.makeText(getContext(), "No user email found", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/Project/fetch_profile.php"; // Replace with your server address

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");

                    if (status.equals("success")) {
                        JSONObject userData = jsonResponse.getJSONObject("data");
                        nameEdt.setText(userData.getString("name"));
                        passEdt.setText(userData.getString("password"));
                        emailEdt.setText(userData.getString("email"));
                        phoneEdt.setText(userData.getString("phone"));
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getContext(), "Failed to fetch profile data: " + message, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail); // Use the logged-in user's email
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void saveProfileData() {
        final String name = nameEdt.getText().toString().trim();
        final String password = passEdt.getText().toString().trim();
        final String email = emailEdt.getText().toString().trim();
        final String phone = phoneEdt.getText().toString().trim();

        if (name.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/Project/update_profile.php"; // Replace with your server address

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");

                    if (status.equals("success")) {
                        Toast.makeText(getContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(getContext(), "Failed to update profile: " + message, Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userEmail); // Use the logged-in user's email
                params.put("name", name);
                params.put("password", password);
                params.put("phone", phone);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
