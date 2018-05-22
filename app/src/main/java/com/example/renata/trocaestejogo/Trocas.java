package com.example.renata.trocaestejogo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trocas extends AppCompatActivity {

    private static final String TAG = "LOGTROCAS";
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String userID;

    private TextView username, gamename, julgouTodos;
    private Button btnAceitar, btnRecusar;
    private LinearLayout contentPrincipal;

    private Map<String, List<String>> users;
    private List<String> listaDeUsuarios;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trocas);

        username = findViewById(R.id.lblTrocasNomeUsuario);
        gamename = findViewById(R.id.lblTrocasNomeJogo);
        julgouTodos = findViewById(R.id.lblNinguem);

        contentPrincipal = findViewById(R.id.TrocasLayout);
        contentPrincipal.setVisibility(contentPrincipal.VISIBLE);

        julgouTodos.setVisibility(julgouTodos.INVISIBLE);

        btnRecusar = findViewById(R.id.btnRecusar);

        users  = new HashMap<>();
        listaDeUsuarios = new ArrayList<>();


        try {
            mAuth = FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();
            userID = user.getUid();

            myRef = mFirebaseDatabase.getInstance().getReference().child("users");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
/// TODO: TEM QUE DESCOBRIR UM JEITO DE PEGAR OS USUARIOS E OS JOGOS DELES, N SEI COMO FAZER :(
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (!snapshot.getKey().equals(userID) && snapshot.hasChild("Jogos")) {
                                //users.add(snapshot.child("nome").getValue().toString());
                                List<String> listaJogos = new ArrayList<>();
                                for (DataSnapshot jogosDoUser : snapshot.child("Jogos").getChildren()){
                                    listaJogos.add(jogosDoUser.child("Nome").getValue().toString());
                                }
                                users.put(snapshot.child("nome").getValue().toString(), listaJogos);
                                listaDeUsuarios.add(snapshot.child("nome").getValue().toString());
                            }
                        }

                        if (users.size() > 1) {
                            String usuario = listaDeUsuarios.get(listaDeUsuarios.size() - 1 );
                            username.setText(usuario);
                            gamename.setText(users.get(usuario).get(users.get(usuario).size() - 1));
                        }

                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e ){}

       click();
    }

    private void click() {
        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 *   verificar se tem mais jogos
                 *   se n tiver tira usario
                 * */
                try {
                    if (!listaDeUsuarios.isEmpty() && listaDeUsuarios.size() > 1 ) {
                        //
                        String usuario = listaDeUsuarios.get(listaDeUsuarios.size() - 1);

                        if (!users.get(usuario).isEmpty() && users.get(usuario).size() > 1 ) {
                            users.get(usuario).remove(users.get(usuario).size() - 1);
                        } else {
                            listaDeUsuarios.remove(listaDeUsuarios.size() - 1);
                            users.remove(users.size() - 1);
                            usuario = listaDeUsuarios.get(listaDeUsuarios.size() - 1);
                        }
                        username.setText(usuario);
                        gamename.setText(users.get(usuario).get(users.get(usuario).size() - 1));

                    }else{
                        contentPrincipal.setVisibility(contentPrincipal.INVISIBLE);

                        julgouTodos.setVisibility(julgouTodos.VISIBLE);
                    }


                } catch (Exception e) {
                }
            }
        });

        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 *   verificar se tem mais jogos
                 *   se n tiver tira usario
                 * */
                try {
                    if (!listaDeUsuarios.isEmpty() && listaDeUsuarios.size() > 1 ) {
                        //
                        String usuario = listaDeUsuarios.get(listaDeUsuarios.size() - 1);

                        if (!users.get(usuario).isEmpty() && users.get(usuario).size() > 1 ) {
                            users.get(usuario).remove(users.get(usuario).size() - 1);
                        } else {
                            listaDeUsuarios.remove(listaDeUsuarios.size() - 1);
                            users.remove(users.size() - 1);
                            usuario = listaDeUsuarios.get(listaDeUsuarios.size() - 1);
                        }
                        username.setText(usuario);
                        gamename.setText(users.get(usuario).get(users.get(usuario).size() - 1));

                    }else{
                        contentPrincipal.setVisibility(contentPrincipal.INVISIBLE);

                        julgouTodos.setVisibility(julgouTodos.VISIBLE);
                    }


                } catch (Exception e) {
                }
            }
        });
    }
}
