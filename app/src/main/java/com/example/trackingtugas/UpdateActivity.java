package com.example.trackingtugas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    Spinner inputStatus;
    EditText inputNama, inputTenggat, inputKeterangan;
    Button updateButton, deleteButton;
    String id, status, nama, tenggat, keterangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        inputStatus = findViewById(R.id.inputStatusTugas2);
        inputNama = findViewById(R.id.inputNamaTugas2);
        inputTenggat = findViewById(R.id.inputTenggatTugas2);
        inputKeterangan = findViewById(R.id.inputKeteranganTugas2);
        updateButton = findViewById(R.id.tombolUpdate);
        deleteButton = findViewById(R.id.tombolHapus);

        // Inisialisasi array status
        String[] statusOptions = {"Belum Dikerjakan", "Sedang Dikerjakan", "Selesai", "Terkumpul"};

        // Menghubungkan array dengan Spinner menggunakan ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusOptions);
        inputStatus.setAdapter(adapter);

        // Ambil Tanggal
        inputTenggat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

        });

        // Panggil getAndSetIntentData setelah inputStatus diinisialisasi
        getAndSetIntentData();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.updateData(id, inputStatus.getSelectedItem().toString().trim(),
                        inputNama.getText().toString().trim(),
                        inputTenggat.getText().toString().trim(),
                        inputKeterangan.getText().toString().trim());
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
    }

    public void showDatePickerDialog() { // Parameter dihapus
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format tanggal sesuai preferensi Anda (dd/MM/yyyy)
                        String selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
                        inputTenggat.setText(selectedDate);
                    }
                },
                // Atur tanggal default ke tanggal saat ini
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void getAndSetIntentData() {
        if (getIntent().hasExtra("id") &&
                getIntent().hasExtra("status") &&
                getIntent().hasExtra("nama") &&
                getIntent().hasExtra("tenggat") &&
                getIntent().hasExtra("keterangan")) {

            // Ambil data dari Intent
            id = getIntent().getStringExtra("id");
            status = getIntent().getStringExtra("status");
            nama = getIntent().getStringExtra("nama");
            tenggat = getIntent().getStringExtra("tenggat");
            keterangan = getIntent().getStringExtra("keterangan");

            // Setting intent data
            inputNama.setText(nama);
            setSpinnerSelection(status);
            inputTenggat.setText(tenggat);
            inputKeterangan.setText(keterangan);
        } else {
            Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus " + nama + " ?");
        builder.setMessage("Apakah kamu ingin menghapus " + nama + " ?");
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    /**
     * Mengatur pilihan spinner berdasarkan teks.
     * @param status Teks yang akan dicari dalam spinner.
     */
    void setSpinnerSelection(String status) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) inputStatus.getAdapter();
        int position = adapter.getPosition(status);

        // Jika posisi ditemukan, atur sebagai pilihan terpilih pada Spinner
        if (position >= 0) {
            inputStatus.setSelection(position);
        }
    }

    public void BackToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}