package com.innerly.app.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Goal.class, Reflection.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract GoalDao goalDao();
    public abstract ReflectionDao reflectionDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "innerly_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}