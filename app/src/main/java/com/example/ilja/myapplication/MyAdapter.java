package com.example.ilja.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;


public class MyAdapter extends BaseAdapter  {
    Context context;
    ArrayList<ListItems> list;
    LayoutInflater inflater;

    public MyAdapter(Context context, ArrayList<ListItems> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.mylist, parent, false);
        }

        ListItems p = list.get(position);

        ((TextView) view.findViewById(R.id.todo)).setText(p.todo);
        ((TextView) view.findViewById(R.id.created)).setText(p.created);
        ((TextView) view.findViewById(R.id.done)).setText(p.done);

        CheckBox check = (CheckBox) view.findViewById(R.id.check);
        check.setTag(position);
        check.setOnCheckedChangeListener(null);
        check.setChecked(p.box);
        check.setOnCheckedChangeListener(myCheck);

        ImageButton btnDel = (ImageButton) view.findViewById(R.id.del);
        btnDel.setOnClickListener(myDel);
        btnDel.setTag(position);

        ImageView photo = (ImageView) view.findViewById(R.id.photo);

        if(p.photo!= null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(p.photo, 0, p.photo.length);
            photo.setImageBitmap(bmp);
        } else photo.setImageBitmap(null);


        return view;
    }

    //checkbox
    CompoundButton.OnCheckedChangeListener myCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int pos = (Integer) compoundButton.getTag();
            Long id = list.get(pos).getId();
            String done = "";
            if(b) done = (String) DateFormat.format("dd.MM.yyyy", new Date());

            ListItems item = ListItems.load(ListItems.class, id);
            item.setBox(b);
            item.setDone(done);
            item.save();
            list.set(pos, item);

            notifyDataSetChanged();

        }
    };
    //delete item
    View.OnClickListener myDel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (Integer) view.getTag();
            Long id = list.get(pos).getId();
            ListItems item = ListItems.load(ListItems.class, id);
            item.delete();
            list.remove(pos);

            notifyDataSetChanged();
        }
    };
}
