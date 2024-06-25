package prototipoappbancariomelhorado.main.controller

import android.graphics.drawable.Drawable
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Movimentacao
import prototipoappbancariomelhorado.main.model.Transferencia
import java.time.format.DateTimeFormatter
import java.util.*

class TransferenciaAdapter (private val transferencias : List<Transferencia>) : RecyclerView.Adapter<TransferenciaAdapter.TransferenciaViewHolder>() {

    class TransferenciaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeTransferenciaTextView: TextView = view.findViewById(R.id.textViewNomeTransferencia)
        val valorTransferenciaTextView: TextView = view.findViewById(R.id.textViewValorTransferencia)
        val dataTransferenciaTextView: TextView = view.findViewById(R.id.textViewValorDataTransferencia)
        val selecaoFeitoRecebidoTextView: TextView = view.findViewById(R.id.textViewRecebidoFeitoTransferencia)
        val valorselecaoFeitoRecebidoTextView: TextView = view.findViewById(R.id.textViewValorRecebidoFeitoTransferencia)
        val contaTransferenciaTextView: TextView = view.findViewById(R.id.textViewValorContaTransferencia)
        val tipoTransferenciaTextView: TextView = view.findViewById(R.id.textViewValorTipoTransferencia)
        val referenciaTransferenciaImageView: ImageView = view.findViewById(R.id.iconTransferencia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransferenciaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_transferencia, parent, false) // Ajuste o nome do layout para algo mais específico
        return TransferenciaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransferenciaViewHolder, position: Int) {
        val transferencia = transferencias[position]

        val context = holder.itemView.context
        val isFeito = transferencia.getTipoTransferenciaAtributo() == "feito"
        val drawable: Drawable? = if (isFeito) {
            holder.nomeTransferenciaTextView.text = "Feita"
            holder.selecaoFeitoRecebidoTextView.text = "Trasnf. P/:"
            holder.valorselecaoFeitoRecebidoTextView.text = transferencia.getContaSaidaAtributo().getUsuarioAtributo().getNomeUsuarioAtributo()
            holder.contaTransferenciaTextView.text = transferencia.getContaSaidaAtributo().getIdContaAtributo().toString()
            holder.tipoTransferenciaTextView.text = "Débito"
            ContextCompat.getDrawable(context, R.drawable.negativo)
        } else {
            holder.nomeTransferenciaTextView.text = "Recebida"
            holder.selecaoFeitoRecebidoTextView.text = "Recebida de:"
            holder.valorselecaoFeitoRecebidoTextView.text = transferencia.getContaSaidaAtributo().getUsuarioAtributo().getNomeUsuarioAtributo()
            holder.contaTransferenciaTextView.text = transferencia.getContaSaidaAtributo().getIdContaAtributo().toString()
            holder.tipoTransferenciaTextView.text = "Crédito"
            ContextCompat.getDrawable(context, R.drawable.positivo)
        }

        holder.referenciaTransferenciaImageView.setImageDrawable(drawable)

        // Usando o Locale padrão para o formato decimal
        val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.getDefault()))
        val saldoFormatado = decimalFormat.format(transferencia.getValorTransferenciaAtributo())
        holder.valorTransferenciaTextView.text = saldoFormatado

        val formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dataFormatada = transferencia.getDataTransferenciaoAtributo()?.format(formatadorDataHora) ?: "Data indisponível"
        holder.dataTransferenciaTextView.text = dataFormatada
    }

    override fun getItemCount() = transferencias.size
}
