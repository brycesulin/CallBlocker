package com.bignerdranch.android.callblocker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by brycesulin
 */

public class EditBlocklist extends Activity implements OnClickListener {
    MainActivity main = new MainActivity();

    private EditText country_code_et, phone_et;
    private Button reset_btn, submit_btn;

    private BlocklistDataSource mBlocklistDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editblocklist);

        mBlocklistDataSource = new BlocklistDataSource(this);

        country_code_et = (EditText) findViewById(R.id.country_code_et);
        phone_et = (EditText) findViewById(R.id.phone_et);
        reset_btn = (Button) findViewById(R.id.reset_btn);
        submit_btn = (Button) findViewById(R.id.submit_btn);

        reset_btn.setOnClickListener(this);
        submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            if (v == submit_btn) {
                if (country_code_et.getText().toString().trim().length() > 0 &&
                        phone_et.getText().toString().trim().length() > 0) {
                    final Blocklist phone = new Blocklist();

                    phone.phoneNumber = "+" + country_code_et.getText().toString() + phone_et.getText().toString();

                    mBlocklistDataSource.edit(phone);

                    showDialog();
                } else {
                    showMessageDialog("All fields are mandatory!");
                }
            } else if (v == reset_btn) {
                reset();
            }
        }

    private void reset()
    {
        country_code_et.setText("");
        phone_et.setText("");
    }

    private void showDialog()
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setMessage("Phone Number changed successfully!");

        alertDialogBuilder.setPositiveButton("Edit Again",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        reset();
                    }
                });

        alertDialogBuilder.setNegativeButton("Continue",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showMessageDialog(final String message)
    {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
