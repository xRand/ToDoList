package com.example.ilja.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class DetailsActivity extends MyActivity {

    ImageView ivPhoto;
    Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        final EditText edit  = (EditText) findViewById(R.id.details);
        Button del = (Button) findViewById(R.id.btnDel);
        Button share = (Button) findViewById(R.id.btnShare);

        Intent intent = getIntent();
        final String details = intent.getStringExtra("details");
        id = intent.getLongExtra("id", 0);
        edit.setText(details);

        //get photo
        ListItems item = ListItems.load(ListItems.class, id);
        if(item.getPhoto() != null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(item.getPhoto(), 0, item.getPhoto().length);
            ivPhoto.setImageBitmap(bmp);
        }

        //take photo button
        edit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (edit.getRight() - edit.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dispatchTakePictureIntent();
                        return true;
                    }
                }
                return false;
            }
        });


        //share intent
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                if(ivPhoto.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.png");
                    try {
                        f.createNewFile();
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                }
                share.putExtra(Intent.EXTRA_TEXT, edit.getText().toString());

                startActivity(Intent.createChooser(share, getResources().getText(R.string.share)));

            }
        });

        //edit item
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                ListItems item = ListItems.load(ListItems.class, id);
                item.todo = charSequence.toString();
                item.save();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //delete item
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListItems item = ListItems.load(ListItems.class, id);
                item.delete();
                item.save();

                Toast.makeText(DetailsActivity.this, getResources().getText(R.string.deleted), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ivPhoto.setImageBitmap(imageBitmap);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer=out.toByteArray();

            ListItems item = ListItems.load(ListItems.class, id);
            item.setPhoto(buffer);
            item.save();

        }
    }

}
