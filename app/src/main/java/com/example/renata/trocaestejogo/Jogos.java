package com.example.renata.trocaestejogo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Struct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class Jogos extends AppCompatActivity {

    private ListView listJogos;

    private static final String TAG = "LogPerfil";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;


    List<String> jogos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogos);
        jogos = new ArrayList<String>();
        listJogos = findViewById(R.id.listaJogos);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_add));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addJogo = new Intent(Jogos.this, NovoJogo.class);
                startActivity(addJogo);
            }
        });

        try {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            //myRef = mFirebaseDatabase.getReference();

            FirebaseUser user = mAuth.getCurrentUser();
            userID = user.getUid();

            myRef = mFirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Jogos");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        jogos.clear();

//                        Jogos dadosRecebidos = dataSnapshot.getValue(Jogos.class);
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            jogos.add(snapshot.child("Nome").getValue().toString());
                            }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Jogos.this, android.R.layout.simple_list_item_1, jogos);
                        listJogos.setAdapter(adapter);
                    } catch (Exception e) {
                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e ){}
    }


}
