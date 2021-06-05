package com.innova.care4u;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qractivity);

         String text=getIntentData(); // Whatever we need to encode in the QR code-- biometric key

        ImageView imageView=(ImageView)findViewById(R.id.image);
        ((Button)(findViewById(R.id.back))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("KEY",text));
            }
        });


        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,700,700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
            Toast.makeText(QRactivity.this, "The Biometric Key Encoded in QR Code is : "+ text, Toast.LENGTH_LONG).show();
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public String getIntentData()
    {// gets the cipher key from biometric authentication
        Intent intent = getIntent();
        String cipherKey = intent.getStringExtra("KEY");

        if (TextUtils.isEmpty(cipherKey)) {
            cipherKey = "“E7r9t8@Q#h%Hy+M";
        }

        return cipherKey;
    }
}
