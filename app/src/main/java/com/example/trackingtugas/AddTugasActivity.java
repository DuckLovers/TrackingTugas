package com.example.trackingtugas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class AddTugasActivity extends AppCompatActivity {
    Spinner inputStatus;
    EditText inputNama, inputTenggat, inputKeterangan;
    Button inputButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tugas);

        inputStatus = findViewById(R.id.inputStatusTugas);
        inputNama = findViewById(R.id.inputNamaTugas);
        inputTenggat = findViewById(R.id.inputTenggatTugas);
        inputKeterangan = findViewById(R.id.inputKeteranganTugas);
        inputButton = findViewById(R.id.tombolInput);

        // Definisi array status
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

        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddTugasActivity.this);
                myDB.addTugas(inputStatus.getSelectedItem().toString().trim(),
                        inputNama.getText().toString().trim(),
                        inputTenggat.getText().toString().trim(),
                        inputKeterangan.getText().toString().trim()
                );
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

    public void BackToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
