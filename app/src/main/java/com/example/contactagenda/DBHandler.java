package com.example.contactagenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    private static final String DB_NAME = "ContactsDB";

    private static final String CONTACTS_TABLE = "contacts";

    private static final String ID = "ID";
    private static final String NAME = "NAME";
    private static final String NUMBER = "NUMBER";
    private static final String EMAIL = "EMAIL";
    private static final String ORGANIZATION = "ORGANIZATION";
    private static final String RELATIONSHIP = "RELATIONSHIP";
    private static final String IMAGE = "IMAGE";


    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + CONTACTS_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NAME + " TEXT," +
                NUMBER + " TEXT," +
                EMAIL + " TEXT," +
                ORGANIZATION + " TEXT," +
                RELATIONSHIP + " TEXT," +
                IMAGE + " TEXT)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE " + CONTACTS_TABLE;
        db.execSQL(sql);
        onCreate(db);
    }

    public long addContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME,contact.getName());
        values.put(NUMBER,contact.getNumber());
        values.put(EMAIL,contact.getEmail());
        values.put(ORGANIZATION,contact.getOrganization());
        values.put(RELATIONSHIP,contact.getRelationship());
        values.put(IMAGE,contact.getImage());


        long id = db.insert(CONTACTS_TABLE,null,values);
        db.close();
        return id;
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                CONTACTS_TABLE,
                new String[]{ID,NAME,NUMBER,EMAIL,ORGANIZATION,RELATIONSHIP,IMAGE},
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
                    cursor.getString(5),
                    cursor.getString(6)
            );
            cursor.close();
            return contact;
        }
        else {
            return null;
        }
    }

    public ArrayList<Contact> getAllContacts(String currentSort){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM " + CONTACTS_TABLE + " ORDER BY "+currentSort;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setOrganization(cursor.getString(4));
                contact.setRelationship(cursor.getString(5));
                contact.setImage(cursor.getString(6));
                contacts.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return contacts;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(NAME,contact.getName());
        values.put(NUMBER,contact.getNumber());
        values.put(EMAIL,contact.getEmail());
        values.put(ORGANIZATION,contact.getOrganization());
        values.put(RELATIONSHIP,contact.getRelationship());
        values.put(IMAGE,contact.getImage());

        return  db.update(
                CONTACTS_TABLE,
                values,
                ID + " = ?",
                new String[]{String.valueOf(contact.getId())}
        );
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(CONTACTS_TABLE,
                ID + " = ?",
                new String[]{String.valueOf(contact.getId())}
        );
        db.close();

    }

    public int getContactCount(){
        SQLiteDatabase db = getReadableDatabase();
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM " + CONTACTS_TABLE;

        Cursor cursor = db.rawQuery(query,null);

        return cursor.getCount();
    }

    public ArrayList<Contact> searchContact(String query){

        ArrayList<Contact> contacts = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String searchQuery =
                "SELECT * FROM " + CONTACTS_TABLE +
                " WHERE " + NAME + " LIKE '%" +query+"%'";

        Cursor cursor = db.rawQuery(searchQuery,null);

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setOrganization(cursor.getString(4));
                contact.setRelationship(cursor.getString(5));
                contact.setImage(cursor.getString(6));
                contacts.add(contact);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return contacts;
    }
}
