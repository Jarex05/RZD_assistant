package com.example.rzdassistant;

        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.example.rzdassistant.adapter.ListItemChet;
        import com.example.rzdassistant.adapter.ListItemNechet;
        import com.example.rzdassistant.db.AppExecuter;
        import com.example.rzdassistant.db.AppExecuterNechet;
        import com.example.rzdassistant.db.MyDbManagerNechet;

public class EditActivityNechet extends AppCompatActivity {
    private EditText edTitleNechet, edPiketStartNechet, edTitleFinishNechet, edPiketFinishNechet, edSpeedNechet;
    private MyDbManagerNechet myDbManagerNechet;
    private boolean isEditStateNechet = true;
    private ListItemNechet item_nechet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_nechet);
        init();
        getMyIntentNechet();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myDbManagerNechet.openDbNechet();
    }

    private void init(){
        myDbManagerNechet = new MyDbManagerNechet(this);

        edTitleNechet= findViewById(R.id.edTitleNechet);
        edPiketStartNechet= findViewById(R.id.edPiketStartNechet);
        edTitleFinishNechet= findViewById(R.id.edTitleFinishNechet);
        edPiketFinishNechet= findViewById(R.id.edPiketFinishNechet);
        edSpeedNechet = findViewById(R.id.edSpeedNechet);
    }

    private void getMyIntentNechet(){

        Intent i_myintentNechet = getIntent();
        if (i_myintentNechet != null){
            item_nechet = (ListItemNechet) i_myintentNechet.getSerializableExtra(DBHelperNechet.LIST_ITEM_INTENT_NECHET);
            isEditStateNechet = i_myintentNechet.getBooleanExtra(DBHelperNechet.EDIT_STATE_NECHET, true);

            if (!isEditStateNechet){
                edTitleNechet.setText(item_nechet.getTitle_nechet());
                edPiketStartNechet.setText(item_nechet.getPiket_start_nechet());
                edTitleFinishNechet.setText(item_nechet.getTitle_finish_nechet());
                edPiketFinishNechet.setText(item_nechet.getPiket_finish_nechet());
                edSpeedNechet.setText(item_nechet.getSpeed_nechet());
            }
        }
    }

    public void onClickSaveNechet(View view){
        final String titlenechet = edTitleNechet.getText().toString();
        final String piketstartnechet = edPiketStartNechet.getText().toString();
        final String titlefinishnechet = edTitleFinishNechet.getText().toString();
        final String piketfinishnechet = edPiketFinishNechet.getText().toString();
        final String speednechet = edSpeedNechet.getText().toString();

        if(titlenechet.equals("") || piketstartnechet.equals("") || titlefinishnechet.equals("") || piketfinishnechet.equals("") || speednechet.equals("")){

            Toast.makeText(this, R.string.text_empty, Toast.LENGTH_SHORT).show();

        }
        else {

            if (isEditStateNechet) {

                AppExecuterNechet.getInstance().getSubIoNechet().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManagerNechet.insertToDbNechet(titlenechet, piketstartnechet, titlefinishnechet, piketfinishnechet, speednechet);
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }
            else {
                AppExecuterNechet.getInstance().getSubIoNechet().execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbManagerNechet.updateNechet(titlenechet, piketstartnechet, titlefinishnechet, piketfinishnechet, speednechet, item_nechet.getId_nechet());
                    }
                });
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
            }

            myDbManagerNechet.closeDbNechet();
            finish();

        }

    }
}