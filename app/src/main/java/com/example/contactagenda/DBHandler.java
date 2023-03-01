package com.example.contactagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String DB_NAME = "ContactsDB";

    private static final String CONTACTS_TABLE = "contacts";

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String NUMBER = "number";
    private static final String EMAIL = "email";
    private static final String ORGANIZATION = "organization";
    private static final String RELATIONSHIP = "relationship";


    public DBHandler(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE + "(" +
                ID + " int PRIMARY KEY," +
                NAME + " TEXT," +
                NUMBER + " TEXT," +
                EMAIL + " TEXT," +
                ORGANIZATION + " TEXT," +
                RELATIONSHIP + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE " + CONTACTS_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public void addContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID,contact.getId());
        values.put(NAME,contact.getName());
        values.put(NUMBER,contact.getNumber());
        values.put(EMAIL,contact.getEmail());
        values.put(ORGANIZATION,contact.getOrganization());
        values.put(RELATIONSHIP,contact.getRelationship());

        db.insert(CONTACTS_TABLE,null,values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                CONTACTS_TABLE,
                new String[]{ID,NAME,NUMBER,EMAIL,ORGANIZATION,RELATIONSHIP},
                ID + "=?",
                new String[]{String.valueOf(id)},
                null,null,null,null
        );
        Contact contact;
        if(cursor != null){
            cursor.moveToFirst();
            contact = new Contact(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
            cursor.close();
            return contact;
        }
        else {
            return null;
        }
    }
}
