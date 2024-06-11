package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final String PREFS_NAME = "LoginPrefs";
    private static final String PREF_EMAIL = "email";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_REMEMBER = "rememberMe";

    private EditText emailEditText;
    private EditText passwordEditText;
    private CheckBox rememberMeCheckBox;
    private Button loginButton;
    private Button signUpButton;
    private TextView forgotPasswordTextView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Intent
        Intent intent = getIntent();

        // Hooks
        emailEditText = findViewById(R.id.emailEdt);
        passwordEditText = findViewById(R.id.passwordEdt);
        rememberMeCheckBox = findViewById(R.id.chkRemember);
        loginButton = findViewById(R.id.loginBtn);
        signUpButton = findViewById(R.id.signupBtn);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Check if credentials are saved
        if (sharedPreferences.getBoolean(PREF_REMEMBER, false)) {
            emailEditText.setText(sharedPreferences.getString(PREF_EMAIL, ""));
            passwordEditText.setText(sharedPreferences.getString(PREF_PASSWORD, ""));
            rememberMeCheckBox.setChecked(true);
            loginUser(); // Automatically log in if credentials are saved
        }

        // Events
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });
    }

    // Handle Log In Button
    private void loginUser() {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://10.0.2.2/Project/login.php"; // Replace with your server address

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response from server: " + response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String status = jsonResponse.getString("status");

                    if (status.equals("success")) {
                        String userType = jsonResponse.getString("Account_type");
                        if (userType.equals("user")) {
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Save login state if "Remember Me" is checked
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);  // Save the email of the logged-in user
                            editor.apply();
                            if (rememberMeCheckBox.isChecked()) {
                                editor.putString(PREF_EMAIL, email);
                                editor.putString(PREF_PASSWORD, password);
                                editor.putBoolean(PREF_REMEMBER, true);
                            } else {
                                editor.clear();
                            }
                            editor.apply();

                            // Proceed to the next activity or home screen
                            Intent intent = new Intent(LoginActivity.this, ViewActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this, admin_home.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Only Admin are allowed to log in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(LoginActivity.this, "Login failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Login failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error response from server: ", error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("action", "login");
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}

