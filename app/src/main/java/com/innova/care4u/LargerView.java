package com.innova.care4u;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Oluwatobi on 7/16/2037.
 */

public class LargerView extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.larger_view);
        String title = getIntent().getExtras().getString("me");
        TextView textView = (TextView) findViewById(R.id.myTitle);
        textView.setText(title);

        ImageView imageView = (ImageView)findViewById(R.id.backFrom);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
