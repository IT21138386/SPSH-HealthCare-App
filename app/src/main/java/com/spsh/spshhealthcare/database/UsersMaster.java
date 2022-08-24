package com.spsh.spshhealthcare.database;

import android.provider.BaseColumns;

public final class UsersMaster { //why is this final?
    private UsersMaster(){} //constructor

    //Users is an inner class
    public static class Users implements BaseColumns { //BaseColumns act as the structure to the table we are about to make in the database
        //since this class is static you don't have to create objects from this class
        //if you want to create another table then you can create another inner class like Users

        //this class will not have a constructor since it is static
        public static final String TABLE_NAME = "users"; //table name
        public static final String COLUMN_NAME_USERNAME = "username"; //table name
        public static final String COLUMN_NAME_PASSWORD = "password"; //table name
    }
}
