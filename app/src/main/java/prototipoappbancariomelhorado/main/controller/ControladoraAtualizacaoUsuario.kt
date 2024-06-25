package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.BancoDeDados
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ControladoraAtualizacaoUsuario : AppCompatActivity() {

    lateinit var botaoAtualizarDados: Button;
    lateinit var botaoVoltar: TextView;
    lateinit var campoNome: TextInputEditText;
    lateinit var campoDataNascimento: TextInputEditText;
    lateinit var campoLogin: TextInputEditText;
    lateinit var campoSenha: TextInputEditText;
    lateinit var dialog: AlertDialog;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atuaizacao_dados_usuario)

        inicializaElementosInterfaceGrafica()

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        insereDadosUsuarioNosInputs(conta)

        var usuarioDAO = UsuarioDAO(this)

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }


        botaoAtualizarDados.setOnClickListener()
        {
            if (verificaEntradasVazias(
                    campoNome.text.toString(), campoDataNascimento.text.toString(),
                    campoLogin.text.toString(), campoSenha.text.toString()
                )
            ) {
                 criarToastCustomizadoEntradaVazia()
            } else {
                //verificar se o login ja se encontra cadastrado.

                if (verificaLoginInformado(campoLogin.text.toString(), usuarioDAO, conta)) {
                    caixaDeDialogoUsuarioEmUso()
                } else {

                    if (trataDataNascimento()) {
                        var usuarioAtualizado = montarUsuarioAtualizado(conta.getUsuarioAtributo())

                        if (usuarioDAO.atualizarDadosUsuario(
                                conta.getUsuarioAtributo(),
                                usuarioAtualizado
                            )
                        ) {
                            caixaDeDialogoSucessoAtualizacaoDados()
                        } else {
                            caixaDeDialogoImpossibilidadeAtualizacaoDados()
                        }
                    } else {
                        caixaDeDialogoErroData()
                    }

                }

            }

        }

    }

    fun inicializaElementosInterfaceGrafica() {
        campoNome = findViewById(R.id.textFieldValorNomeUsuario)
        campoDataNascimento = findViewById(R.id.textFieldValorDataNascimentoUsuario)
        campoLogin = findViewById(R.id.textFieldValorLoginUsuario)
        campoSenha = findViewById(R.id.textFieldValorSenhaUsuario)
        botaoVoltar = findViewById(R.id.botaoVoltarAtualizacaoCliente)
        botaoAtualizarDados = findViewById(R.id.botaoAtualizarCliente)


    }

    fun verificaEntradasVazias(
        campoNome: String, campoDataNascimento: String,
        campoLogin: String, campoSenha: String
    ): Boolean {
        return (campoNome.isNullOrBlank() || campoNome.isNullOrEmpty()
                || campoDataNascimento.isNullOrBlank() || campoDataNascimento.isNullOrEmpty()
                || campoLogin.isNullOrBlank() || campoLogin.isNullOrEmpty()
                || campoSenha.isNullOrBlank() || campoSenha.isNullOrEmpty())
    }

    fun insereDadosUsuarioNosInputs(conta: Conta) {
        val formatadorDia = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        var dataNascimento =
            conta.getUsuarioAtributo().getDataDeNascimentoUsuarioAtributo()!!.format(formatadorDia)

        campoNome.setText(conta.getUsuarioAtributo().getNomeUsuarioAtributo())
        campoDataNascimento.setText(dataNascimento.toString())
        campoLogin.setText(conta.getUsuarioAtributo().getLoginUsuarioAtributo())
        campoSenha.setText(conta.getUsuarioAtributo().getSenhaUsuarioAtributo())
    }

    private fun pegaContaDaActivityAnterior(bundle: Bundle?): Conta? {
        return bundle?.run {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                getParcelable("conta", Conta::class.java)
            } else {
                getParcelable("conta")
            }
        }
    }

    fun montarUsuarioAtualizado(usuario: Usuario): Usuario {
        var dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var dataNascimento = LocalDate.parse(campoDataNascimento.text.toString(), dataFormatada);

        var usuarioAtualizado = Usuario(
            usuario.getIdUsuarioAtributo(), campoNome.text.toString(),
            dataNascimento, campoLogin.text.toString(), campoSenha.text.toString()
        )

        return usuarioAtualizado
    }


    fun verificaLoginInformado(login: String, usuarioDAO: UsuarioDAO, conta: Conta): Boolean {
        var loginRecebido = login.trim();

        var loginEncontrado =
            usuarioDAO.verificaExistenciaLoginInformadoAtualizacao(login, conta).trim()


        return loginEncontrado.lowercase() == loginRecebido || loginEncontrado.uppercase() == loginRecebido
    }

    fun trataDataNascimento(): Boolean {
        return try {
            var dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(campoDataNascimento.text.toString(), dataFormatada);
            true
        } catch (e: Exception) {
            false
        }
    }

    fun caixaDeDialogoSucessoAtualizacaoDados() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(
            R.layout.activity_caixa_dialogo_sucesso_atualizacao_dados_usuario,
            null
        )

        build.setView(view);

        var botaoOk = view.findViewById<Button>(R.id.botaoOkAtualizacaoUsuario);
        botaoOk.setOnClickListener {
            dialog.dismiss()
            this.finish();
        }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoErroData() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(
            R.layout.activity_caixa_dialogo_erro_data_atualizacao_dados,
            null
        )

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoErroDataAtualizacaoDados);
        botaoErro.setOnClickListener { dialog.dismiss() }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeAtualizacaoDados() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(
            R.layout.activity_caixa_dialogo_impossibilidade_atualizacao_dados_usuario,
            null
        )

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoImpossibilidadeAtualizacaoUsuario);
        botaoErro.setOnClickListener { dialog.dismiss() }
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoUsuarioEmUso() {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_login_em_uso, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoUsuarioExistente);
        botaoErro.setOnClickListener { dialog.dismiss() }
        dialog = build.create()
        dialog.show()

    }

    fun criarToastCustomizadoEntradaVazia ()
    {
        val view = layoutInflater.inflate(R.layout.activity_custom_toast_entradas_vazias,null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

}