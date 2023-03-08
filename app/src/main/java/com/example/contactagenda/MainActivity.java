package com.example.contactagenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView contactRv;

    private DBHandler dbHandler;

    private AdapterContact adapterContact;

    private String sortByNameAsc = "NAME ASC";
    private String sortByNameDesc = "NAME DESC";

    private String currentSort = sortByNameAsc;


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
                intent.putExtra("isEditMode",false);
                startActivity(intent);
            }
        });

        loadData(currentSort);
    }

    private void loadData(String currentSort) {
        adapterContact = new AdapterContact(this,dbHandler.getAllContacts(currentSort));
        contactRv.setAdapter(adapterContact);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(currentSort);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_top_menu,menu);

        MenuItem item = menu.findItem(R.id.searchContact);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContact(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContact(newText);
                return true;
            }
        });
        return true;
    }

    private void searchContact(String query) {
        adapterContact = new AdapterContact(this,dbHandler.searchContact(query));
        contactRv.setAdapter(adapterContact);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sortContacts:
                sortDialog();
                break;
        }

        return true;
    }

    private void sortDialog() {
        String[] option = {"A-Z","Z-A"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort By");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    loadData(sortByNameAsc);
                }else if(which == 1){
                    loadData(sortByNameDesc);
                }else{
                    loadData(sortByNameAsc);
                }
            }
        });
        builder.create().show();
    }
}