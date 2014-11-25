package com.example.ilja.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        TextView txt = (TextView) findViewById(R.id.details);
        Button del = (Button) findViewById(R.id.btnDel);

        final Intent intentA = getIntent();
        String details = intentA.getStringExtra("details");
        txt.setText(details);

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", intentA.getIntExtra("id", 0));
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                setResult(0, intent);
                finish();
            }
        });

    }
}
