package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Usuario

class ControladoraCadastroUsuario : AppCompatActivity() {

    lateinit var botaoEntrar : Button;
    lateinit var campoLogin : EditText;
    lateinit var campoSenha : EditText;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abertura_conta)

    }
}