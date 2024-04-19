package com.example.petcareapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    private Context context;
    private List<Jobs> petList;

    public PetAdapter(Context context, List<Jobs> petList) {
        this.context = context;
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jobitem, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Jobs pet = petList.get(position);


        holder.txvCustNam.setText(pet.getPetOwnerName());
        holder.txvPetType.setText(pet.getPetType());
        holder.txvPetGender.setText(pet.getPetGender());
        holder.txvPetPrice.setText(String.valueOf(pet.getTotalPrice()));
        holder.txvPetDuration.setText(String.valueOf(pet.getDuration()));

        holder.txvPetLocation.setText(pet.getPetLocation());
        Glide.with(context).load(Uri.parse(pet.getImageUrl())).into(holder.imvPetImage);

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView txvCustNam, txvPetType,txvPetGender,txvPetLocation,txvPetDuration,txvPetPrice;
        ImageView imvPetImage;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);

            txvCustNam = itemView.findViewById(R.id.txvCustNam);
            txvPetType = itemView.findViewById(R.id.txvPetType);
            imvPetImage = itemView.findViewById(R.id.imvPetImage);
            txvPetGender = itemView.findViewById(R.id.txvPetGender);
            txvPetDuration = itemView.findViewById(R.id.txvPetDuration);
            txvPetLocation = itemView.findViewById(R.id.txvPetLocation);
            txvPetPrice = itemView.findViewById(R.id.txvPetPrice);
        }
    }
}