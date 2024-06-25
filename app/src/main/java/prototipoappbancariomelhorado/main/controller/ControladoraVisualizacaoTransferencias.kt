package prototipoappbancariomelhorado.main.controller

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.MovimentacaoDAO
import prototipoappbancariomelhorado.main.model.Transferencia
import prototipoappbancariomelhorado.main.model.TransferenciaDAO
import prototipoappbancariomelhorado.main.model.UsuarioDAO

class ControladoraVisualizacaoTransferencias : AppCompatActivity() {

    lateinit var botaoVoltar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transferencias)

        botaoVoltar = findViewById(R.id.botaoVoltarMinhasTransferencias)

        botaoVoltar.setOnClickListener()
        {
            this.finish()
        }

        var conta = pegaContaDaActivityAnterior(intent.extras) as Conta

        var usuarioDAO = UsuarioDAO(this)

        var contaDAO = ContaDAO(this)

        var transferenciaDAO = TransferenciaDAO(this)

        var listaDeTransferencia =
            transferenciaDAO.pegarTransferenciasConta(conta, contaDAO, usuarioDAO)

        if (listaDeTransferencia.isNotEmpty()) {
            val recyclerViewTransferencia = findViewById<RecyclerView>(R.id.reciclerTransferencias)

            recyclerViewTransferencia.layoutManager = LinearLayoutManager(this)

            val adapter = TransferenciaAdapter(listaDeTransferencia)

            recyclerViewTransferencia.adapter = adapter
        } else {
            criarToastCustomizadoListaTransferenciVazia()
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

    fun criarToastCustomizadoListaTransferenciVazia() {
        val view =
            layoutInflater.inflate(R.layout.activity_custom_toast_transferencias_vazias, null)

        val toast = Toast(this)
        toast.view = view
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

}