package com.cleanup.todoc.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import static com.cleanup.todoc.model.Project.getAllProjects;

@Database(entities = {Task.class, Project.class}, version = 2, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    private static volatile TodocDatabase INSTANCE;
    private static String DATABASE = "todoc";

    public abstract TaskDao taskDao();
    public abstract ProjectDao projectDao();

    public static synchronized TodocDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized(TodocDatabase.class) {
                if(INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, DATABASE)
                            .addCallback(prepopulateDatabase())
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                Project[] allProjects = getAllProjects();

                for (Project element: allProjects) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("id", element.getId());
                    contentValues.put("name", element.getName());
                    contentValues.put("color", element.getColor());
                    db.insert("Project", OnConflictStrategy.IGNORE, contentValues);

                }
            }
        };
    }
}