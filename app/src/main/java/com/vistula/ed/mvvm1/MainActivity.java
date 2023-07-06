package com.vistula.ed.mvvm1;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {


    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.mainRecView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton btnAddNote = findViewById(R.id.btnAddNote);
        btnAddNote.setOnClickListener(v -> {

                    Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                    startActivityForResult(intent, ADD_NOTE_REQUEST);

//            ActivityResultLauncher<Intent> startActivityIntent = registerForActivityResult(
//                    new ActivityResultContracts.StartActivityForResult(),
//                    result -> {
//                        // Add same code that you want to add in onActivityResult method
//                    });
//


                }
        );

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, notes -> {
            // update our RecyclerView
            adapter.submitList(notes);
            Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(note -> {

            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class).
                    putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle()).
                    putExtra(AddEditNoteActivity.EXTRA_DECRIPTION, note.getDescription()).
                    putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority()).
                    putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
            startActivityForResult(intent, EDIT_NOTE_REQUEST);
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllNotes) {
            noteViewModel.deleteAllNotes();
            Toast.makeText(MainActivity.this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Opps! Something went wrong.", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, "Note Updated.", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
        }
    }
}