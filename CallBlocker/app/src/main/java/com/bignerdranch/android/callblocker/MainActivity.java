package com.bignerdranch.android.callblocker;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.ContactsContract.*;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnItemLongClickListener {

    private Button add_blocklist_btn;
    private Button show_blocklist_btn;
    public ListView listview;

    private RadioGroup mRadioGroup;

    public RadioButton blockAll;
    public RadioButton blockUnsaved;
    public RadioButton blockFromList;
    public RadioButton cancelBlockAll;

    private BlocklistDataSource mBlocklistDataSource;

    // List of phone numbers on blocklist
    public static List<Blocklist> blockList;

    private int selectedRecordPosition = -1;

    public int radioValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_blocklist_btn = (Button) findViewById(R.id.add_blocklist_btn);
        show_blocklist_btn = (Button) findViewById(R.id.show_blocklist_btn);

        blockAll = (RadioButton) findViewById(R.id.block_all);
        blockUnsaved = (RadioButton) findViewById(R.id.block_unsaved);
        blockFromList = (RadioButton) findViewById(R.id.block_from_list);
        cancelBlockAll = (RadioButton) findViewById(R.id.cancel_block_all);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (blockAll.isChecked()) {
                   radioValue = 1;
                    Toast.makeText(MainActivity.this,
                            "Block All Calls", Toast.LENGTH_SHORT).show();
                }
                else if (blockUnsaved.isChecked()) {
                    radioValue = 2;
                    Toast.makeText(MainActivity.this,
                            "Block Unsaved Calls", Toast.LENGTH_SHORT).show();
                }
                else if (blockFromList.isChecked()) {
                    radioValue = 3;
                    Toast.makeText(MainActivity.this,
                            "Block BlackList Calls", Toast.LENGTH_SHORT).show();
                }
                else if (cancelBlockAll.isChecked()) {
                    radioValue = 4;
                    Toast.makeText(MainActivity.this,
                            "Cancel Block All Calls", Toast.LENGTH_SHORT).show();
                }
            }
        });

        add_blocklist_btn.setOnClickListener(this);
        show_blocklist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listview.isShown()) {
                    listview.setVisibility(View.GONE);
                }
                else {
                    listview.setVisibility(View.VISIBLE);
                }
            }
        });

        listview = (ListView) findViewById(R.id.listview);

        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.list_item, listview, false);
        listview.addHeaderView(rowView);

        listview.setOnItemLongClickListener(this);

        listview.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_number:
                startActivity(new Intent(this, AddToBlocklist.class));
                return true;
            case R.id.show_list:
                if (listview.isShown()) {
                    listview.setVisibility(View.GONE);
                }
                else {
                    listview.setVisibility(View.VISIBLE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateNoRecordMsg()
    {
        if(blockList.size() == 0)
        {
            final TextView tv = new TextView(this);
            tv.setPadding(5, 5, 5, 5);
            tv.setTextSize(15);
            tv.setText("No Record Found!");
            listview.addFooterView(tv);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == add_blocklist_btn) {
            startActivity(new Intent(this, AddToBlocklist.class));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        if (position > 0) {
            selectedRecordPosition = position - 1;
            showDialog();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mBlocklistDataSource = new BlocklistDataSource(this);

        blockList = mBlocklistDataSource.getAllBlocklist();

        if(listview.getChildCount() > 1)
            listview.removeFooterView(listview.getChildAt(listview.getChildCount() - 1));

        listview.setAdapter(new CustomArrayAdapter(this, R.layout.list_item, blockList));

        populateNoRecordMsg();
    }

    private void showDialog()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("What do you want to do to the selected number?");

        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {

                            mBlocklistDataSource.delete(blockList.get(selectedRecordPosition));

                            blockList.remove(selectedRecordPosition);
                            listview.invalidateViews();

                            selectedRecordPosition = -1;
                            populateNoRecordMsg();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNeutralButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {

                            startActivity(new Intent(getApplicationContext(), EditBlocklist.class));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class CustomArrayAdapter extends ArrayAdapter<String> {

        private LayoutInflater inflater;

        private List<Blocklist> records;

        @SuppressWarnings("unchecked")
        public CustomArrayAdapter(Context context, int resource, @SuppressWarnings("rawtypes") List objects) {
            super(context, resource, objects);

            this.records = objects;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null)
                convertView = inflater.inflate(R.layout.list_item, parent, false);

            final Blocklist phoneNumber =  records.get(position);

            ((TextView)convertView.findViewById(R.id.serial_tv)).setText("" + (position +1));
            ((TextView)convertView.findViewById(R.id.phone_number_tv)).setText(phoneNumber.phoneNumber);
            return convertView;
        }
    }

    public boolean contactExists(Context context, String number) {

        /// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }
}