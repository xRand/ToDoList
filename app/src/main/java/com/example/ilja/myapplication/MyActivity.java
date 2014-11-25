package com.example.ilja.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;


public class MyActivity extends Activity {

    ArrayList<ListItems> list = new ArrayList<ListItems>();
    MyAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        adapter = new MyAdapter(this, list);
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        //запуск второго активити
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyActivity.this, DetailsActivity.class);
                intent.putExtra("id", i);
                intent.putExtra("details", list.get(i).todo);
                startActivityForResult(intent, 0);
            }
        });

        //добавление
        Button btnAdd = (Button) findViewById(R.id.button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText todo = (EditText) findViewById(R.id.editText);
                if (!todo.getText().toString().isEmpty()) {
                    String created = (String) DateFormat.format("dd.MM.yyyy ", new Date());
                    ListItems item = new ListItems(false, todo.getText().toString(), created, "");
                    list.add(item);
                    todo.setText("");
                    adapter.notifyDataSetChanged();
                }
               else Toast.makeText(getApplicationContext(), "Введите текст", Toast.LENGTH_SHORT).show();
            }
        });

        loadList();

    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (data == null) return;
        super.onActivityResult(requestCode, resultCode, data);
        list.remove(data.getIntExtra("id", 0));
        adapter.notifyDataSetChanged();
    }

    //сохраняем данные
    void saveList() {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor ed = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        ed.putString("MyList", json);
        ed.commit();

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
    }
    //загружаем данные
    void loadList() {
        SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
        String json = pref.getString("MyList", "");
        if(json.isEmpty() == false) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray array = parser.parse(json).getAsJsonArray();
            for (JsonElement element : array) {
                ListItems it = gson.fromJson(element, ListItems.class);
                list.add(it);
            }
        }
    }

    @Override
    protected void onPause(){
        saveList();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
