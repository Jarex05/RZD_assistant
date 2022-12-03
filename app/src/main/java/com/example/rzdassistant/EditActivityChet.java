package com.example.rzdassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.SortedList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rzdassistant.adapter.ListItemChet;
import com.example.rzdassistant.db.AppExecuter;
import com.example.rzdassistant.db.MyDbManagerChet;

public class EditActivityChet extends AppCompatActivity {
    private EditText edTitleChet, edPiketStartChet, edTitleFinishChet, edPiketFinishChet, edSpeedChet;
    private MyDbManagerChet myDbManagerChet;
    private boolean isEditStateChet = true;
    private ListItemChet item_chet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chet);
        init();
        getMyIntentChet();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbManagerChet.openDbChet();
    }

    private void init(){
        myDbManagerChet = new MyDbManagerChet(this);

        edTitleChet = findViewById(R.id.edTitleChet);
        edPiketStartChet = findViewById(R.id.edPiketStartChet);
        edTitleFinishChet= findViewById(R.id.edTitleFinishChet);
        edPiketFinishChet= findViewById(R.id.edPiketFinishChet);
        edSpeedChet = findViewById(R.id.edSpeedChet);
    }

    private void getMyIntentChet(){

        Intent i_myintentChet = getIntent();
        if (i_myintentChet != null){
            item_chet = (ListItemChet) i_myintentChet.getSerializableExtra(DBHelper.LIST_ITEM_INTENT_CHET);
            isEditStateChet = i_myintentChet.getBooleanExtra(DBHelper.EDIT_STATE_CHET, true);

            if (!isEditStateChet){
                edTitleChet.setText(item_chet.getTitle_chet());
                edPiketStartChet.setText(item_chet.getPiket_start_chet());
                edTitleFinishChet.setText(item_chet.getTitle_finish_chet());
                edPiketFinishChet.setText(item_chet.getPiket_finish_chet());
                edSpeedChet.setText(item_chet.getSpeed_chet());
            }
        }
    }

    public void onClickSaveChet(View view){
        final String titlechet = edTitleChet.getText().toString();
        final String piketstart = edPiketStartChet.getText().toString();
        final String titlefinishchet = edTitleFinishChet.getText().toString();
        final String piketfinish = edPiketFinishChet.getText().toString();
        final String speedchet = edSpeedChet.getText().toString();

        if(titlechet.equals("") || piketstart.equals("") || titlefinishchet.equals("") || piketfinish.equals("") || speedchet.equals("")){

            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();

        }
        else {

            if (isEditStateChet) {

                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManagerChet.insertToDbChet(titlechet, piketstart, titlefinishchet, piketfinish, speedchet);
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
            else {

                AppExecuter.getInstance().getSubIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManagerChet.updateChet(titlechet, piketstart, titlefinishchet, piketfinish, speedchet, item_chet.getId_chet());
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }

            myDbManagerChet.closeDbChet();
            finish();

        }

    }
}