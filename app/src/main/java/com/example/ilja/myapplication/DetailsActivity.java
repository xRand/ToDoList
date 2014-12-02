package com.example.ilja.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DetailsActivity extends MyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        EditText edit  = (EditText) findViewById(R.id.details);
        Button del = (Button) findViewById(R.id.btnDel);
        Button share = (Button) findViewById(R.id.btnShare);

        Intent intent = getIntent();
        final String details = intent.getStringExtra("details");
        final Integer id = intent.getIntExtra("id", 0);
        edit.setText(details);


        //share intent
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, details);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));
            }
        });

        //delete item
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("id", id);
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                setResult(0, intent);
                finish();
            }
        });

    }

//    @Override
//    protected void onPause() {
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
//        SharedPreferences.Editor editor = pref.edit();
//       // editor.putString("to//do", edit.getText().toString());
//        editor.commit();
//        super.onPause();
//    }
}
