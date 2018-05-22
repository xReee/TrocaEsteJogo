package com.example.renata.trocaestejogo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NovoJogo extends AppCompatActivity {

    private static final String TAG = "LogPerfil" ;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    private String userID;

    private EditText nome, preco;
    private Switch usado;
    private Button btnAdicionarJogo;

    private String nomeJogo, precoJogo, eUsado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_jogo);

        nome = findViewById(R.id.txtNJogoNome);
        preco = findViewById(R.id.txtNJogoPreco);
        usado = findViewById(R.id.swtNJogoNovo);

        btnAdicionarJogo = findViewById(R.id.btnNJogoAdd);

        //   firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    //sucesso
                    Log.d(TAG, "onAuthStateChanged: " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: signout ");
                }
            }
        };

              click();

    }

    private void click() {
        btnAdicionarJogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 nomeJogo = nome.getText().toString();
                 precoJogo = preco.getText().toString();

                if(usado.isChecked()) {
                    eUsado = "Usado";
                } else {
                    eUsado = "Novo";
                }


                if ( (nomeJogo != "") && (precoJogo != "")){
                    addJogo(nomeJogo, precoJogo, eUsado);
                } else {
                    Toast.makeText(NovoJogo.this, "Algo está em branco", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void addJogo(String nome, String preco, String usado) {
        try {
            mDatabase.child("users").child(userID).child("Jogos").child(nome).child("Nome").setValue(nome);
            mDatabase.child("users").child(userID).child("Jogos").child(nome).child("Preco").setValue(preco);
            mDatabase.child("users").child(userID).child("Jogos").child(nome).child("Usado").setValue(usado);
            mDatabase.child("users").child(userID).child("Jogos").child(nome).child("Ativo").setValue("false");
            mDatabase.child("users").child(userID).child("Jogos").child(nome).child("Trocado").setValue("false");
            Toast.makeText(NovoJogo.this, "Cadastrado!", Toast.LENGTH_LONG).show();
            finish();
        } catch (Exception e ){
            Toast.makeText(NovoJogo.this, "Erro", Toast.LENGTH_LONG).show();
        }
    }

}
