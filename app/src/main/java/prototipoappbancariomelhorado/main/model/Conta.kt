package prototipoappbancariomelhorado.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.format.DateTimeFormatter

@Parcelize
class Conta (private var idConta : Int, private var usuario: Usuario, private var saldoConta : Double) : Parcelable
{
    fun setIdContaAtributo(idConta: Int) {
        this.idConta = idConta
    }

    fun getIdContaAtributo(): Int {
        return this.idConta
    }

    fun setUsuarioAtributo(usuario: Usuario) {
        this.usuario = usuario
    }

    fun getUsuarioAtributo(): Usuario {
        return this.usuario
    }

    fun setSaldoContaAtributo(saldoConta: Double) {
        this.saldoConta = saldoConta
    }

    fun getSaldoContaAtributo(): Double {
        return this.saldoConta
    }

    override fun toString(): String {

        return "Conta: " + this.idConta + "\n" +
                this.usuario + "\n" +
                "Saldo: " + this.saldoConta +"\n"
    }
}