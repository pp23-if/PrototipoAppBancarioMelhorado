package prototipoappbancariomelhorado.main.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
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
import java.time.format.DateTimeFormatter

class ControladoraPerfil : AppCompatActivity() {

    lateinit var botaoVoltar : TextView;
    lateinit var infoNomePerfil : TextView;
    lateinit var infoDataNascimentoPerfil : TextView;
    lateinit var infoLoginPerfil : TextView;
    lateinit var infoSenhaPerfil : TextView;
    lateinit var infoNumeroContaPerfil : TextView;
    lateinit var infoSaldoContaPerfil : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        inicializaElementosInterfaceGrafica()

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        insereDadosContaNaActivity(conta)
    }


    fun inicializaElementosInterfaceGrafica()
    {
        botaoVoltar = findViewById(R.id.botaoVoltarPerfil)
        infoNomePerfil = findViewById(R.id.textViewNomeUsuarioPerfil)
        infoDataNascimentoPerfil = findViewById(R.id.textViewDataNascimentoUsuarioPerfil)
        infoLoginPerfil = findViewById(R.id.textViewValorLoginUsuario)
        infoSenhaPerfil = findViewById(R.id.textViewValorSenhaUsuario)
        infoNumeroContaPerfil = findViewById(R.id.textViewValorNumeroContaUsuario)
        infoSaldoContaPerfil = findViewById(R.id.textViewValorSaldoContaUsuario)

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

    fun insereDadosContaNaActivity (conta: Conta)
    {

        val decimalFormat = DecimalFormat("#.##")
        val saldoFormatado  = decimalFormat.format(conta.getSaldoContaAtributo())

        val formatadorDia = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        infoNomePerfil.text = conta.getUsuarioAtributo().getNomeUsuarioAtributo()
        infoDataNascimentoPerfil.text =
            conta.getUsuarioAtributo().getDataDeNascimentoUsuarioAtributo()!!.format(formatadorDia).toString()
        infoLoginPerfil.text = conta.getUsuarioAtributo().getLoginUsuarioAtributo()
        infoSenhaPerfil.text = conta.getUsuarioAtributo().getSenhaUsuarioAtributo()
        infoNumeroContaPerfil.text = conta.getIdContaAtributo().toString()
        infoSaldoContaPerfil.text = saldoFormatado
    }
}