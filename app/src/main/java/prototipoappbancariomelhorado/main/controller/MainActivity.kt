package prototipoappbancariomelhorado.main.controller

import android.annotation.SuppressLint
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

class MainActivity : AppCompatActivity() {

    lateinit var linkCadastro : TextView;
    lateinit var botaoEntrar : Button;
    lateinit var campoLogin : EditText;
    lateinit var campoSenha : EditText;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializaElementosInterfaceGrafica()

        var usuarioDAO = UsuarioDAO(this)

        var contaDAO = ContaDAO(this)


        botaoEntrar.setOnClickListener()
        {
          if(verificaEntradasVazias(campoLogin.text.toString(), campoSenha.text.toString()))
          {
              Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
          }
          else
          {
             if(fazerLogin(campoLogin.text.toString(), campoSenha.text.toString(), usuarioDAO, contaDAO))
             {
                 var conta = pegarContaUsuarioLogado(campoLogin.text.toString(), campoSenha.text.toString(), contaDAO, usuarioDAO)

                 limparCampos()

                 var intent = Intent(this, ControladoraUsuarioLogado::class.java);
                 intent.putExtra("conta", conta)
                 startActivity(intent);

             }
             else
             {
                 Toast.makeText(this, "Não foi possível fazer o login, por favor verifique os dados informados.", Toast.LENGTH_SHORT).show()
             }
          }
        }

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

    fun verificaEntradasVazias (campoLogin : String, campoSenha: String) : Boolean
    {
        return (campoLogin.isNullOrBlank() || campoLogin.isNullOrEmpty()
                || campoSenha.isNullOrBlank() || campoSenha.isNullOrEmpty())
    }

    fun fazerLogin (login : String, senha : String, usuarioDAO: UsuarioDAO, contaDAO: ContaDAO) : Boolean
    {
      return verificarUsuarioLogado(login, senha, usuarioDAO) && verificaContaUsuarioLogado(login, senha, contaDAO, usuarioDAO)
    }

    fun verificarUsuarioLogado (login : String, senha : String, usuarioDAO: UsuarioDAO) : Boolean
    {
        var usuario = usuarioDAO.pegarDadosUsuarioPorLoginSenha(login, senha);
        return usuario?.getLoginUsuarioAtributo() == login && usuario?.getSenhaUsuarioAtributo() == senha
    }

    fun verificaContaUsuarioLogado (login : String, senha : String, contaDAO: ContaDAO, usuarioDAO: UsuarioDAO) : Boolean
    {
        var usuario = usuarioDAO.pegarDadosUsuarioPorLoginSenha(login, senha);
        var conta = contaDAO.pegarContaUsuario(usuario!!)

        return usuario.getIdUsuarioAtributo() == conta?.getUsuarioAtributo()!!.getIdUsuarioAtributo()

    }

    fun pegarContaUsuarioLogado (login : String, senha : String, contaDAO: ContaDAO, usuarioDAO: UsuarioDAO) : Conta?
    {
        var conta = contaDAO.pegarContaUsuario(usuarioDAO.pegarDadosUsuarioPorLoginSenha(login, senha)!!)

        return conta
    }

    fun limparCampos ()
    {
        campoLogin.text.clear()
        campoSenha.text.clear()
    }
}