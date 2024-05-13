package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import prototipoappbancariomelhorado.main.R

class MainActivity : AppCompatActivity() {

    lateinit var linkCadastro : TextView;
    lateinit var botaoEntrar : Button;
    lateinit var campoLogin : EditText;
    lateinit var campoSenha : EditText;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializaElementosInterfaceGrafica()

        linkCadastro.setOnClickListener()
        {
            var intent = Intent(this, ControladoraAberturaConta::class.java);
            startActivity(intent);
        }

    }

    fun inicializaElementosInterfaceGrafica()
    {
        campoLogin = findViewById(R.id.editUserName)
        campoSenha = findViewById(R.id.editPassword)
        linkCadastro = findViewById(R.id.txtAbrirConta);
        botaoEntrar = findViewById(R.id.botaoDeLogin);

    }
}