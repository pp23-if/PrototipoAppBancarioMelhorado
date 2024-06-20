package prototipoappbancariomelhorado.main.controller

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import prototipoappbancariomelhorado.main.databinding.ItensBancariosBinding


class AdaptadorOperacoesBancarias(
    private val context: Context,
    private val listaDeIcones: MutableList<OperacoesBancarias>,
    private val listener: OnOperacaoClickListener
) : RecyclerView.Adapter<AdaptadorOperacoesBancarias.OperacoesBancariasHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperacoesBancariasHolder {
        val itemLista = ItensBancariosBinding.inflate(LayoutInflater.from(context), parent, false)
        return OperacoesBancariasHolder(itemLista)
    }

    override fun getItemCount() = listaDeIcones.size

    override fun onBindViewHolder(holder: OperacoesBancariasHolder, position: Int) {
        val operacao = listaDeIcones[position]
        holder.bind(operacao)
    }

    inner class OperacoesBancariasHolder(private val binding: ItensBancariosBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(operacao: OperacoesBancarias) {
            binding.icBancarioDepositar.setBackgroundResource(operacao.icone!!)
            binding.txtIconeBancarioDepositar.text = operacao.titulo

            // Configura o listener de clique no item
            binding.root.setOnClickListener {
                listener.onOperacaoClick(operacao)
            }
        }

    }

}