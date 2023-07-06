package com.vistula.ed.mvvm1;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){

        // synchronized means only one thread can access at this method at the same time, (VERY IMPORTANT)

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class, "note_database").fallbackToDestructiveMigration().addCallback(roomCallBack).build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private  static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        public PopulateDbAsyncTask(NoteDatabase db) {
            this.noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Welcome to Note App!","Please look at my GitHub profile",1));
            noteDao.insert(new Note("My Note","This is my first note on this app",2));
            noteDao.insert(new Note("My New Note","This is my new note",3));
            return null;
        }
    }
}
