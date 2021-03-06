package com.naldroid.zainalsalamun.skripsi.pesanpizza;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputNama;
    public long _Lat, _Lng;

    //Location Manager
    LocationManager locManager;

    // inisialisasi url tambahanggota.php
    private static String url_tambah_kurir = "http://trackingpizza.pe.hu/trackingPizza/tambahkurir.php";

    // inisialisasi nama node dari json yang dihasilkan oleh php (utk class ini
    // hanya node "sukses")
    private static final String TAG_SUKSES = "sukses";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
        }

        // inisialisasi Edit Text
        inputNama = (EditText) findViewById(R.id.inputNama);

        // inisialisasi button
        Button btnTambahAnggota = (Button) findViewById(R.id.btnConfirm);

        // klik even tombol tambah anggota
        btnTambahAnggota.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // buat method pada background thread
                new BuatAnggotaBaru().execute();
            }
        });
    }

    private void updateWithNewLocation(Location location) {
        TextView myLocationText = (TextView) findViewById(R.id.tvLokasi);
        String latLongString = "Tunggu Hingga Lokasi Muncul. . .";
        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            double iLat = lat * 1000000000;
            double iLng = lng * 1000000000;
            _Lat = (long) iLat;
            _Lng = (long) iLng;
            latLongString = "Posisi Anda : \n Lat:" + lat + "\nLong:" + lng;
        } else {
            latLongString = "Lokasi Tidak Ditemukan :(";
        }
        myLocationText.setText(latLongString);
    }

    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {}

        public void onStatusChanged(String provider,int status,Bundle extras){}
    };

    /**
     * Background Async Task untuk menambah data anggota baru
     * */
    class BuatAnggotaBaru extends AsyncTask<String, String, String> {

        // sebelum memulai background thread tampilkan Progress Dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Menambah data..silahkan tunggu");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        // menambah data
        protected String doInBackground(String... args) {
            String nama = inputNama.getText().toString();
            String latitude = String.valueOf(_Lat);
            String longitude = String.valueOf(_Lng);

            // Parameters
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("nama", nama));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("longitude", longitude));

            // mengambil JSON Object dengan method POST
            JSONObject json = jsonParser.makeHttpRequest(url_tambah_kurir,
                    "POST", params);

            // periksa respon log cat
            Log.d("Respon tambah kurir", json.toString());

            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {

                    // jika sukses menambah data baru
                    Intent i = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(i);

                    // tutup activity ini
                    finish();
                } else {

                    // jika gagal dalam menambah data
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // hilangkan dialog ketika selesai menambah data baru
            pDialog.dismiss();
        }
    }
}