package prototipoappbancariomelhorado.main.controller

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.databinding.ActivityUsuarioLogadoBinding
import prototipoappbancariomelhorado.main.model.Conta


class ControladoraUsuarioLogado : AppCompatActivity(), OnOperacaoClickListener {

    private lateinit var binding: ActivityUsuarioLogadoBinding
    private lateinit var adaptadorDeOperacoesBancarias: AdaptadorOperacoesBancarias
    private val listaDeOperacoesBancarias: MutableList<OperacoesBancarias> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioLogadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        insereNoTextViewNomeUsuarioLogado(conta)
        insereNoTextViewSaldoUsuarioLogado(conta)

        setupRecyclerView()
        listaIconesOperacoesBancarias()
    }

    private fun setupRecyclerView() {
        val recyclerIcones = binding.reciclerIconesBancarios
        recyclerIcones.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerIcones.setHasFixedSize(true)
        adaptadorDeOperacoesBancarias = AdaptadorOperacoesBancarias(this, listaDeOperacoesBancarias, this)
        recyclerIcones.adapter = adaptadorDeOperacoesBancarias
    }

    override fun onOperacaoClick(operacao: OperacoesBancarias) {

        when (operacao.icone) {

            R.drawable.depositar -> {

            }

            R.drawable.sacar -> {

            }

            R.drawable.transferencia -> {

            }

           R.drawable.movimentacoes -> {


            }

            R.drawable.transferencias -> {


            }

            R.drawable.meus_dados -> {


            }

             R.drawable.atualizar_meus_dados -> {


            }

             R.drawable.sair -> {


            }
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

    private fun insereNoTextViewNomeUsuarioLogado(conta: Conta) {
        val nomeUsuarioLogado = "Olá, " + conta.getUsuarioAtributo().getNomeUsuarioAtributo()
        binding.txtNomeUsuarioLogado.text = nomeUsuarioLogado
    }

    private fun insereNoTextViewSaldoUsuarioLogado(conta: Conta) {
        val saldoUsuarioLogado = conta.getSaldoContaAtributo().toString()
        binding.txtSaldoUsuarioLogado.text = saldoUsuarioLogado
    }

    private fun listaIconesOperacoesBancarias() {
        listaDeOperacoesBancarias.apply {
            add(OperacoesBancarias(R.drawable.depositar, "Depositar"))
            add(OperacoesBancarias(R.drawable.sacar, "Sacar"))
            add(OperacoesBancarias(R.drawable.transferencia, "Transferir"))
            add(OperacoesBancarias(R.drawable.movimentacoes, "Movimentações"))
            add(OperacoesBancarias(R.drawable.transferencias, "Transferências"))
            add(OperacoesBancarias(R.drawable.meus_dados, "Meus dados"))
            add(OperacoesBancarias(R.drawable.atualizar_meus_dados, "Atualizar meus dados"))
            add(OperacoesBancarias(R.drawable.sair, "Sair"))
        }
    }
}