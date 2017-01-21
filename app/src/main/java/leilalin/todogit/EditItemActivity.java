package leilalin.todogit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

//import static android.R.attr.data;
//import static android.R.attr.id;

public class EditItemActivity extends AppCompatActivity {
    EditText txtEtItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        String txtEditItem = getIntent().getStringExtra("txtEditItem");

        txtEtItem = (EditText) findViewById(R.id.txtEdit);
        txtEtItem.setText(txtEditItem);
        setupEditTextListener();
    }

    private void setupEditTextListener() {
        txtEtItem.setOnFocusChangeListener(new AdapterView.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    txtEtItem.setSelection(txtEtItem.getText().length());
                }
            }
        });
    }

    public void onSave(View v) {
        EditText txtRetItem = (EditText) findViewById(R.id.txtEdit);
        Intent retData = new Intent();
        int posEditItem = getIntent().getIntExtra("posEditItem", 0);
        int idEditItem = getIntent().getIntExtra("idEditItem", 0);

        retData.putExtra("txtEditItem", txtRetItem.getText().toString());
        retData.putExtra("posEditItem", posEditItem);
        retData.putExtra("idEditItem", idEditItem);
        retData.putExtra("resCode", 200);

        setResult(RESULT_OK, retData);
        this.finish();
    }
}
