package com.example.renata.trocaestejogo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Perfil extends AppCompatActivity {


    private FirebaseAuth auth;
    private FirebaseUser user;

    private TextView email;
    private TextView name;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        setContentView(R.layout.activity_perfil);

        email = findViewById(R.id.txtProfileEmail);
        name = findViewById(R.id.txtProfileName);

        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(auth.getUid()).child("name");

        //name.setText(user.child(auth.getUid()).child("name").toString());

        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue(String.class));
//                Log.i(TAG, dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               // Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
        user = Conexao.getFirebaseUser();
        verificaUser();

    }

    private void logout(){
        Conexao.logOut();
        finish();
    }

    private void verificaUser() {
        if (user == null) {
            finish();
        } else {
            email.setText(user.getEmail());
        }
    }
}
