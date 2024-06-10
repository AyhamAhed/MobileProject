package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<Car> carList;
    private Context context;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.carNameTextView.setText(car.getCompany() + " " + car.getModelYear());
        String imageUrl = "http://10.0.2.2/" + car.getImage();

        Picasso.get()
                .load(imageUrl)
                .into(holder.imageViewCar);

        holder.btnViewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CarDetailsActivity.class);
                intent.putExtra("car_id", car.getID()); // Pass car ID
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {

        TextView carNameTextView;
        ImageView imageViewCar;
        Button btnViewDetails;
        CardView cardView;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carNameTextView = itemView.findViewById(R.id.carNameTextView);
            imageViewCar = itemView.findViewById(R.id.imageViewCar);
            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}
