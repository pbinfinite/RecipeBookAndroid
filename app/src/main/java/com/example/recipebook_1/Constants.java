package com.example.recipebook_1;

public class Constants {
    public static final String DB_NAME = "RECIPE_DB";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "RECIPE_TABLE";
    //columns
    public static final String C_ID = "ID";
    public static final String C_TITLE = "TITLE";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_INGREDIENTS = "INGREDIENTS";
    public static final String C_STEPS = "STEPS";
    public static final String C_ADD_TIMESTAMP = "ADD_TIMESTAMP";
    public static final String C_UPDATE_TIMESTAMP = "UPDATE_TIMESTAMP";
    //create query for table
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_TITLE + " TEXT,"
            + C_IMAGE + " TEXT,"
            + C_INGREDIENTS + " TEXT,"
            + C_STEPS + " TEXT,"
            + C_ADD_TIMESTAMP + " TEXT,"
            + C_UPDATE_TIMESTAMP + " TEXT"
            + ");";

}
