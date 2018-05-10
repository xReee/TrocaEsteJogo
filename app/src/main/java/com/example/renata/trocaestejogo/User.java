

// Essa é a classe do usuário, está meio desorganizada, mas funciona bem xD

package com.example.renata.trocaestejogo;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

public class User {
    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getNome() {
        return nome;
    }

    public ImageView getFoto() {
        return foto;
    }

    private String email, senha, nome;
    private ImageView foto;

    public User(String email, String senha, String nome, ImageView foto) {
        this.email = email;
        this.senha = senha;
        this.nome = nome;
        if (foto.getDrawable() == null){
            foto.setImageResource(R.drawable.profile_empty);
        }
    }



}
