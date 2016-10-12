package com.naldroid.zainalsalamun.skripsi.pesanpizza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ProdukActivity extends ActionBarActivity {

    private static final String TAG_URL = "http://trackingpizza.pe.hu/trackingPizza/produk.php";
    private static final String TAG_PRODUK = "produk";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_IDPRODUK = "id_produk";
    private static final String TAG_STOK = "stok";
    private static final String TAG_NAMA = "nama_produk";
    private static final String TAG_HARGA = "harga";
    private static final String TAG_DESKRIPSI = "deskripsi";
   // private static final String TAG_GAMBAR = "gambar";


    ProgressDialog pDialog;
    //    String status = "1";
    JSONParser jParser = new JSONParser();
    int success = 0;
    JSONArray cod = null;
    ListView listproduk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        new Proses().execute();
        listproduk = (ListView) findViewById(R.id.listView);
        listproduk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {

                String nama = ((TextView) view.findViewById(R.id.namaPro)).getText().toString();
                String deskripsi = ((TextView) view.findViewById(R.id.desPro)).getText().toString();
                String harga = ((TextView) view.findViewById(R.id.hargaPro)).getText().toString();
                String stok = ((TextView) view.findViewById(R.id.stokPro)).getText().toString();
                //String gambar = ((TextView) view.findViewById(R.id.gambar)).getText().toString();
                String idproduk = ((TextView) view.findViewById(R.id.idPro)).getText().toString();


                Intent x = new Intent(getApplicationContext(), DetailProduk.class);
                x.putExtra(TAG_NAMA, nama);
                x.putExtra(TAG_DESKRIPSI, deskripsi);
                x.putExtra(TAG_HARGA, harga);
                x.putExtra(TAG_STOK, stok);

                x.putExtra(TAG_IDPRODUK, idproduk);
               // x.putExtra(TAG_GAMBAR, gambar);


                startActivity(x);

            }
        });
    }

    public class Proses extends AsyncTask<String, String, String> {
        ArrayList<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProdukActivity.this);
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            JSONParser jParser = new JSONParser();

            JSONObject json = jParser.getJSONFromUrl(TAG_URL);

            try {
                cod = json.getJSONArray(TAG_PRODUK);
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    for (int i = 0; i < cod.length(); i++) {
                        JSONObject c = cod.getJSONObject(i);
                        HashMap<String, String> data = new HashMap<String, String>();

                        String id = c.getString(TAG_IDPRODUK);
                        String nama = c.getString(TAG_NAMA);
                        String deskripsi = c.getString(TAG_DESKRIPSI);
                        String harga = c.getString(TAG_HARGA);
                        String stok = c.getString(TAG_STOK);
                        //String gambar = c.getString(TAG_GAMBAR);


                        data.put(TAG_IDPRODUK, id);
                        data.put(TAG_NAMA, nama);
                        data.put(TAG_DESKRIPSI, deskripsi);
                        data.put(TAG_HARGA, harga);
                        data.put(TAG_STOK,stok);
                       // data.put(TAG_GAMBAR, gambar);

                        dataList.add(data);
                    }
                } else {
//                    status = "0";
                    success = 0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return TAG_MESSAGE;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            pDialog.dismiss();
            if (success == 1) {
                ListAdapter adapter = new SimpleAdapter(getApplicationContext(),
                        dataList, R.layout.list_item_produk,
                        new String[]{TAG_NAMA, TAG_DESKRIPSI, TAG_HARGA, TAG_IDPRODUK,
                                //TAG_GAMBAR,
                                TAG_STOK},
                        new int[]{R.id.namaPro, R.id.desPro, R.id.hargaPro, R.id.idPro,
                               // R.id.gambar,
                                R.id.stokPro});
                listproduk.setAdapter(adapter);

            } else {
                Toast.makeText(getApplicationContext(), "data tidak ada", Toast.LENGTH_LONG).show();

            }
        }
    }
}

