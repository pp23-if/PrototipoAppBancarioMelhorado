package prototipoappbancariomelhorado.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
class Movimentacao (private var idMovimentacao: Int, private var conta: Conta,
                    private var tipoMovimentacao: String, private var valorMovimentacao: Double,
                    private var dataMovimentacao: LocalDateTime? = LocalDateTime.now()) : Parcelable
{
    fun setIdMovimentacaoAtributo(idMovimentacao: Int) {
        this.idMovimentacao = idMovimentacao
    }

    fun getIdMovimentacaoAtributo(): Int {
        return this.idMovimentacao
    }

    fun setContaAtributo(conta: Conta) {
        this.conta = conta
    }

    fun getContaAtributo(): Conta {
        return this.conta
    }

    fun setTipoMovimentacaoAtributo(tipoMovimentacao: String) {
        this.tipoMovimentacao = tipoMovimentacao
    }

    fun getTipoMovimentacaoAtributo(): String {
        return this.tipoMovimentacao
    }

    fun setValorMovimentacaoAtributo(valorMovimentacao: Double) {
        this.valorMovimentacao = valorMovimentacao
    }

    fun getValorMovimentacaoAtributo(): Double {
        return this.valorMovimentacao
    }

    fun setDataMovimentacaoAtributo(dataMovimentacao: LocalDateTime) {
        this.dataMovimentacao = dataMovimentacao
    }

    fun getDataMovimentacaoAtributo(): LocalDateTime? {
        return this.dataMovimentacao
    }

    override fun toString(): String {

        val formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return   "Conta: " + this.conta.getIdContaAtributo() + "\n" +
                 "Usuário: " + this.conta.getUsuarioAtributo().getNomeUsuarioAtributo() + "\n" +
                 "ID Movimentação: " + this.idMovimentacao + "\n" +
                 "Tipo De Movimentação: " + this.tipoMovimentacao +"\n" +
                 "Valor Movimentacao: " + this.valorMovimentacao +"\n" +
                 "Data Movimentação: " + (this.dataMovimentacao!!.format(formatadorDataHora))
    }
}