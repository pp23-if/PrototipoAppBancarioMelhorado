package prototipoappbancariomelhorado.main.model

import android.icu.text.DecimalFormat
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
class SelecaoConta (private var conta: Conta): Parcelable {

    fun getContaAtributo(): Conta {
        return this.conta
    }
    override fun toString(): String {

        return  "Conta Numero: " + this.conta.getIdContaAtributo() + " | " +
                "Titular: " + this.conta.getUsuarioAtributo().getNomeUsuarioAtributo()

    }

}