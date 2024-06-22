package prototipoappbancariomelhorado.main.controller

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.databinding.ActivityUsuarioLogadoBinding
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Movimentacao
import prototipoappbancariomelhorado.main.model.MovimentacaoDAO
import java.time.LocalDateTime

class ControladoraSaque : AppCompatActivity() {

    lateinit var campoValorSaque : TextInputEditText;
    lateinit var botaoSacar : Button;
    lateinit var botaoVoltar : TextView;
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saque)

        inicializaElementosInterfaceGrafica()

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        //Log.i("Erro", "A CONTA E: $conta")

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoSacar.setOnClickListener()
        {
            if(verificaEntradasVazias())
            {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(trataValorSaque())
                {
                    var valorSaque = campoValorSaque.text.toString().toDouble()

                    var movimentacao = Movimentacao(0, conta, "debito", valorSaque, LocalDateTime.now())
                    var contaDAO = ContaDAO(this)
                    var movimentacaoDAO = MovimentacaoDAO(this)

                    if(movimentacaoDAO.realizarMovimentacao(movimentacao, calculaSaldoAtualizadoContaSaque(valorSaque, contaDAO, conta)))
                    {
                        caixaDeDialogoSucessoMovimentacaoSaque()
                        limparCampos()
                    }
                    else
                    {
                        caixaDeDialogoImpossibilidadeSaque()
                    }

                }
                else
                {
                    caixaDeDialogoValorSaqueIncorreto()
                }
            }
        }

    }

    fun inicializaElementosInterfaceGrafica()
    {
        campoValorSaque = findViewById(R.id.textFieldValorSaque)
        botaoSacar = findViewById(R.id.botaoFazerSaque)
        botaoVoltar = findViewById(R.id.botaoVoltarSaque);

    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoValorSaque.text.isNullOrBlank() || campoValorSaque.text.isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoValorSaque.text?.clear()
    }

    fun trataValorSaque () : Boolean
    {
        return try {
            campoValorSaque.text.toString().toDouble()
            true
        } catch (e: Exception) {
            false
        }
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

    fun calculaSaldoAtualizadoContaSaque (valorSaque: Double, contaDAO: ContaDAO, conta: Conta) : Double
    {
        val saldoConta = contaDAO.pegaSaldoConta(conta)

        val saldoAtualizado = (saldoConta - valorSaque)

        return saldoAtualizado
    }

    fun caixaDeDialogoValorSaqueIncorreto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_valor_incorreto, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoValorIncorreto);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoSucessoMovimentacaoSaque()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_saque, null)

        build.setView(view);

        var botaoOk = view.findViewById<Button>(R.id.botaoOkSaque);
        botaoOk.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeSaque()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_impossibilidade_saque, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoImpossibilidadeSaque);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }
}