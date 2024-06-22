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

class ControladoraDeposito : AppCompatActivity() {

    lateinit var campoValorDeposito : TextInputEditText;
    lateinit var botaoDepositar : Button;
    lateinit var botaoVoltar : TextView;
    lateinit var dialog : AlertDialog;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposito)

        inicializaElementosInterfaceGrafica()

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        Log.i("Erro", "A CONTA E: $conta")

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoDepositar.setOnClickListener()
        {
            if(verificaEntradasVazias())
            {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
            else
            {
               if(trataValorDeposito())
               {
                 var valorDeposito = campoValorDeposito.text.toString().toDouble()

                   var movimentacao = Movimentacao(0, conta, "credito", valorDeposito, LocalDateTime.now())
                   var contaDAO = ContaDAO(this)
                   var movimentacaoDAO = MovimentacaoDAO(this)

                 if(movimentacaoDAO.realizarMovimentacao(movimentacao, calculaSaldoAtualizadoContaDeposito(valorDeposito, contaDAO, conta)))
                 {
                     caixaDeDialogoSucessoMovimentacaoDeposito()
                 }
                 else
                 {
                    caixaDeDialogoImpossibilidadeDeposito()
                     limparCampos()
                 }

               }
               else
               {
                 caixaDeDialogoValorDepositoIncorreto()
               }
            }
        }

    }

    fun inicializaElementosInterfaceGrafica()
    {
        campoValorDeposito = findViewById(R.id.textFieldValorDeposito)
        botaoDepositar = findViewById(R.id.botaoFazerDeposito)
        botaoVoltar = findViewById(R.id.botaoVoltarDeposito);

    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoValorDeposito.text.isNullOrBlank() || campoValorDeposito.text.isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoValorDeposito.text?.clear()
    }

    fun trataValorDeposito () : Boolean
    {
        return try {
            campoValorDeposito.text.toString().toDouble()
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

    fun calculaSaldoAtualizadoContaDeposito (valorDeposito: Double, contaDAO: ContaDAO, conta: Conta) : Double
    {
        val saldoAtualizado =+ valorDeposito + contaDAO.pegaSaldoConta(conta)

        return saldoAtualizado
    }

    fun caixaDeDialogoValorDepositoIncorreto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_valor_incorreto, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoValorIncorreto);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoSucessoMovimentacaoDeposito()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_deposito, null)

        build.setView(view);

        var botaoOk = view.findViewById<Button>(R.id.botaoOkDeposito);
        botaoOk.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeDeposito()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_impossibilidade_deposito, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoImpossibilidadeDeposito);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }
}