package prototipoappbancariomelhorado.main.controller

import android.content.Intent
import android.icu.text.DecimalFormat
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.databinding.ActivityUsuarioLogadoBinding
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO


class ControladoraUsuarioLogado : AppCompatActivity(), OnOperacaoClickListener {

    private lateinit var binding: ActivityUsuarioLogadoBinding
    private lateinit var adaptadorDeOperacoesBancarias: AdaptadorOperacoesBancarias
    private val listaDeOperacoesBancarias: MutableList<OperacoesBancarias> = mutableListOf()
    private  var conta: Conta? = null
    var usuarioDAO = UsuarioDAO(this)
    var contaDAO = ContaDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuarioLogadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        setupRecyclerView()
        listaIconesOperacoesBancarias()
    }

    override fun onResume() {
        super.onResume()
        conta = sincronizaContaComBancoDeDados(usuarioDAO, contaDAO, conta!!.getUsuarioAtributo())

        insereNoTextViewNomeUsuarioLogado(conta!!)
        insereNoTextViewSaldoUsuarioLogado(conta!!)
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

                var intent = Intent(this, ControladoraDeposito::class.java);
                intent.putExtra("conta", conta)
                startActivity(intent);
            }

            R.drawable.sacar -> {

                var intent = Intent(this, ControladoraSaque::class.java);
                intent.putExtra("conta", conta)
                startActivity(intent);

            }

            R.drawable.transferencia -> {

                var intent = Intent(this, ControladoraTransferencia::class.java);
                intent.putExtra("conta", conta)
                startActivity(intent);
            }

            R.drawable.movimentacoes -> {

                var intent = Intent(this, ControladoraMovimentacoes::class.java);
                intent.putExtra("conta", conta)
                startActivity(intent);

            }

            R.drawable.transferencias -> {


            }

            R.drawable.meus_dados -> {

                var intent = Intent(this, ControladoraPerfil::class.java);
                intent.putExtra("conta", conta)
                startActivity(intent);

            }

             R.drawable.atualizar_meus_dados -> {

                 var intent = Intent(this, ControladoraAtualizacaoUsuario::class.java);
                 intent.putExtra("conta", conta)
                 startActivity(intent);

            }

             R.drawable.sair -> {
                 fazerLogout()
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

        val decimalFormat = DecimalFormat("#.##")
        val saldoFormatado  = decimalFormat.format(conta.getSaldoContaAtributo())

        val saldoUsuarioLogado = "Saldo: R$ $saldoFormatado"
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

    fun sincronizaContaComBancoDeDados (usuarioDAO: UsuarioDAO, contaDAO: ContaDAO, usuario: Usuario) : Conta?
    {
       var usuarioAtualizado = usuarioDAO.pegarDadosUsuarioPorID(usuario)

        var conta = usuarioAtualizado?.let { contaDAO.pegarContaUsuario(it) }

        return conta
    }

    fun fazerLogout ()
    {
        conta = null
        this.finish()
    }
}