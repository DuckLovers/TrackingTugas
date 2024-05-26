package com.example.trackingtugas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;


public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final String DATABASE_NAME = "CatatanTugas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Tugas";
    private static final String COLUMN_ID = "id_tugas";
    private static final String COLUMN_STATUS = "status_tugas";
    private static final String COLUMN_NAMA = "nama_tugas";
    private static final String COLUMN_TENGGAT = "tenggat_tugas";
    private static final String COLUMN_KETERANGAN = "keterangan_tugas";

    MyDatabaseHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STATUS + " TEXT CHECK (status_tugas IN ('Belum Dikerjakan', 'Sedang Dikerjakan', 'Selesai', 'Terkumpul')), " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_TENGGAT + " DATE, " +
                COLUMN_KETERANGAN + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTugas(String status, String nama, String tenggat, String keterangan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_TENGGAT, tenggat);
        cv.put(COLUMN_KETERANGAN, keterangan);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1){
            Toast.makeText(context, "Gagal Ditambahkan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_STATUS + " ASC, " + COLUMN_TENGGAT + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String status, String nama, String tenggat, String keterangan){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_STATUS, status);
        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_TENGGAT, tenggat);
        cv.put(COLUMN_KETERANGAN, keterangan);

        long result = db.update(TABLE_NAME, cv, "id_tugas=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Gagal Memperbarui", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Berhasil Memperbarui", Toast.LENGTH_SHORT).show();
            Log.d("UpdateData", "Data dengan ID " + row_id + " diperbarui dengan status " + status);
        }
    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "id_tugas=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Gagal Menghapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Berhasil Menghapus", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
