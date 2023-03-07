package com.example.contactagenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView contactRv;

    private DBHandler dbHandler;

    private AdapterContact adapterContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHandler = new DBHandler(this);

        fab = findViewById(R.id.fab);
        contactRv = findViewById(R.id.contactRv);

        contactRv.setHasFixedSize(true);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // move to new activity to add contact
                Intent intent = new Intent(MainActivity.this, AddContact.class);
                startActivity(intent);
            }
        });

        loadData();
    }

    private void loadData() {
        adapterContact = new AdapterContact(this,dbHandler.getAllContacts());
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}