package com.ngampus.cafetaria;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//import static -> dapat dipanggail secara langsung variabel/atribut yang dimiliki oleh class Constants / BaseColumn
import static android.provider.BaseColumns._ID;
import static com.ngampus.cafetaria.Constants.PILIHAN_HD;
import static com.ngampus.cafetaria.Constants.RASA_PILIHAN;
import static com.ngampus.cafetaria.Constants.TABLE_NAME;
import static com.ngampus.cafetaria.Constants.UKURAN_GL;

/**
 * Created by Faizal Lutviyanto on 7/30/2015.
 */
public class DataMinuman extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cafe.db";
    private static final int DATABASE_VERSION = 4;

    //Constructor DataMinumna
    public DataMinuman(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Mengoveride Method onCreate & onUpgrade dari --> abstract class SQLiteOpenHelper
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + UKURAN_GL
                + " TEXT NOT NULL, " + PILIHAN_HD + " TEXT NOT NULL, " + RASA_PILIHAN + " TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
