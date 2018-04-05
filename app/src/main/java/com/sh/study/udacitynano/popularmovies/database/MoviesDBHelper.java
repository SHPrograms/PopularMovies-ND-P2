package com.sh.study.udacitynano.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sh.study.udacitynano.popularmovies.constants.MoviesConstants;
import com.sh.study.udacitynano.popularmovies.database.MoviesContract.MoviesEntry;


/**
 * A helper class for local Database
 *
 * @author Sławomir Hagiel
 * @version 1.0
 * @since 2018-03-20
 */
public class MoviesDBHelper extends SQLiteOpenHelper {
    private static final String CLASS_NAME = "MoviesDBHelper";

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        MoviesConstants.debugTag(CLASS_NAME,"constructor:end");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MoviesConstants.debugTag(CLASS_NAME,"onCreate:start - SQL: " + MoviesEntry.SQL_CREATE_MOVIES_TABLE);
        db.execSQL(MoviesEntry.SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MoviesConstants.debugTag(CLASS_NAME,"onUpgrade:start - SQL: " + MoviesEntry.SQL_DELETE_MOVIES_TABLE);
        db.execSQL(MoviesEntry.SQL_DELETE_MOVIES_TABLE);
        onCreate(db);
    }
}
