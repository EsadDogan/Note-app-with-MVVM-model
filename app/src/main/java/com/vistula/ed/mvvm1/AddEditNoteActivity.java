package com.vistula.ed.mvvm1;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.vistula.ed.mvvm1.EXTRA_ID";

    public static final String EXTRA_TITLE = "com.vistula.ed.mvvm1.EXTRA_TITLE";

    public static final String EXTRA_DECRIPTION = "com.vistula.ed.mvvm1.EXTRA_DECRIPTION";

    public static final String EXTRA_PRIORITY = "com.vistula.ed.mvvm1.EXTRA_PRIORITY";


    private EditText edtTitle, edtDescription;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);
        numberPicker = findViewById(R.id.number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(9);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            edtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            edtDescription.setText(intent.getStringExtra(EXTRA_DECRIPTION));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("Add Note");
        }




    }

    private void saveNote() {
        String title = edtTitle.getText().toString();
        String description = edtDescription.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DECRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != 1){
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save_Note) {
            saveNote();
            return true;

        } else return false;
    }
}