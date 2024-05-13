package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO

class ControladoraUsuarioLogado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logado)

    }
}