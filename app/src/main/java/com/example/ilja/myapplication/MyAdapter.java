package com.example.ilja.myapplication;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

    // кол-во элементов
    @Override
    public int getCount() {
        return list.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //созданное ранее View, но неиспользуемое в данный момент
        View view = convertView;
        //Если view вдруг оказался равен null тогда мы загружаем его с помошью inflater
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

        Button btnDel = (Button) view.findViewById(R.id.del);
        btnDel.setOnClickListener(myDel);
        btnDel.setTag(position);

        return view;
    }


    //чекбоксы
    CompoundButton.OnCheckedChangeListener myCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            int pos = (Integer) compoundButton.getTag();
            String done = "";
            if(b) done = (String) DateFormat.format("dd.MM.yyyy mm:ss", new Date());

            ListItems item = new ListItems (b, list.get(pos).todo, list.get(pos).created, done);
            list.set(pos, item);

            notifyDataSetChanged();
        }
    };
    //удаление
    View.OnClickListener myDel = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (Integer) view.getTag();
            list.remove(pos);
            notifyDataSetChanged();
        }
    };
}
