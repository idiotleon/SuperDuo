package barqsoft.footballscores.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.provider.ScoresContract.ScoresTable;

public class ScoresDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 2;

    public ScoresDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CreateScoresTable = "CREATE TABLE " + ScoresContract.TABLE_NAME + " ("
                + ScoresContract.ScoresTable._ID + " INTEGER PRIMARY KEY,"
                + ScoresContract.ScoresTable.DATE_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.TIME_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.HOME_TEAM_NAME_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.AWAY_TEAM_NAME_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + ScoresContract.ScoresTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + ScoresContract.ScoresTable.MATCH_ID + " INTEGER NOT NULL,"
                + ScoresContract.ScoresTable.MATCH_DAY + " INTEGER NOT NULL,"
                // What a great UNIQUE here!!!!
                + " UNIQUE (" + ScoresContract.ScoresTable.MATCH_ID + ") ON CONFLICT REPLACE"
                + " );";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + ScoresContract.TABLE_NAME);
    }
}
