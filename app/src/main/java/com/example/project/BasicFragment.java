package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

public class BasicFragment extends Fragment {

    private EditText searchQueryEditText;
    private RecyclerView carsRecyclerView;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private List<Car> filteredCarList;

    private Button viewDetailsButton;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        searchQueryEditText = view.findViewById(R.id.searchEditText);
        carsRecyclerView = view.findViewById(R.id.carsRecyclerView);
        carList = getCarList(); // Replace with actual data source
        filteredCarList = new ArrayList<>(carList);

        carAdapter = new CarAdapter(getContext(), filteredCarList, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        carsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        carsRecyclerView.setAdapter(carAdapter);

        // Apply item decoration for spacing
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        carsRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

        searchQueryEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCars(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void filterCars(String query) {
        filteredCarList.clear();
        if (query.isEmpty()) {
            filteredCarList.addAll(carList);
        } else {
            for (Car car : carList) {
                if (car.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredCarList.add(car);
                }
            }
        }
        carAdapter.notifyDataSetChanged();
    }

    private List<Car> getCarList() {
        // Replace with actual data retrieval logic
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("Car 1", "Details about Car 1", R.drawable.car));
        cars.add(new Car("Car 2", "Details about Car 2", R.drawable.car));
        // Add more cars
        return cars;
    }
}
