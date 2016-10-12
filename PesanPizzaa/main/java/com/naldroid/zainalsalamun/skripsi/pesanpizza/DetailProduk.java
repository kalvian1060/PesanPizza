package com.naldroid.zainalsalamun.skripsi.pesanpizza;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailProduk extends ActionBarActivity {

    private static final String TAG_URL ="http://trackingpizza.pe.hu/trackingPizza/produk.php";
    private static final String TAG_NAMA = "nama_produk";
    private static final String TAG_HARGA = "harga";
    private static final String TAG_DESKRIPSI = "deskripsi";
    private static final String TAG_STOK = "stok";
  //  private static final String TAG_GAMBAR = "gambar";

    private TextView nama;
    private TextView harga;
    private TextView deskripsi;
    private TextView stok;

    private Button beli;
    private ImageView image;

   // private DisplayImageOptions options;
    //private ImageLoader imageLoader;
    private ProgressBar Pbar;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);

        nama = (TextView) findViewById(R.id.namaProduk);
        harga = (TextView) findViewById(R.id.hargaProduk);
        deskripsi = (TextView) findViewById(R.id.desproduk);
        stok = (TextView) findViewById(R.id.stokproduk);
       // image = (ImageView) findViewById(R.id.gambarProduk);
        beli = (Button)findViewById(R.id.btnCheck);
        Pbar = (ProgressBar)findViewById(R.id.progressBar);

        Intent i = getIntent();

        nama.setText(i.getStringExtra(TAG_NAMA));
        harga.setText(i.getStringExtra(TAG_HARGA));
        deskripsi.setText(i.getStringExtra(TAG_DESKRIPSI));
        stok.setText(i.getStringExtra(TAG_STOK));


        beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(DetailProduk.this,KeranjangActivity.class);
//            String nama = ((TextView) view.findViewById(R.id.namaProduk)).getText().toString();
//                String harga = ((TextView) view.findViewById(R.id.hargaProduk)).getText().toString();
                i.putExtra(TAG_NAMA,nama.getText().toString());
                i.putExtra(TAG_HARGA,harga.getText().toString());
                startActivity(i);

            }
        });

    }

}
