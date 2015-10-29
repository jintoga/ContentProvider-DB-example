package com.example.dat.contentprovider1;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dat.contentprovider1.Activities.MainActivity;
import com.example.dat.contentprovider1.ContentProvider.MyContentProvider;
import com.example.dat.contentprovider1.Database.AnimalTable;
import com.example.dat.contentprovider1.Model.Animal;

/**
 * Created by Nguyen on 10/29/2015.
 */
public class CustomDialogEdit extends Dialog {
    private Activity activity;
    private Button buttonEdit;
    private EditText editTextName, editTextType;
    private Animal selectedAnimal;

    public CustomDialogEdit(Activity activity, Animal animal) {
        super(activity);
        this.activity = activity;
        this.selectedAnimal = animal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        setContentView(R.layout.custom_dialog_edit);
        getIDs();
        setEvents();
    }


    private void getIDs() {
        editTextName = (EditText) findViewById(R.id.editTextEditName);
        editTextType = (EditText) findViewById(R.id.editTextEditType);
        buttonEdit = (Button) findViewById(R.id.btnEdit);

        if (selectedAnimal != null) {
            editTextName.setText(selectedAnimal.getName());
            editTextType.setText(selectedAnimal.getType());
        }
    }

    private void setEvents() {
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAnimal();
                CustomDialogEdit.this.dismiss();
                ((MainActivity) activity).refreshListAfterDeletion();
            }
        });
    }

    private void editAnimal() {
        ContentResolver contentResolver = activity.getContentResolver();
        if (contentResolver != null) {
            if (!editTextName.getText().toString().isEmpty() && !editTextType.getText().toString().isEmpty()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(AnimalTable.NAME, editTextName.getText().toString());
                contentValues.put(AnimalTable.TYPE, editTextType.getText().toString());

                int key_id = selectedAnimal.getKey_id();
                contentResolver.update(MyContentProvider.CONTENT_URI, contentValues, AnimalTable.KEY_ID + " = " + String.valueOf(key_id), null);
                Toast.makeText(activity, "Edited", Toast.LENGTH_LONG).show();
            }
        }
    }
}
