package com.ngampus.cafetaria;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * Created by Faizal Lutviyanto on 7/30/2015.
 */

public class MainActivity extends Activity implements OnClickListener {
    private EditText mNomorMeja;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        View getStartedButton = findViewById(R.id.button_ayo);
        getStartedButton.setOnClickListener(this);

        mNomorMeja = (EditText) findViewById(R.id.nomor_meja);
        final int grav = mNomorMeja.getGravity();
        mNomorMeja.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (mNomorMeja.getText().toString().equals(""))
                    mNomorMeja.setGravity(grav);
                else
                    mNomorMeja.setGravity(Gravity.CENTER);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        //switch case
        //
        switch (v.getId()) {
            case R.id.button_ayo:
                if (getNomorMeja().equals("") || getNomorMeja() == null) {
                    new AlertDialog.Builder(this).setTitle(R.string.judul_ada_kesalahan).setMessage(R.string.masukkan_no_meja_teks).setCancelable(false)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    Intent i = new Intent(this, Pemesanan.class);
                    startActivity(i);
                }
                break;
            // tambahan button di statement sini (jika diperlukan)
        }
    }
    //method bertipe string
    //mengembalikan nilai tipe sring dari instance objek mNomorMeja
    public String getNomorMeja() {
        return mNomorMeja.getText().toString();
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
                new AlertDialog.Builder(this).setTitle(R.string.judul_bantuan).setMessage(R.string.bantuan_nomor_meja).setCancelable(false)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                return true;
            case R.id.exit:
                new AlertDialog.Builder(this).setTitle(R.string.keluar).setMessage("Apakah anda ingin keluar?").setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

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
