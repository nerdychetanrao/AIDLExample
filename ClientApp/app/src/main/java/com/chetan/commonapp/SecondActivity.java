package com.chetan.commonapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chetan.clientapp.R;


import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final ArrayList<String> array_list = (ArrayList<String>) getIntent().getSerializableExtra("array_list");

        ListView lv = findViewById(R.id.lv1);

       /* ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                array_list );*/

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.row_layout, R.id.date_title, array_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(R.id.date_title);
                TextView text2 = (TextView) view.findViewById(R.id.diff_summary);
                String[] temp = array_list.get(position).split("%");
                text1.setText(temp[0]);
                text2.setText(temp[1]);
                return view;
            }
        };
        lv.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
