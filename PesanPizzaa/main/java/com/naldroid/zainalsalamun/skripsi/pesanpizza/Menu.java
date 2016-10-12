package com.naldroid.zainalsalamun.skripsi.pesanpizza;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Menu extends Activity {
    DrawerLayout drawer;


    ImageButton btnSurvei, btnHasil, btnKantor, btnTentang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btnSurvei = (ImageButton) findViewById(R.id.btnSurvei);
        btnHasil = (ImageButton)findViewById(R.id.btnHasil);
        btnKantor =(ImageButton)findViewById(R.id.btnKantor);
        btnTentang=(ImageButton)findViewById(R.id.btnTentang);

        btnSurvei.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture

            }
        });



        //lihat data
        btnHasil.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // go tampil data activity
                Intent login= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(login);

            }
        });
//
//
        btnKantor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kurir= new Intent(getApplicationContext(),ListKurirActivity.class);
                startActivity(kurir);

            }
        });
//
        btnTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tentang();
            }
        });
//

    }


    /**
     * Checking device has camera hardware or not
     * */



    public void Tentang(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Menu.this);
        alertDialog.setIcon(R.drawable.logo)
                .setTitle("Tentang")
                .setMessage("Aplikasi ini merupakan Aplikasi Pemesanan Pizza " +
                        "dimana pengguna Aplikasi ini dapat melihat estimasi" +
                        "waktu kedatangan kurir ke lokasi pemesan " +
                        "secara realtime")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.create().show();
    }
    public void onBackPressed() {
//            super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Apakah anda yakin ingin keluar?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }




    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */


}