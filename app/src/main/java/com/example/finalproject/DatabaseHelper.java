package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context, @Nullable String name,
                          @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlStatement= "CREATE TABLE " + DatabaseContract.UserEntry.TABLE_NAME + "(" +
                DatabaseContract.UserEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DatabaseContract.UserEntry.COLUMN_NAME + " TEXT)";

        SQLiteStatement statement= db.compileStatement(sqlStatement);
        statement.execute();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long addHistory(History history){

        SQLiteDatabase database = getWritableDatabase();

        String sqlQuery= "INSERT INTO " + DatabaseContract.UserEntry.TABLE_NAME +
                "(" + DatabaseContract.UserEntry.COLUMN_NAME + ") VALUES (?)";

        SQLiteStatement statement= database.compileStatement(sqlQuery);
        String name= history.getSearched();

        statement.bindString(1,name);

        long rowID= statement.executeInsert();
        database.close();

        return rowID;
    }

    public List<History> getAllHistory(){

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        List<History> histories = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + DatabaseContract.UserEntry.TABLE_NAME;
        Cursor resultSet= sqLiteDatabase.rawQuery(sqlQuery, null); //null because we are selecting all

        if(resultSet.moveToFirst()){
            do{
                String userName= resultSet.getString(1);
                History history= new History(userName);
                histories.add(history);
                long user_id = resultSet.getLong(0);
                history.setId(user_id);
            }
            while(resultSet.moveToNext());
        }
        resultSet.close();
        sqLiteDatabase.close();
        return histories;
    }

    public int deleteUser(History history){
        SQLiteDatabase database = getWritableDatabase(); //we are changing it

        String sqlQuery= "DELETE FROM " + DatabaseContract.UserEntry.TABLE_NAME +
                " where " + DatabaseContract.UserEntry.COLUMN_ID + " = ?";

        SQLiteStatement statement = database.compileStatement(sqlQuery);
        statement.bindLong(1,history.getId()); // to find that particular

        int numRows =statement.executeUpdateDelete();
        database.close();
        return numRows;
    }
}
