package com.ngampus.cafetaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static com.ngampus.cafetaria.Constants.RASA_PILIHAN;
import static com.ngampus.cafetaria.Constants.TABLE_NAME;
import static com.ngampus.cafetaria.Constants.UKURAN_GL;

/**
 * Created by Faizal Lutviyanto on 7/30/2015.
 */
@SuppressWarnings("deprecation")
public class MinumanBaru extends Activity implements OnClickListener {
    //
    private RadioButton rasaPilRadio;
    private TextView rasaText;
    //
    private ArrayList<String> rasaPilList = new ArrayList<String>();
    private ArrayList<String> toppingsList = new ArrayList<String>();
    //objek data dari class DataMinuman
    private DataMinuman data;
    //deklarasi dan inisalisasi variabel id
    int id = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_baru);

        data = new DataMinuman(this);
        createToppingList();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.size() > 0)
            id = bundle.getInt(_ID);

        rasaPilRadio = (RadioButton) findViewById(R.id.rasaRadioB);
        rasaText = (TextView) findViewById(R.id.rasa_teks);

        if (id != 3) {
            Cursor minumanCursor = getMinuman();
            if (minumanCursor.moveToFirst()) {
                rasaText.setText(minumanCursor.getString(minumanCursor.getColumnIndex(RASA_PILIHAN)));
                //memberi jarak tanda (+)
                String[] rasaTopping = rasaText.getText().toString().split("[+]+");
                setArrayList(rasaPilList, rasaTopping);
            }
        }

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(this));

        gallery.setOnItemClickListener(new OnItemClickListener() {
            @SuppressWarnings("rawtypes")
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                if (rasaPilList.isEmpty())
                    rasaPilList.clear();
                if (rasaPilRadio.isChecked()) { // Rasa
                    if (rasaPilList.contains(toppingsList.get(position))) {
                        displayMessage(position, " dibatalkan");
                        rasaPilList.remove(rasaPilList.indexOf(toppingsList.get(position)));
                        if (rasaPilList.isEmpty())
                            rasaText.setText("");
                        else
                            rasaText.setText(editString(rasaPilList));
                    } else {
                        displayMessage(position, " ditambahkan");
                        rasaPilList.add(toppingsList.get(position));
                        rasaText.setText(editString(rasaPilList));
                    }
                }
            }

        });

        //set button
        View getTambahPesanButton = findViewById(R.id.tambahPesanButton);
        getTambahPesanButton.setOnClickListener(this);
        View getBatalButton = findViewById(R.id.batalButton);
        getBatalButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tambahPesanButton:
                tambahPesan();
                finish();
                break;
            case R.id.batalButton:
                //
                if (id == 3) {
                    data.getWritableDatabase().delete(TABLE_NAME, RASA_PILIHAN + "='none'", null);
                }
                else {
                    data.getWritableDatabase().delete(TABLE_NAME, _ID + "='" + id + "'", null);
                    finish();
                }
                break;
        }

    }

    private void tambahPesan() {
        String toppingRasa = "";
        if (rasaPilList.size() > 0) {
            toppingRasa = toppingRasa + editString(rasaPilList);
        }
        updateMinuman(toppingRasa);
    }

    private void createToppingList() {
        toppingsList.add("SUSU");
        toppingsList.add("COKLAT");
        toppingsList.add("GULA");
    }

    private void displayMessage(int position, String message) {
        Toast.makeText(MinumanBaru.this, toppingsList.get(position) + message, Toast.LENGTH_SHORT).show();
    }

    private String editString(List<String> list) {
        String toppings = "";
        String withOutComma;
        for (String item : list) {
            toppings += item + " + ";
        }
        if (toppings.equals("")) {
            withOutComma = "";
        }
        else {
            withOutComma = toppings.substring(0, toppings.length() - 2);
        }
        return withOutComma;
    }

    private void updateMinuman(String toppingRasa) {
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RASA_PILIHAN, toppingRasa);
        if (id == 3)
            db.update(TABLE_NAME, values, RASA_PILIHAN + "='none'", null);
        else
            db.update(TABLE_NAME, values, _ID + "='" + id + "'", null);
    }
    private static String ORDER_BY = UKURAN_GL + " DESC";

    private Cursor getMinuman() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, _ID + "='" + id + "'", null, null, null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.help:
                new AlertDialog.Builder(this).setTitle(R.string.judul_bantuan).setMessage(R.string.bantuan_tambah_).setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            case R.id.exit:
                new AlertDialog.Builder(this).setTitle(R.string.keluar).setMessage("Are you sure you want to exit?").setCancelable(true)
                        .setNeutralButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(0);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
        }
        return false;
    }

    private void setArrayList(ArrayList<String> list, String[] toppings) {
        for (int i = 0; i < toppings.length; i++) {
            list.add(toppings[i].trim());
        }
    }
}
