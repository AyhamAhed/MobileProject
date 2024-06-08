package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private Context context;
    private AdapterView.OnItemClickListener onItemClickListener;
    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    public CarAdapter(Context context, List<Car> carList, AdapterView.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.carList = carList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.carNameTextView.setText(car.getName());
        holder.carImageView.setImageResource(car.getImageResource());
        holder.viewDetailsButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CarDetailsActivity.class);
            intent.putExtra("CAR_MAKE_MODEL", car.getName());
            intent.putExtra("CAR_YEAR", "2021"); // Replace with actual data
            intent.putExtra("CAR_PRICE", "$100/day"); // Replace with actual data
            intent.putExtra("CAR_FEATURES", car.getDetails());
            intent.putExtra("CAR_IMAGE_RES_ID", car.getImageResource());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;
        TextView carNameTextView;
        TextView carDetailsTextView;
        Button viewDetailsButton;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carImageView = itemView.findViewById(R.id.carImageView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
}
