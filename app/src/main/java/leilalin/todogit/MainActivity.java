package leilalin.todogit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItems);
        items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter,
                                           View item, int pos, long id) {
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }

        });

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> adapter,
                                        View item, int pos, long id) {
                 String txtEditItem = items.get(pos);
                 launchEditView(txtEditItem, pos, id);
             }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
    }

    private void readItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        } catch (IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final int REQUEST_CODE = 200;
    public void launchEditView(String txtItem, int pos, long id) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        i.putExtra("txtEditItem", txtItem);
        i.putExtra("posEditItem", pos);
        i.putExtra("idEditItem", id);

        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent retData) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String txtRetItem = retData.getExtras().getString("txtEditItem");
            int intRetPos = retData.getExtras().getInt("posEditItem", 0);
            int intRetId = retData.getExtras().getInt("idEditItem", 0);
            int code = retData.getExtras().getInt("resCode", 0);

            String txtOriItem = itemsAdapter.getItem(intRetPos);
            itemsAdapter.remove(txtOriItem);
            itemsAdapter.insert(txtRetItem, intRetPos);
            itemsAdapter.notifyDataSetChanged();
            writeItems();

            Toast.makeText(this, txtRetItem, Toast.LENGTH_SHORT).show();
        }
    }
}
