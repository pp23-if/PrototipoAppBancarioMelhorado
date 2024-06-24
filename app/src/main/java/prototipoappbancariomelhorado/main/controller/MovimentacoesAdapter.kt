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
import java.time.format.DateTimeFormatter
import java.util.*

class MovimentacoesAdapter(private val movimentacoes: List<Movimentacao>) : RecyclerView.Adapter<MovimentacoesAdapter.MovimentacaoViewHolder>() {

    class MovimentacaoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeMovimentacaoTextView: TextView = view.findViewById(R.id.textViewNomeMovimentacao)
        val valorMovimentacaoTextView: TextView = view.findViewById(R.id.textViewValorMovimentacao)
        val dataMovimentacaoTextView: TextView = view.findViewById(R.id.textViewValorDataMovimentacao)
        val tipoMovimentacaoTextView: TextView = view.findViewById(R.id.textViewValorTipoMovimentacao)
        val referenciaMovimentacaoImageView: ImageView = view.findViewById(R.id.iconReferencia)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovimentacaoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_movimentacoes, parent, false) // Ajuste o nome do layout para algo mais específico
        return MovimentacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovimentacaoViewHolder, position: Int) {
        val movimentacao = movimentacoes[position]

        val context = holder.itemView.context
        val isCredito = movimentacao.getTipoMovimentacaoAtributo() == "credito"
        val drawable: Drawable? = if (isCredito) {
            holder.nomeMovimentacaoTextView.text = "Depósito"
            holder.tipoMovimentacaoTextView.text = "Crédito"
            ContextCompat.getDrawable(context, R.drawable.positivo)
        } else {
            holder.nomeMovimentacaoTextView.text = "Saque"
            holder.tipoMovimentacaoTextView.text = "Débito"
            ContextCompat.getDrawable(context, R.drawable.negativo)
        }

        holder.referenciaMovimentacaoImageView.setImageDrawable(drawable)

        // Usando o Locale padrão para o formato decimal
        val decimalFormat = DecimalFormat("#.##", DecimalFormatSymbols(Locale.getDefault()))
        val saldoFormatado = decimalFormat.format(movimentacao.getValorMovimentacaoAtributo())
        holder.valorMovimentacaoTextView.text = saldoFormatado

        val formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        val dataFormatada = movimentacao.getDataMovimentacaoAtributo()?.format(formatadorDataHora) ?: "Data indisponível"
        holder.dataMovimentacaoTextView.text = dataFormatada
    }

    override fun getItemCount() = movimentacoes.size
}
