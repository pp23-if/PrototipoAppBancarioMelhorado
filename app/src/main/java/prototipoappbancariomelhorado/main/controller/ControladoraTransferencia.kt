package prototipoappbancariomelhorado.main.controller

import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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
import prototipoappbancariomelhorado.main.model.SelecaoConta
import prototipoappbancariomelhorado.main.model.Transferencia
import prototipoappbancariomelhorado.main.model.TransferenciaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO
import java.time.LocalDateTime
import java.util.LinkedList

class ControladoraTransferencia : AppCompatActivity() {

    lateinit var campoValorTransferencia : TextInputEditText;
    lateinit var botaoTransferir : Button;
    lateinit var botaoVoltar : TextView;
    lateinit var menuSelecao : AutoCompleteTextView;
    lateinit var dialog : AlertDialog;
    var contaDAO = ContaDAO(this)
    var usuarioDAO = UsuarioDAO(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transferencia)

        inicializaElementosInterfaceGrafica()

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta
        var listaDeUsuarios = usuarioDAO.pegarDadosUsuarios()

        var listaDeContas = contaDAO.pegarContasExcetoUsuarioLogado(listaDeUsuarios)

        var itemSelecionado : SelecaoConta? = null

        Log.i("Erro", "A LISTA E: $listaDeContas")

        var indiceSelecaoConta = buscaContaDaSelecao(conta, listaDeContas)

        listaDeContas.removeAt(indiceSelecaoConta)

        val adapter = ArrayAdapter(this, R.layout.list_item, listaDeContas)

        menuSelecao.setAdapter(adapter)

        menuSelecao.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            itemSelecionado = listaDeContas[position]

            Log.i("Erro", "A CONTA E: $itemSelecionado")

        }


        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        botaoTransferir.setOnClickListener()
        {
            if(verificaEntradasVazias())
            {
                Toast.makeText(this, "Por favor, selecione e preencha  todos os campos.", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(trataValorTransferencia())
                {

                    var valorTransferencia = campoValorTransferencia.text.toString().toDouble()

                    var contaDAO = ContaDAO(this)
                    var transferenciaDAO = TransferenciaDAO(this)

                    var saldoEmConta = contaDAO.pegaSaldoConta(conta)

                    if(verificaSaldoInsuficienteTransferencia(valorTransferencia, saldoEmConta))
                    {
                        var transferenciaFeita = Transferencia(0, conta, itemSelecionado!!.getContaAtributo(),
                            "feito", valorTransferencia)

                        var transferenciaRecebida = Transferencia(0, itemSelecionado!!.getContaAtributo(), conta,
                            "recebido", valorTransferencia)

                        var saldoAtualizadoContaOrigem = calculaSaldoAtualizadoContaOrigem(valorTransferencia, contaDAO, conta)

                        var saldoAtualizadoContaDestino = calculaSaldoAtualizadoContaDestino(valorTransferencia, contaDAO, itemSelecionado!!.getContaAtributo())

                        if(transferenciaDAO.realizarTransferencia(transferenciaFeita, transferenciaRecebida, saldoAtualizadoContaOrigem, saldoAtualizadoContaDestino))
                        {
                            caixaDeDialogoSucessoTransferencia()
                            limparCampos()
                        }
                        else
                        {
                            caixaDeDialogoImpossibilidadeTransferencia()
                        }
                    }
                    else
                    {
                        caixaDeDialogoSaldoInsuficiente()
                    }

                }
                else
                {
                    caixaDeDialogoValorTransferenciaIncorreto()
                }
            }
        }

    }

    fun inicializaElementosInterfaceGrafica()
    {
        campoValorTransferencia = findViewById(R.id.textFieldValorTransferencia)
        botaoTransferir = findViewById(R.id.botaoFazerTransferencia)
        botaoVoltar = findViewById(R.id.botaoVoltarTransferencia);
        menuSelecao = findViewById(R.id.autoComplete)

    }

    fun verificaEntradasVazias () : Boolean
    {
        return (campoValorTransferencia.text.isNullOrBlank() || campoValorTransferencia.text.isNullOrEmpty()
                || menuSelecao.text.isNullOrBlank() ||  menuSelecao.text.isNullOrEmpty())
    }

    fun limparCampos ()
    {
        campoValorTransferencia.text?.clear()
        menuSelecao.text?.clear()
    }

    fun trataValorTransferencia () : Boolean
    {
        return try {
            campoValorTransferencia.text.toString().toDouble()
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

    fun buscaContaDaSelecao (conta: Conta, listaDeSelecaoConta: LinkedList<SelecaoConta>) : Int
    {

         listaDeSelecaoConta.forEachIndexed { index, selecaoConta ->

             if(selecaoConta.getContaAtributo().getIdContaAtributo() == conta.getIdContaAtributo())
             {
                 return index
             }

         }

        return -1
    }

    fun calculaSaldoAtualizadoContaOrigem (valorTransferencia: Double, contaDAO: ContaDAO, conta: Conta) : Double
    {
        val saldoConta = contaDAO.pegaSaldoConta(conta)

        val saldoAtualizado = (saldoConta - valorTransferencia)

        return saldoAtualizado
    }

    fun calculaSaldoAtualizadoContaDestino (valorTransferencia: Double, contaDAO: ContaDAO, conta: Conta) : Double
    {
        val saldoConta = contaDAO.pegaSaldoConta(conta)

        val saldoAtualizado = (saldoConta + valorTransferencia)

        return saldoAtualizado
    }

    fun verificaSaldoInsuficienteTransferencia (valorTransferencia: Double, saldoEmConta : Double) : Boolean
    {
        return valorTransferencia > 0 && valorTransferencia <= saldoEmConta
    }

    fun caixaDeDialogoValorTransferenciaIncorreto()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_valor_incorreto, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoValorIncorreto);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoSucessoTransferencia()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_sucesso_transferencia, null)

        build.setView(view);

        var botaoOk = view.findViewById<Button>(R.id.botaoOkTransferencia);
        botaoOk.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoImpossibilidadeTransferencia()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_impossibilidade_transferencia, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoImpossibilidadeTransferencia);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }

    fun caixaDeDialogoSaldoInsuficiente()
    {
        val build = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.activity_caixa_dialogo_saldo_insuficiente, null)

        build.setView(view);

        var botaoErro = view.findViewById<Button>(R.id.botaoSaldoInsuficiente);
        botaoErro.setOnClickListener{dialog.dismiss()}
        dialog = build.create()
        dialog.show()

    }
}