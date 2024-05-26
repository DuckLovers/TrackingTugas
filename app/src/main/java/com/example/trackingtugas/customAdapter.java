package com.example.trackingtugas;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class customAdapter extends RecyclerView.Adapter<customAdapter.MyViewHolder> {

    private Context context;
    Activity activity;
    private ArrayList tugas_id, tugas_status, tugas_nama,tugas_tenggat, tugas_keterangan;

    Animation translate_anim;
    customAdapter(Activity activity,
                  Context context,
                  ArrayList tugas_id,
                  ArrayList tugas_status,
                  ArrayList tugas_nama,
                  ArrayList tugas_tenggat,
                  ArrayList tugas_keterangan) {
        this.activity = activity;
        this.context = context;
        this.tugas_id = tugas_id;
        this.tugas_status = tugas_status;
        this.tugas_nama = tugas_nama;
        this.tugas_tenggat = tugas_tenggat;
        this.tugas_keterangan = tugas_keterangan;

    }

    @NonNull
    @Override
    public customAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.MyViewHolder holder, final int position) {
        String status = (String) tugas_status.get(position); // Asumsi ini adalah String
        int color;

        switch (status) {
            case "Belum Dikerjakan":
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.BelumDikerjakan);
                break;
            case "Sedang Dikerjakan":
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.SedangDikerjakan);
                break;
            case "Selesai":
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Selesai);
                break;
            case "Terkumpul":
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.Terkumpul);
                break;
            default:
                color = ContextCompat.getColor(holder.itemView.getContext(), R.color.BelumDikerjakan); // Default color
                break;
        }

        // Set tint color
        holder.tugas_status_img.setImageTintList(ColorStateList.valueOf(color));

        // Set text
        holder.tugas_nama_txt.setText(String.valueOf(tugas_nama.get(position)));
        holder.tugas_tenggat_txt.setText(String.valueOf(tugas_tenggat.get(position)));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(tugas_id.get(position)));
                intent.putExtra("status", String.valueOf(tugas_status.get(position)));
                intent.putExtra("nama", String.valueOf(tugas_nama.get(position)));
                intent.putExtra("tenggat", String.valueOf(tugas_tenggat.get(position)));
                intent.putExtra("keterangan", String.valueOf(tugas_keterangan.get(position)));
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tugas_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView tugas_status_img;
        TextView tugas_nama_txt, tugas_tenggat_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tugas_status_img = itemView.findViewById(R.id.circleStatus);
            tugas_nama_txt = itemView.findViewById(R.id.namaTugas);
            tugas_tenggat_txt = itemView.findViewById(R.id.tenggatTugas);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_anim = AnimationUtils.loadAnimation(context, R.anim.translate_anim);
            mainLayout.setAnimation(translate_anim);
        }
    }
}
