package com.naldroid.zainalsalamun.skripsi.pesanpizza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class KeranjangActivity extends ActionBarActivity {
    private Button checkout;
    private TextView nama;
    private TextView harga;
    private TextView total,date;
    private EditText ket;
    private Spinner jumlah;
    int hasil = 0;
    JSONParser jsonParser = new JSONParser();
    ProgressDialog pDialog;
    JSONObject json;
    JSONArray databelanja;
    int success = 0;

    private static final String TAG_NAMA = "nama_produk";
    private static final String TAG_HARGA = "harga";
    private static final String TAG_JUMLAH = "jumlah";
    private static final String TAG_KET = "keterangan";
    private static final String TAG_TOT = "total_harga";
  //  private static final String TAG_DATE = "waktutanggal";
    private static final String TAG_MESSAGE = "pesan";
    private static final String TAG_SUCCESS = "sukses";
    private static final String LOGIN_URL = "http://trackingpizza.pe.hu/trackingPizza/simpankeranjang.php";




    //    int t = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keranjang);

        nama = (TextView) findViewById(R.id.namabarang);
        harga = (TextView) findViewById(R.id.hargabarang);
        checkout = (Button) findViewById(R.id.btnCheck);
        jumlah = (Spinner) findViewById(R.id.jumlahspin);
        total = (TextView) findViewById(R.id.total);
        ket =(EditText)findViewById(R.id.ket);
        Intent i = getIntent();

        nama.setText(i.getStringExtra(TAG_NAMA));
        harga.setText(i.getStringExtra(TAG_HARGA));



        jumlah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String a = String.valueOf(jumlah.getSelectedItem());
                if (a.equals(String.valueOf(jumlah.getSelectedItem()))) {
                    int c = Integer.parseInt(harga.getText().toString());
                    int tot = Integer.parseInt(parent.getItemAtPosition(position).toString());
                    hasil = c * tot;
                    total.setText(String.valueOf(hasil));
//                    total.setText(hasil);

                } else {
                    Toast.makeText(parent.getContext(), "anda belum memilih jumlah" + parent.getItemAtPosition(position).toString(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new checkout().execute(nama.getText().toString(), harga.getText().toString(), jumlah.getSelectedItem().toString(),
                        ket.getText().toString(), total.getText().toString());
            }
        });
    }

    class checkout extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KeranjangActivity.this);
            pDialog.setMessage("Sedang proses .....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Integer doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            String nama_produk = params[0];
            int harga = Integer.parseInt(params[1]);
            int jumlah = Integer.parseInt(params[2]);
            String keterangan = params[3];
            int total_harga = Integer.parseInt(params[4]);

            param.add(new BasicNameValuePair(TAG_NAMA, nama_produk));
            param.add(new BasicNameValuePair(TAG_HARGA, String.valueOf(harga)));
            param.add(new BasicNameValuePair(TAG_JUMLAH, String.valueOf(jumlah)));
            param.add(new BasicNameValuePair(TAG_KET, keterangan));
            param.add(new BasicNameValuePair(TAG_TOT, String.valueOf(total_harga)));

            json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", param);
            try {
                success = json.getInt(TAG_SUCCESS);
                //databelanja = json.getJSONArray("pembeli");
                if (success == 1) {
                    Log.d("Attempt", "Success");
                } else
                    Log.d("JSON Parser", String.valueOf(success));
                Log.i("JSON Parser", json.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Integer s) {
            pDialog.dismiss();
            if (s == 1) {
                Intent login = new Intent(getApplicationContext(), Menu.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);
                Toast.makeText(getApplicationContext(), "Simpan data pembelian berhasil",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Simpan data gagal",
                        Toast.LENGTH_LONG).show();

            }
        }
    }
}
