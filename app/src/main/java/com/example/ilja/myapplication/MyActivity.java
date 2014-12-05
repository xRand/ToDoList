package com.example.ilja.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.activeandroid.query.Select;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    ArrayList<ListItems> list = new ArrayList<ListItems>();
    MyAdapter adapter;
    SharedPreferences sp;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        adapter = new MyAdapter(this, list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        //details activity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, DetailsActivity.class);
                Long id = list.get(i).getId();
                intent.putExtra("id", id);
                intent.putExtra("details", list.get(i).todo);
                startActivity(intent);
            }
        });

        //add new item
        Button btnAdd = (Button) findViewById(R.id.button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText todo = (EditText) findViewById(R.id.editText);
                if (!todo.getText().toString().isEmpty()) {

                    String created = (String) DateFormat.format("dd.MM.yyyy ", new Date());
                    ListItems item = new ListItems(false, todo.getText().toString(), created, "");
                    item.save();
                    list.add(item);
                    todo.setText("");
                    adapter.notifyDataSetChanged();

                }
               else Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //load data from DB
    void loadData() {
        list.clear();
        List <ListItems> data = new Select().from(ListItems.class).execute();
        for (ListItems item : data){
            list.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        //окрашиваем тутдулист
        String color = sp.getString("color", "error");
        if(color=="error") color = "YELLOW";
        listView.setBackgroundColor(Color.parseColor(color));
        loadData();
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent pref = new Intent(MyActivity.this, PrefActivity.class);
            startActivity(pref);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
