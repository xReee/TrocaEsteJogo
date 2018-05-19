/*
*
* Essa é a classe q tem a tela de login
* Basicamente vai ser verificado os dados e permitir que o usuario entre
* Existem dois botões adicionais -> Registro e esqueceu senha
* Cada um leva para sua respectiva tela
*
* */

package com.example.renata.trocaestejogo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    EditText txtEmail, txtSenha;
    Button btnLogar, btnRegistrar, btnEsqueceu;

    private ProgressBar spinner;


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnLogar = findViewById(R.id.btnLogar);
        btnRegistrar = findViewById(R.id.btnRegistro);
        btnEsqueceu = findViewById(R.id.btnEsquecer);

        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        //chama evento para adicionar o click do botão
        eventClick();
    }

    private void eventClick() {
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Cadastro.class);
                startActivity(i);
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    //recupera os dados digitados

                    // btnLogar.setEnabled(false); // kkkk essa foi uma tentativa fail de fazer a requisição esperar

                    String email = txtEmail.getText().toString().trim();
                    String senha = txtSenha.getText().toString().trim();

                    //tenta logar
                    spinner.setVisibility(View.VISIBLE);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    login(email, senha);
                } catch (Exception e) {
                    alert("Algo está errado");
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    spinner.setVisibility(View.GONE);
                }
            }
        });

        //leva pra tela de lembrar senha
        btnEsqueceu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ResetSenha.class);
                startActivity(i);
            }
        });
    }

    //testa e faz o login
    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                    //        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                            //se deu certo, então ele inicia a tela
                            try {
                                Intent i = new Intent(Login.this, Perfil.class);
                                startActivity(i);
                            } catch (Exception e) {
                                alert("erro:" + e );
                            }


                    } else {
                            alert("email ou senha errados");
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            spinner.setVisibility(View.GONE);

                        }

                }

                });
    }

    private void alert(String s) {
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }

    //conecta ao firebase
    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
