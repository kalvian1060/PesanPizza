package com.naldroid.zainalsalamun.skripsi.pesanpizza;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListKurirActivity extends ListActivity {

    private ProgressDialog pDialog;

    // URL to get contacts JSON
    private static String url = "http://trackingpizza.pe.hu/trackingPizza/semua_kurir.php";

    // JSON Node names
    private static final String TAG_KURIR = "kurir";
    private static final String TAG_ID = "id_kurir";
    private static final String TAG_NAMA = "nama";
    //private static final String TAG_ALAMAT= "alamat";
   // private static final String TAG_NOTELP = "no_telp";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
//    private static final String TAG_LATITUDET = "latitudet";
//    private static final String TAG_LONGITUDET = "longitudet";

    // contacts JSONArray
    JSONArray kurir = null;

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> kurirList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        kurirList = new ArrayList <HashMap<String, String>>();

        new GetKurir().execute();
        ListView lv = getListView();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String latitude = ((TextView) view.findViewById(R.id.latitude)).getText().toString();
                String longitude = ((TextView) view.findViewById(R.id.longitude)).getText().toString();
//                String latitudet = ((TextView) view.findViewById(R.id.latitudet)).getText().toString();
//                String longitudet = ((TextView) view.findViewById(R.id.longitudet)).getText().toString();

                Intent in = new Intent(getApplicationContext(), Maps.class);
                in.putExtra("latitude", latitude);
                in.putExtra("longitude", longitude);
//                in.putExtra("latitudet", latitudet);
//                in.putExtra("longitudet", longitudet);
                startActivityForResult(in, 100);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // jika result code 100
        if (resultCode == 100) {
            // jika result code 100 diterima artinya user mengedit/menghapus member
            // reload layar ini lagi
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetKurir extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ListKurirActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    kurir = jsonObj.getJSONArray(TAG_KURIR);

                    // looping through All Contacts
                    for (int i = 0; i < kurir.length(); i++) {
                        JSONObject c = kurir.getJSONObject(i);

                        String id_kurir = c.getString(TAG_ID);
                        String nama = c.getString(TAG_NAMA);
                        //String alamat = c.getString(TAG_ALAMAT);
                        //String no_telp = c.getString(TAG_NOTELP);
                        String latitude = c.getString(TAG_LATITUDE);
                        String longitude = c.getString(TAG_LONGITUDE);
//                        String latitudet = c.getString(TAG_LATITUDET);
//                        String longitudet = c.getString(TAG_LONGITUDET);

                        // tmp hashmap for single contact
                        HashMap<String, String> kurir = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        kurir.put(TAG_ID, id_kurir);
                        kurir.put(TAG_NAMA, nama);
                        //pemesan.put(TAG_ALAMAT, alamat);
                        //pemesan.put(TAG_NOTELP, no_telp);
                        kurir.put(TAG_LATITUDE, latitude);
                        kurir.put(TAG_LONGITUDE, longitude);
//                        kurir.put(TAG_LATITUDET, latitudet);
//                        kurir.put(TAG_LONGITUDET, longitudet);

                        // adding contactpemesan to contact list
                        kurirList.add(kurir);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Tidak bisa mendapatkan data dari url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ListKurirActivity.this, kurirList,
                    R.layout.list_item, new String[] {
                    TAG_ID,
                    TAG_NAMA,
                    //TAG_ALAMAT,
                    //TAG_NOTELP,
                    TAG_LATITUDE,
                    TAG_LONGITUDE,
//                    TAG_LATITUDET,
//                    TAG_LONGITUDET
            },
                    new int[] { R.id.id_kurir,
                            R.id.nama,
                           // R.id.alamat,
                            //R.id.no_telp,
                            R.id.latitude,
                            R.id.longitude,
//                            R.id.latitudet,
//                            R.id.longitudet,
                    });

            setListAdapter(adapter);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
