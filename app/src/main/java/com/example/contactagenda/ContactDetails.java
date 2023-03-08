package com.example.contactagenda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactDetails extends AppCompatActivity {

    private TextView nameTv,phoneTv,emailTv,organizationTv,relationshipTv;
    private ImageView imageIv;
    private int id;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);

        dbHandler = new DBHandler(this);

        Intent intent = getIntent();
        Log.d("test", "id = "+intent.getStringExtra("contactId"));
        id = Integer.parseInt(intent.getStringExtra("contactId"));

        nameTv = findViewById(R.id.name_detailsTv);
        phoneTv = findViewById(R.id.phone_detailsTv);
        emailTv = findViewById(R.id.email_detailsTv);
        organizationTv = findViewById(R.id.organization_detailsTv);
        relationshipTv = findViewById(R.id.relationship_detailsTv);
        imageIv = findViewById(R.id.image_detailsIv);

        loadDataById();
    }

    private void loadDataById() {


        Contact contact = dbHandler.getContact(id);

        dbHandler.close();
        if(contact != null){
            nameTv.setText(contact.getName());
            phoneTv.setText(contact.getNumber());
            emailTv.setText(contact.getEmail());
            organizationTv.setText(contact.getOrganization());
            relationshipTv.setText(contact.getRelationship());


            if(contact.getImage().equals("null")){
                imageIv.setImageResource(R.drawable.baseline_person_24);
            }else {
                imageIv.setImageURI(Uri.parse(contact.getImage()));
            }
        }
    }
}