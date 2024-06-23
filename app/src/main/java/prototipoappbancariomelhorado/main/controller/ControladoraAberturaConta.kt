package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.BancoDeDados
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ControladoraAberturaConta : AppCompatActivity() {

    lateinit var botaoAbrirConta : Button;
    lateinit var botaoVoltar : TextView;
    lateinit var botaoRegistros : Button;
    lateinit var campoNome : EditText;
    lateinit var campoDataNascimento : EditText;
    lateinit var campoLogin : EditText;
    lateinit var campoSenha : EditText;
    lateinit var dialog : AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abertura_conta)

        inicializaElementosInterfaceGrafica()

        var usuarioDAO = UsuarioDAO(this)

        var contaDAO = ContaDAO(this)


        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }


        botaoRegistros.setOnClickListener()
        {

            var registros = contaDAO.pegarContasComoTexto(usuarioDAO.pegarDadosUsuarios())

            var intent = Intent(this, ControladoraVisualizacaoRegistros::class.java);
            intent.putExtra("registros", registros)
            startActivity(intent);
        }


        botaoAbrirConta.setOnClickListener()
        {
          if(verificaEntradasVazias(campoNome.text.toString(), campoDataNascimento.text.toString(),
              campoLogin.text.toString(), campoSenha.text.toString()))
          {
              Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
          }
          else
          {
              //verificar se o login ja se encontra cadastrado.

              if(verificaLoginInformado(campoLogin.text.toString(), usuarioDAO))
              {
                  Toast.makeText(this, "O Login Informado já foi cadastrado!", Toast.LENGTH_SHORT).show()
              }
              else
              {
                  var dataNascimento : LocalDate

                  try {

                      var dataFormatada : DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                      dataNascimento = LocalDate.parse(campoDataNascimento.text.toString(), dataFormatada);

                      var conta = montarConta(campoNome.text.toString(), dataNascimento,
                          campoLogin.text.toString(), campoSenha.text.toString())

                      if(contaDAO.cadastrarConta(conta))
                      {
                          limparCampos();
                          caixaDeDialogoSucessoAberturaConta()
                      }
                      else
                      {
                          caixaDeDialogoImpossibilidadeAberturaConta()
                      }

                  }
                  catch (e: Exception)
                  {
                      caixaDeDialogoErroData()
                  }

              }

          }

        }

    }

    fun inicializaElementosInterfaceGrafica()
    {
        campoNome = findViewById(R.id.editNome)
        campoDataNascimento = findViewById(R.id.editDataNascimento)
        campoLogin = findViewById(R.id.editUsuario)
        campoSenha = findViewById(R.id.editSenha)
        botaoVoltar = findViewById(R.id.botaoVoltar)
        botaoAbrirConta = findViewById(R.id.botaoDeAbrirConta)
        botaoRegistros = findViewById(R.id.botaoDeRegisto)

    }

    fun verificaEntradasVazias (campoNome : String, campoDataNascimento: String,
                                campoLogin : String, campoSenha: String) : Boolean
    {
        return (campoNome.isNullOrBlank() || campoNome.isNullOrEmpty()
                || campoDataNascimento.isNullOrBlank() || campoDataNascimento.isNullOrEmpty()
                || campoLogin.isNullOrBlank() || campoLogin.isNullOrEmpty()
                || campoSenha.isNullOrBlank() || campoSenha.isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoNome.text.clear();
        campoDataNascimento.text.clear();
        campoLogin.text.clear();
        campoSenha.text.clear();
    }

    fun montarConta (campoNome : String, campoDataNascimento: LocalDate,
                     campoLogin : String, campoSenha: String) : Conta
    {
            var usuario = Usuario(0, campoNome, campoDataNascimento, campoLogin, campoSenha);

            var conta = Conta(0, usuario, 0.0);

        return conta
    }


    fun verificaLoginInformado (login : String, usuarioDAO: UsuarioDAO) : Boolean
    {
        var  loginRecebido = login.trim();

        var loginEncontrado = usuarioDAO.verificaExistenciaLoginInformado(loginRecebido).trim()

        return loginEncontrado.lowercase() == loginRecebido || loginEncontrado.uppercase() == loginRecebido
    }

    fun caixaDeDialogoSucessoAberturaConta()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_abertura_conta, null)

        build.setView(view);

        var botaoOk = view.findViewById<Button>(R.id.botaoOk);
        botaoOk.setOnClickListener{dialog.dismiss()
            this.finish();
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoErroData()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_erro_abertura_conta, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoErro);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAberturaConta()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_abertura_conta, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoImpossibilidade);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

}