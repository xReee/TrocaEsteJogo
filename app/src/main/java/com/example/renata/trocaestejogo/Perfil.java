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
//        if (Build.VERSION.SDK_INT < 16) {
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }

        setContentView(R.layout.activity_perfil);

        email = findViewById(R.id.txtProfileEmail);
        name = findViewById(R.id.txtProfileName);

        ///TODO: Recuperar dados do firebase e jogar pra tela de perfil
//        DatabaseReference user = FirebaseDatabase.getInstance().getReference("users").child(auth.getUid()).child("name");

        //name.setText(user.child(auth.getUid()).child("name").toString());

//        user.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                try {
//                    name.setText(dataSnapshot.getValue(String.class));
//                } catch(Exception e) {
//                    Toast.makeText(Perfil.this, "Deu ruim" + dataSnapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//               // Log.w(TAG, "onCancelled", databaseError.toException());
//            }
//        });
      //  mPostReference.addValueEventListener(postListener);


    }



    @Override
    protected void onStart() {
        super.onStart();
//        auth = Conexao.getFirebaseAuth();
//        user = Conexao.getFirebaseUser();
//        verificaUser();

    }


//    //função de logOut já está pronta, mas ainda não a coloquei em nenhum lugar
//    private void logout(){
//        Conexao.logOut();
//        finish();
//    }
//
//    //nada aqui
//    private void verificaUser() {
//        if (user == null) {
//            finish();
//        } else {
//            email.setText(user.getEmail());
//        }
//    }
}
