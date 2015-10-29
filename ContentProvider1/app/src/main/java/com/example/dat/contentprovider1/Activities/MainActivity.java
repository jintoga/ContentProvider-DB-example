package com.example.dat.contentprovider1.Activities;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dat.contentprovider1.Adapter.CustomAdapterAnimals;
import com.example.dat.contentprovider1.ContentProvider.MyContentProvider;
import com.example.dat.contentprovider1.CustomDialogEdit;
import com.example.dat.contentprovider1.Database.AnimalTable;
import com.example.dat.contentprovider1.Model.Animal;
import com.example.dat.contentprovider1.R;
import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnDismissCallback {

    private Button btnAdd, buttonClearAll;
    private EditText editTextName, editTextType;
    private ListView listViewAnimals;
    CustomAdapterAnimals customAdapterAnimals;

    private ContentResolver contentResolver;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIDs();
        setEvents();

        if (getAllAnimals() != null) {
            customAdapterAnimals.clearAll();
            customAdapterAnimals.addAll(getAllAnimals());
            customAdapterAnimals.notifyDataSetChanged();
        }
        setAnimationAdapter(customAdapterAnimals);
    }

    private void getIDs() {

        btnAdd = (Button) findViewById(R.id.btnAdd);
        buttonClearAll = (Button) findViewById(R.id.buttonClearAll);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextType = (EditText) findViewById(R.id.editTextType);
        listViewAnimals = (ListView) findViewById(R.id.listViewAnimals);
        customAdapterAnimals = new CustomAdapterAnimals(this, new ArrayList<Animal>()); // add a new empty list
        listViewAnimals.setAdapter(customAdapterAnimals);


    }

    private void setAnimationAdapter(CustomAdapterAnimals customAdapterAnimals) {
        if (listViewAnimals != null) {
            SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(customAdapterAnimals, this));
            swingBottomInAnimationAdapter.setAbsListView(listViewAnimals);
            assert swingBottomInAnimationAdapter.getViewAnimator() != null;
            swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(500);
            listViewAnimals.setAdapter(swingBottomInAnimationAdapter);
        }
    }

    private void setEvents() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                if (!editTextName.getText().toString().isEmpty() && !editTextType.getText().toString().isEmpty()) {
                    contentValues.put(AnimalTable.NAME, editTextName.getText().toString());
                    contentValues.put(AnimalTable.TYPE, editTextType.getText().toString());
                    Uri uri = getContentResolver().insert(MyContentProvider.CONTENT_URI, contentValues);
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    customAdapterAnimals.clearAll();
                    customAdapterAnimals.addAll(getAllAnimals());
                    //customAdapterAnimals.notifyDataSetChanged();
                    setAnimationAdapter(customAdapterAnimals);
                }
            }
        });

        listViewAnimals.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomDialogEdit customDialogEdit = new CustomDialogEdit(MainActivity.this, (Animal) customAdapterAnimals.getItem(position));
                customDialogEdit.show();
                return false;
            }
        });
        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentResolver.delete(MyContentProvider.CONTENT_URI, null, null);
                refreshListAfterDeletion();
            }
        });

    }

    public void refreshListAfterDeletion() {
        if (getAllAnimals() != null) {
            customAdapterAnimals.clearAll();
            customAdapterAnimals.addAll(getAllAnimals());
            customAdapterAnimals.notifyDataSetChanged();
        }
        setAnimationAdapter(customAdapterAnimals);
    }


    private ArrayList<Animal> getAllAnimals() {
        ArrayList<Animal> animals = new ArrayList<>();
        String selection = null;
        contentResolver = getContentResolver();
        cursor = contentResolver.query(MyContentProvider.CONTENT_URI, null, selection, null,
                null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int indexKeyID = cursor.getColumnIndex(AnimalTable.KEY_ID);
                int indexName = cursor.getColumnIndex(AnimalTable.NAME);
                int indexType = cursor.getColumnIndex(AnimalTable.TYPE);
                do {
                    Animal animal = new Animal();
                    animal.setKey_id(cursor.getInt(indexKeyID));
                    animal.setName(cursor.getString(indexName));
                    animal.setType(cursor.getString(indexType));
                    animals.add(animal);
                } while (cursor.moveToNext());
            }
        }
        return animals;
    }

    @Override
    public void onDismiss(ViewGroup listView, int[] reverseSortedPositions) {
        for (int position : reverseSortedPositions) {
            customAdapterAnimals.remove(position);
            Toast.makeText(this, "Database Table Length:" + MyContentProvider.getNumbOfEntries(), Toast.LENGTH_SHORT).show();
        }
    }


}
