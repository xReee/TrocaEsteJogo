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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.Manifest;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
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

    private TextView name;
    private Button btnGoToJogos;

    //RETIRAR DEPOIS
    private TextView coord1,coord2;

    private boolean MY_PERMISSION_ACCESS_COURSE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);


        name = findViewById(R.id.txtProfileName);
        btnGoToJogos = findViewById(R.id.btnGoJogos);

        click();

        //ACRESCENTEI PARA MOSTRAR A COORDENADA, LEMBRAR DE RETIRAR DEPOIS
        coord1=findViewById(R.id.coord1);
        coord2=findViewById(R.id.coord2);

        //VERIFICA SE O USUÁRIO JÁ DEU PERMISSÃO PARA GPS
        MY_PERMISSION_ACCESS_COURSE_LOCATION = checkLocationPermission();
        //Caso a permissão não tenha sido realizada, pede permissão para o usuário e chama o metodo "onRequestPermissionsResult()"
        if(MY_PERMISSION_ACCESS_COURSE_LOCATION==false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        //Caso o usuário já tenha fornecido a permissão previamente, se atualiza a localização
        else{
            updateLocation();
        }


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

    private void click() {
        btnGoToJogos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jogos = new Intent( Perfil.this, Jogos.class);
                startActivity(jogos);
            }
        });
    }

    //Verifica se tem a permissão para saber a localização do usuario
    public boolean checkLocationPermission(){
        String permission = "android.permission.ACCESS_COARSE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    
    //Efetua a solicitação ao usuario caso não tenha a permissão
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                // Caso o usuário tenha fornecido a permissão, se realiza o update da localização
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateLocation();
                }
                //Caso não tenha sido fornecida a permissão, se retorna ao usuário a informação de que não será possivel realizar tal feito
                else {
                    Toast.makeText(this,"A permissão é necessária para saber os jogos próximos do usuário",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    //Realiza o update da localização do usuário baseando-se no acesso a internet e no gps
    public void updateLocation(){
        try{
            boolean gps_enabled = false;
            boolean network_enabled = false;

            LocationManager lm = (LocationManager) this
                    .getSystemService(Context.LOCATION_SERVICE);

            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location net_loc = null, gps_loc = null, finalLoc = null;

            if (gps_enabled)
                gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (network_enabled)
                net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gps_loc != null && net_loc != null) {

                //smaller the number more accurate result will
                if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                    finalLoc = net_loc;
                else
                    finalLoc = gps_loc;

                // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

            } else {

                if (gps_loc != null) {
                    finalLoc = gps_loc;
                } else if (net_loc != null) {
                    finalLoc = net_loc;
                }
            }



            coord1.setText(java.lang.String.valueOf(finalLoc.getLatitude()));
            coord2.setText(java.lang.String.valueOf(finalLoc.getLongitude()));
        }
        catch (SecurityException e){
            Toast.makeText(this,"falhou",Toast.LENGTH_SHORT).show();
        }
    }


    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()){
            UserInformation uInfo = new UserInformation();

            //set user info
            uInfo.setEmail(ds.child(userID).getValue(UserInformation.class).getEmail());
            uInfo.setNome(ds.child(userID).getValue(UserInformation.class).getNome());

            if (uInfo.getNome() != null) {
                name.setText(uInfo.getNome());
            }


        }
    }
}

