package prototipoappbancariomelhorado.main.controller

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.DecimalFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Movimentacao
import prototipoappbancariomelhorado.main.model.MovimentacaoDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO
import java.time.format.DateTimeFormatter

class ControladoraMovimentacoes : AppCompatActivity() {

     lateinit var botaoVoltar : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_movimentacoes)

        botaoVoltar = findViewById(R.id.botaoVoltarMinhasMovimentações)

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        var movimentacaoDAO = MovimentacaoDAO(this)

        var listaDeMovimentacoes = movimentacaoDAO.pegarMovimentacoesConta(conta)

        val recyclerViewMovimentacoes = findViewById<RecyclerView>(R.id.reciclerMovimentacoes)

        recyclerViewMovimentacoes.layoutManager = LinearLayoutManager(this)

        val adapter = MovimentacoesAdapter(listaDeMovimentacoes)

        recyclerViewMovimentacoes.adapter = adapter

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

}