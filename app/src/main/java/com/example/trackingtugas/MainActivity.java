package com.example.trackingtugas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Button AddButton;
    ImageView empty_imageview;
    TextView empty_textview;
    ImageButton deleteall_imageview;

    MyDatabaseHelper myDB;
    ArrayList<String> tugas_id, tugas_status, tugas_nama, tugas_tenggat, tugas_keterangan;
    customAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.viewTugas);
        AddButton = findViewById(R.id.AddButton);
        empty_imageview = findViewById(R.id.empty_imageview);
        empty_textview = findViewById(R.id.empty_textview);
        deleteall_imageview = findViewById(R.id.delete_all);

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTugasActivity.class);
                startActivity(intent);
            }
        });
        deleteall_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });

        myDB = new MyDatabaseHelper(MainActivity.this);
        tugas_id = new ArrayList<>();
        tugas_status = new ArrayList<>();
        tugas_nama = new ArrayList<>();
        tugas_tenggat = new ArrayList<>();
        tugas_keterangan = new ArrayList<>();

        storeDataInArrays();

        setupPieChart();

        customAdapter = new customAdapter(MainActivity.this,this, tugas_id, tugas_status, tugas_nama, tugas_tenggat, tugas_keterangan);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void setupPieChart() {
        if (tugas_status != null) {
            PieChart pieChart = findViewById(R.id.progressBar);
            TextView angkaBelum = findViewById(R.id.angkaBelum);
            TextView angkaSedang = findViewById(R.id.angkaSedang);
            TextView angkaSelesai = findViewById(R.id.angkaSelesai);
            TextView angkaTerkumpul = findViewById(R.id.angkaTerkumpul);

            ArrayList<PieEntry> entries = new ArrayList<>();

            int countBelumDikerjakan = 0;
            int countSedangDikerjakan = 0;
            int countSelesai = 0;
            int countTerkumpul = 0;

            // Hitung jumlah tugas berdasarkan status
            for (String status : tugas_status) {
                switch (status) {
                    case "Belum Dikerjakan":
                        countBelumDikerjakan++;
                        break;
                    case "Sedang Dikerjakan":
                        countSedangDikerjakan++;
                        break;
                    case "Selesai":
                        countSelesai++;
                        break;
                    case "Terkumpul":
                        countTerkumpul++;
                        break;
                }
            }

            // Tambahkan entri sesuai dengan jumlah tugas
            entries.add(new PieEntry(countBelumDikerjakan, "Belum Dikerjakan"));
            entries.add(new PieEntry(countSedangDikerjakan, "Sedang Dikerjakan"));
            entries.add(new PieEntry(countSelesai, "Selesai"));
            entries.add(new PieEntry(countTerkumpul, "Terkumpul"));

            PieDataSet dataSet = new PieDataSet(entries, "Status Tugas");

            // Atur warna pada dataSet menggunakan warna dari colors.xml
            Resources res = getResources();
            dataSet.setColors(
                    res.getColor(R.color.BelumDikerjakan),
                    res.getColor(R.color.SedangDikerjakan),
                    res.getColor(R.color.Selesai),
                    res.getColor(R.color.Terkumpul)
            );

            angkaBelum.setText(String.valueOf(countBelumDikerjakan));
            angkaSedang.setText(String.valueOf(countSedangDikerjakan));
            angkaSelesai.setText(String.valueOf(countSelesai));
            angkaTerkumpul.setText(String.valueOf(countTerkumpul));

            // Atur teks label
            dataSet.setDrawValues(false); // Nonaktifkan nilai default
            pieChart.setDrawEntryLabels(false); // Nonaktifkan label
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setEnabled(false);

            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            // Memuat ulang data dari database
            storeDataInArrays();
            // Memberitahu adapter bahwa data telah berubah
            customAdapter.notifyDataSetChanged();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();

        // Kosongkan array terlebih dahulu untuk menghindari duplikasi data
        tugas_id.clear();
        tugas_status.clear();
        tugas_nama.clear();
        tugas_tenggat.clear();
        tugas_keterangan.clear();

        if(cursor.getCount() == 0){
            empty_imageview.setVisibility(View.VISIBLE);
            empty_textview.setVisibility(View.VISIBLE);
        } else {
            while (cursor.moveToNext()){
                tugas_id.add(cursor.getString(0));
                tugas_status.add(cursor.getString(1));
                tugas_nama.add(cursor.getString(2));
                tugas_tenggat.add(cursor.getString(3));
                tugas_keterangan.add(cursor.getString(4));
            }
            empty_imageview.setVisibility(View.GONE);
            empty_textview.setVisibility(View.GONE);
        }
        cursor.close();  // Jangan lupa menutup cursor setelah tidak digunakan lagi
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Semua?");
        builder.setMessage("Apakah kamu ingin menghapus semua data yang ada?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
                myDB.deleteAllData();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}