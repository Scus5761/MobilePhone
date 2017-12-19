package com.itheima.find;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itheima.enginee.Contacts;
import com.itheima.mobile.R;
import com.itheima.ui.MyAsycnTask;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ThinkPad on 2017-05-26.
 */

public class ContactsActivity extends Activity{

    private ListView lv_contacts;
    private TextView tv_contacts_name;
    private TextView tv_contacts_phone;
    private ArrayList<HashMap<String, String>> list;
    private ProgressBar pb_contact;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        pb_contact = (ProgressBar) findViewById(R.id.pb_contact);

        lv_contacts = (ListView) findViewById(R.id.lv_contacts);

        new MyAsycnTask(){
            @Override
            public void preTak() {
                pb_contact.setVisibility(View.VISIBLE);
            }

            @Override
            public void postTak() {
                lv_contacts.setAdapter(new Myadapter());
                pb_contact.setVisibility(View.INVISIBLE);
            }

            @Override
            public void doinback() {
                list = (ArrayList<HashMap<String, String>>) Contacts.getAllContacts(getApplicationContext());

            }
        }.execute();

        lv_contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("num", "13929704684");
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    private class Myadapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.item_contacts, null);
            tv_contacts_phone = (TextView) view.findViewById(R.id.tv_contacts_phone);
            tv_contacts_name = (TextView) view.findViewById(R.id.tv_contacts_name);
           /* tv_contacts_name.setText(list.get(position).get("name"));
            tv_contacts_name.setText(list.get(position).get("phone"));*/

            tv_contacts_name.setText("徐大伟");
            tv_contacts_phone.setText("13929704684");
            return view;
        }
    }


}
