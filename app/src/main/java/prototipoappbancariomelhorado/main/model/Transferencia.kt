package prototipoappbancariomelhorado.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Parcelize
class Transferencia (private var idTransferencia : Int, private var contaEntrada: Conta,
                     private var contaSaida: Conta, private var tipoTransferencia: String,
                     private var valorTransferencia: Double,
                     private var dataTransferencia:  LocalDateTime? = LocalDateTime.now()) : Parcelable
{

    fun setIdTransferenciaAtributo(idTransferencia: Int) {
        this.idTransferencia = idTransferencia
    }

    fun getIdTransferenciaAtributo(): Int {
        return this.idTransferencia
    }

    fun setContaEntradaAtributo(contaEntrada: Conta) {
        this.contaEntrada = contaEntrada
    }

    fun getContaEntradaAtributo(): Conta {
        return this.contaEntrada
    }

    fun setContaSaidaAtributo(contaSaida: Conta) {
        this.contaSaida = contaSaida
    }

    fun getContaSaidaAtributo(): Conta {
        return this.contaSaida
    }

    fun setTipoTransferenciaAtributo(tipoTransferencia: String) {
        this.tipoTransferencia = tipoTransferencia
    }

    fun getTipoTransferenciaAtributo(): String {
        return this.tipoTransferencia
    }

    fun setValorTransferenciaAtributo(valorTransferencia: Double) {
        this.valorTransferencia = valorTransferencia
    }

    fun getValorTransferenciaAtributo(): Double {
        return this.valorTransferencia
    }

    fun setDataTransferenciaAtributo(dataTransferencia: LocalDateTime) {
        this.dataTransferencia = dataTransferencia
    }

    fun getDataTransferenciaoAtributo(): LocalDateTime? {
        return this.dataTransferencia
    }

    override fun toString(): String {

        val formatadorDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return  "ID Transferência: " + this.idTransferencia + "\n" +
                "Conta De Destino: " + this.contaEntrada.getIdContaAtributo() + "\n" +
                "Titular: " + this.contaEntrada.getUsuarioAtributo().getNomeUsuarioAtributo() + "\n" +
                "Conta De Origem: " + this.contaSaida.getIdContaAtributo() +"\n" +
                "Titular: " + this.contaSaida.getUsuarioAtributo().getNomeUsuarioAtributo() + "\n" +
                "Tipo De Transferência: " + this.tipoTransferencia +"\n" +
                "Valor Transferência: " + this.valorTransferencia +"\n" +
                "Data Movimentação: " + (this.dataTransferencia!!.format(formatadorDataHora))
    }
}