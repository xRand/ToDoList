package com.example.ilja.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.activeandroid.query.Select;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyActivity extends Activity {

    ArrayList<ListItems> list = new ArrayList<ListItems>();
    MyAdapter adapter;
    SharedPreferences sp;
    ListView listView;
    EditText todo;
    protected static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        adapter = new MyAdapter(this, list);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        todo = (EditText) findViewById(R.id.editText);

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

        //take a photo button
        todo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (todo.getRight() - todo.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dispatchTakePictureIntent();
                        return true;
                    }
                }
                return false;
            }
        });


        //add new item
        ImageButton btnAdd = (ImageButton) findViewById(R.id.button);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!todo.getText().toString().isEmpty()) {
                    addNew(null);
                }
               else Toast.makeText(getApplicationContext(), getResources().getText(R.string.empty), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //share active items
    protected  void shareAllActiveItems() {
        ArrayList<String> activeList = new ArrayList<String>();
        List <ListItems> data = new Select().from(ListItems.class).execute();
        for (ListItems item : data){
            if(!item.box) activeList.add(item.todo);
        }
        int array_size = activeList.size();
        String mailBody = "";
        for (int i=0;i<array_size;i++){
            mailBody += (i+1)+") "+activeList.get(i)+"\n";
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(android.content.Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "ToDoList");
        sendIntent.putExtra(Intent.EXTRA_TEXT, mailBody);
        //sendIntent.setType("text/plain");
        sendIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.share)));

    }

    //load data from DB
    protected void loadData() {
        list.clear();
        List <ListItems> data = new Select().from(ListItems.class).execute();
        for (ListItems item : data){
            list.add(item);
        }
        adapter.notifyDataSetChanged();
    }
    //take a photo
    protected void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void addNew(byte[] photo){

        String created = (String) DateFormat.format("dd.MM.yyyy ", new Date());
        ListItems item = new ListItems(false, todo.getText().toString(), created, "", photo);
        item.save();
        list.add(item);
        todo.setText("");
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer=out.toByteArray();
            addNew(buffer);

        }
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
        //settings activity
        if (id == R.id.action_settings) {
            Intent pref = new Intent(MyActivity.this, PrefActivity.class);
            startActivity(pref);
            return true;
        }
        //share activity
        if (id == R.id.action_share) {
            shareAllActiveItems();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
