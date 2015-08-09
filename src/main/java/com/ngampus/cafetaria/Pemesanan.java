package com.ngampus.cafetaria;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static com.ngampus.cafetaria.Constants.PILIHAN_HD;
import static com.ngampus.cafetaria.Constants.RASA_PILIHAN;
import static com.ngampus.cafetaria.Constants.TABLE_NAME;
import static com.ngampus.cafetaria.Constants.UKURAN_GL;

/**
 * Created by Faizal Lutviyanto on 7/30/2015.
 */
@SuppressWarnings("deprecation")
public class Pemesanan extends ListActivity implements OnClickListener {
    private DataMinuman data;
    private boolean hasShown = false;
    private TextView totalText;
    private Cursor cursorEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pemesanan);
        getListView().setChoiceMode(1);
        getListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                cursorEdit = (Cursor) getListView().getItemAtPosition(arg2);
                //method mengubah minumanPesanan
                editMinumanPesanan();
            }

        });

        data = new DataMinuman(this);
        try {
            Cursor cursor = getEvents();
            showEvents(cursor);
        } finally {
            data.close();
        }

        //Set Button
        View getNewMinuman = findViewById(R.id.minuman_baru);
        getNewMinuman.setOnClickListener(this);
        View getButtonKeluar = findViewById(R.id.button_keluar);
        getButtonKeluar.setOnClickListener(this);
        totalText = (TextView) findViewById(R.id.total);
    }

    //Overriding method onResume dari class Activity
    @Override
    protected void onResume() {
        super.onResume();
        try {
            Cursor cursor = getEvents();
            showEvents(cursor);
        } finally {
            data.close();
        }
        if (getListView().getCount() == 0) {
            totalText.setText("Total: Rp 0.00");
        }
        else {
            totalText.setText("Total: Rp " + (getListView().getCount() * 2000));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.minuman_baru:
                if (!hasShown) {
                    caraMemesanDialog();
                    hasShown = true;
                } else {
                    pilihanUkuranGelasDialog();
                }
                break;
            case R.id.button_keluar:
                if (getListView().getCount() != 0)
                    keluarDialog();
                else {
                    new AlertDialog.Builder(this).setTitle("Info").setMessage("Anda Harus Memesan Terlebih Dahulu Sebelum Keluar!.").setCancelable(false)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pilihanUkuranGelasDialog();
                                }
                            }).show();
                }
                break;
            //penambahan button disini(jika dperlukan)
        }
    }

    private void caraMemesanDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.judul_cara_memesan).setMessage(R.string.cara_memesan_teks).setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pilihanUkuranGelasDialog();
                    }
                }).show();
    }

    private void pilihanUkuranGelasDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.ukuran_gelas).setItems(R.array.ukuran_gelas, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ukuran;
                if (which == 0)
                    ukuran = "Gelas Kecil";
                else
                    ukuran = "Gelas Besar";
                pilihanHDDialog(ukuran);
            }
        }).show();
    }

    protected void pilihanHDDialog(final String ukuran) {
        new AlertDialog.Builder(this).setTitle(R.string.pilihan_hangat_dingin).setItems(R.array.pilihan_hangat_dingin, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pilihanHD;
                if (which == 0)
                    pilihanHD = "Hangat";
                else
                    pilihanHD = "Dingin";
                addEvent(ukuran, pilihanHD, "none");
                mulaiPemesanan();
            }
        }).show();
    }

    protected void mulaiPemesanan() {
        Intent intent = new Intent(Pemesanan.this, MinumanBaru.class);
        startActivity(intent);
    }

    private void keluarDialog() {
        new AlertDialog.Builder(this).setTitle(R.string.judul_keluar).setMessage(R.string.keluar_teks).setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.getWritableDatabase().delete(TABLE_NAME, null, null);
                        finish();
                    }
                }).show();
    }

    private void addEvent(String ukuran, String pilihanHD, String pilihanRasa) {
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UKURAN_GL, ukuran);
        values.put(PILIHAN_HD, pilihanHD);
        values.put(RASA_PILIHAN, pilihanRasa);
        db.insertOrThrow(TABLE_NAME, null, values);
    }

    private static String[] FROM = {UKURAN_GL, PILIHAN_HD, RASA_PILIHAN};
    private static String ORDER_BY = UKURAN_GL + " DESC";

    //
    private Cursor getEvents() {
        SQLiteDatabase db = data.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, ORDER_BY);
        startManagingCursor(cursor);
        return cursor;
    }
    //attribute static TO
    private static int[] TO = { R.id.teks1, R.id.teks2, R.id.rasaPilihanHeader};

    private void showEvents(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_listview, cursor, FROM, TO);
        setListAdapter(adapter);
    }

    private void editMinumanPesanan() {
        int aliranMinuman = cursorEdit.getInt(0);
        Bundle bundle = new Bundle();
        bundle.putInt(_ID, aliranMinuman);

        Intent intent = new Intent(Pemesanan.this, MinumanBaru.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
                //			startActivity(new Intent(this, Prefs.class));
                return true;
            case R.id.help:
                new AlertDialog.Builder(this).setTitle(R.string.judul_bantuan).setMessage(R.string.bantuan_ubah_).setCancelable(false)
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
}
