package com.spsh.spshhealthcare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper { //DBHelper class includes all the CRUD Operations
    public static final String DATABASE_NAME = "UserInfo.db"; //static name for database

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //when changing data base, if you want to keep a backup you can change the version, then you can backup previous version of database
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //this will create a table the first time it is called, if table already exists then nothing will happen, //if you have more than 1 table, you can either have many DBHelper classes or you can do the create table part (line 20-24) multiple times and call that in line 26 with different names
        //Below code is essentially the structure of the query
        String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UsersMaster.Users.TABLE_NAME + " (" + //mind the spaces
                        UsersMaster.Users._ID+ " INTEGER PRIMARY KEY," +  //_ID and _COUNT are default methods that are available when we implement basecolumns, they are like the default columns, _COUNT returns the number of rows you have
                        UsersMaster.Users.COLUMN_NAME_USERNAME+ " TEXT,"+
                        UsersMaster.Users.COLUMN_NAME_PASSWORD+ " TEXT)";
        //we have a method in db called execSQL
        db.execSQL(SQL_CREATE_ENTRIES); //we pass the string from LINE 20 in this
    }
    //*************************************************************************Create method****************************************************************************
    public Long addInfo(String userName, String password){ //inside brackets we put elements we are going to pass
        SQLiteDatabase db = getWritableDatabase(); //this create this db as an instance of Writeable Database so we will be able to add information to it

        //ContentValues class is used to create the object that will contain the username and the password, this will act as a row
        ContentValues values = new ContentValues();
        //adding values
        values.put(UsersMaster.Users.COLUMN_NAME_USERNAME, userName); //the key is the username
        values.put(UsersMaster.Users.COLUMN_NAME_PASSWORD, password);

        //insert query               //table name below
        return db.insert(UsersMaster.Users.TABLE_NAME, null, values);
    }

    //**************************************************************************Read method******************************************************************************
    public List readAll(){
        SQLiteDatabase db = getReadableDatabase(); //for create function we used getWritableDatabase()

        //creating projection, this is where you get information like ID, Username and password
        String [] projection = {
            //ids
            UsersMaster.Users._ID,
            UsersMaster.Users.COLUMN_NAME_USERNAME,
            UsersMaster.Users.COLUMN_NAME_PASSWORD
        }; //projection done

        //sorting in order
        String sortOrder = UsersMaster.Users._ID + " DESC"; //you must use capital letters because this is a query

        Cursor cursor = db.query(   //this is what goes through the table and take the information
                UsersMaster.Users.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        ); //cursor complete

        List info = new ArrayList<>();

        while (cursor.moveToNext()){ //loop that will stop when all elements are checked     //must pass column name here
            String userName = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_NAME_USERNAME)); //taking string from table
            String password = cursor.getString(cursor.getColumnIndexOrThrow(UsersMaster.Users.COLUMN_NAME_PASSWORD));
            //if there are 10 records, one by one all 10 will be taken (userName will be 10 times refreshed)

            info.add(userName+":"+password); //passing concatenated objects
        }
        cursor.close(); //closing the cursor, this must be done

        return info; //if there are many columns then the while loop can be implemented in the logic level (main activity) and we can return the cursor in this function
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //**************************************************************************Delete method******************************************************************************
    public  void deleteInfo(View view, String userName){
        SQLiteDatabase db = getReadableDatabase(); //this should be readable database because, we are going to check whether data is available when you are going to execute the query

        String selection = UsersMaster.Users.COLUMN_NAME_USERNAME + " LIKE ?";//here we check username whether the username being passed to function(line 92) is same as the selection variable
        String[] stringArgs = {userName}; //passing arguments as arrays //you can delete multiple stuff if you add argument as an array

        int count = db.delete(UsersMaster.Users.TABLE_NAME,selection,stringArgs); //if you know the exact delete query, you can do db.executeSQL and execute it

        //displaying count using Snack bar
        Snackbar snackbar = Snackbar.make(view, count + " rows affected",Snackbar.LENGTH_LONG);
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }

    //**************************************************************************Update method******************************************************************************
    public void updateInfo(View view, String userName, String password){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues(); //creates an object that looks like a row

        values.put(UsersMaster.Users.COLUMN_NAME_PASSWORD,password); //in this case we are only changing the password so that is the only thing passed

        String selection = UsersMaster.Users.COLUMN_NAME_USERNAME + " LIKE ?"; //for the project we can use the id and access an entry to update whatever attributes(such as username and password) //When we pass arguments question mark will be replaced by the arguments
        String[] selectionArgs = {userName}; //this is same as in Delete

        int count = db.update( //when calling db.update() method, it returns the affected number of rows, if there are same usernames then all of them will be updated by the same password, count holds that integer number
                UsersMaster.Users.TABLE_NAME,
                values,
                selection,
                selectionArgs
                //if you want to update multiple columns, line 107 will be repeated, and line 114 will cover for as many columns as necessary
        );

        //displaying count using Snack bar
        Snackbar snackbar = Snackbar.make(view, count + " rows affected",Snackbar.LENGTH_LONG); //the view being passed is the entire UI
        snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        snackbar.show();
    }
}
