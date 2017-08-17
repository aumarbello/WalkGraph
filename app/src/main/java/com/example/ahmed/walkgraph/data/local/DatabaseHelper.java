package com.example.ahmed.walkgraph.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ahmed.walkgraph.utils.AppConstants.*;

/**
 * Created by ahmed on 8/10/17.
 */

class DatabaseHelper extends SQLiteOpenHelper {
    DatabaseHelper(Context context) {
        super(context, dataBaseName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + locationTable + "(" +
                " _id integer primary key autoincrement, " +
                latitude + ", " +
                longitude + ", " +
                location_date +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
