/*
*
* Não terminei essa tela, na vdd não fiz quase nada
*
*
* Mas essa é a tela de perfil, aqui o usuario pode editar sua foto e configurações
* Além disso, adicionar novos jogos e/ou editar os já existentes
*
*
TODO:  1) Recuperar dados do firebase
TODO:  2) Colocar os dados presentes na tela de forma 'bonita'
TODO:  3) Fazer uma tela para editar esse perfil
TODO:  4) Adicionar jogos
TODO:  5) LogOut
*
*/



package com.example.renata.trocaestejogo;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

public class Perfil extends AppCompatActivity {

    private static final String TAG = "LogPerfil" ;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private TextView email;
    private TextView name;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        email = findViewById(R.id.txtProfileEmail);
        name = findViewById(R.id.txtProfileName);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //sucesso
                    Log.d(TAG, "onAuthStateChanged: " + user.getUid());
                    email.setText(user.getEmail());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signout ");
                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();

            //set user info
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
            uInfo.setNome(ds.child(userID).getValue(UserInformation.class).getNome());

            if (uInfo.getNome() != null) {
                name.setText(uInfo.getNome());
                email.setText(uInfo.getEmail());
            }


        }
    }
}

