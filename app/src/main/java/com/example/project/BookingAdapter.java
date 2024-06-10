package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private final List<Booking> bookingList;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.carIDTextView.setText("Car ID: " + booking.getCarID());
        holder.carModelTextView.setText(booking.getCarModel());
        holder.totalPriceTextView.setText("Total Price: $" + booking.getTotalPrice());
        holder.startDateTextView.setText("Start Date: " + booking.getStartDate());
        holder.endDateTextView.setText("End Date: " + booking.getEndDate());
        holder.statusTextView.setText("Status: " + booking.getStatus());
        Picasso.get().load("http://10.0.2.2/" + booking.getCarImage()).into(holder.carImageView);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {

        TextView carIDTextView;
        TextView carModelTextView;
        TextView totalPriceTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView statusTextView;
        ImageView carImageView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            carIDTextView = itemView.findViewById(R.id.carIDTextView);
            carModelTextView = itemView.findViewById(R.id.carModelTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            startDateTextView = itemView.findViewById(R.id.startDateTextView);
            endDateTextView = itemView.findViewById(R.id.endDateTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            carImageView = itemView.findViewById(R.id.carImageView);
        }
    }
}
