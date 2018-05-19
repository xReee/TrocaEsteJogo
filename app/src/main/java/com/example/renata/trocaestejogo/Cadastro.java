

/*
Essa é a tela de cadastro
Basicamente o usuário preenche tudo e aperta no botão de cadastrar
TODO: Conseguir salvar imagem de perfil no firebase storage
TODO: Fazer o usuário esperar o processo de cadastro terminar

*/

package com.example.renata.trocaestejogo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import static android.app.PendingIntent.getActivity;

public class Cadastro extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 1;
    private static final int PICK_IMAGE_REQUEST = 1 ;
    private  EditText txtEmail, txtSenha;
    EditText txtNome;
    private Button btnRegistrar, btnCancelar;
    private ImageButton btnTrocaFoto;
    private ImageView imgPerfil;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseStorage storageRef = FirebaseStorage.getInstance();
    private Uri filePath;


    private String nome, email, senha;
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
//
        txtNome = findViewById(R.id.txtNomeCadastro);
        txtEmail = findViewById(R.id.txtEmail);
        txtSenha = findViewById(R.id.txtSenha);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnTrocaFoto = findViewById(R.id.btnMudarFoto);
        imgPerfil = findViewById(R.id.imgCadastroFoto);

        //instancia do firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();

//        adiciona os eventos de click
        eventoClick();
    }
//

    private void eventoClick() {
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pega os dados digitados para salvar no banco
                nome =  txtNome.getText().toString().trim();
                email = txtEmail.getText().toString().trim();
                senha = txtSenha.getText().toString().trim();


                if (nome == "" || email == "" || senha == ""  ) {
                    Toast.makeText(Cadastro.this, "Algo em branco!",Toast.LENGTH_LONG).show();
                } else {
                    //cria um usuario e chama a função de salvar no banco
                    User newUser = new User(email, senha, nome, imgPerfil);

                    criarUser(newUser);
                }



            }
        });

        //chama o botão de importar foto
        btnTrocaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        });

    }

    //aqui é só pra pegar a foto da galeria
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if(reqCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgPerfil.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


//        if (resultCode == RESULT_OK ) {
//            try {
//                final Uri imageUri = data.getData();
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imgPerfil.setImageBitmap(selectedImage);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                Toast.makeText(Cadastro.this, "Something went wrong", Toast.LENGTH_LONG).show();
//            }
//
//        }else {
//            Toast.makeText(Cadastro.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
//        }
    }

    //autentico e dps guardo o registro no firebase
    private void criarUser(final User newUser) {
        auth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getSenha())
                .addOnCompleteListener(Cadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //se deu certo  o email e a senha então eu salvo o registo no banco
                            writeNewUser(auth.getUid(), newUser);
                            alert("Usuário cadastro com sucesso");
                            Intent i = new Intent(Cadastro.this, Perfil.class);
                            startActivity(i);
                            finish();
                        }else {
                            alert("Erro de cadastro");
                        }
                    }
                });
    }

    private  void alert(String msg) {
        Toast.makeText(Cadastro.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void writeNewUser(String uid, User user) {
           try {
               //pego meu novo registro (de autenticação e salvo no banco)
               mDatabase.child("users").child(uid).setValue(user.getEmail());
               mDatabase.child("users").child(uid).child("email").setValue(user.getEmail());
               mDatabase.child("users").child(uid).child("nome").setValue(user.getNome());

               /// TODO: ENVIAR IMAGEM PRO STORAGE DO FIREBASE

               StorageReference storageRef = this.storageRef.getReference().child("users").child(uid);


               //mDatabase.child("users").child(uid).child("foto").setValue(user.getFoto().getDrawable());
               //user.getFoto().getDrawable()
//               uploadImage(uid, "perfil");

           }
        catch ( Exception e) {
            Toast.makeText(Cadastro.this, "Algum erro ao salvar",Toast.LENGTH_LONG).show();

        }
    }

    ///TODO: Tem essa função mas ela n está pronta
    private void uploadImage(String uid, String local) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference storRef = storageRef.getReferenceFromUrl("images/");

           // StorageReference ref = storageRef.child("images");
            storRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Cadastro.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Cadastro.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conexao.getFirebaseAuth();
    }
}
