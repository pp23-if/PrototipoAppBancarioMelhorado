package prototipoappbancariomelhorado.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
class Usuario (private var idUsuario : Int, private var nomeUsuario : String,
               private var dataDeNascimentoUsuario: LocalDate? = LocalDate.now(),
               private var loginUsuario : String, private var senhaUsuario : String) : Parcelable {


    fun setIdUsuarioAtributo(idUsuario: Int) {
        this.idUsuario = idUsuario
    }

    fun getIdUsuarioAtributo(): Int {
        return this.idUsuario
    }

    fun setNomeUsuarioAtributo(nomeUsuario : String)
    {
        this.nomeUsuario = nomeUsuario
    }

    fun getNomeUsuarioAtributo() : String
    {
        return this.nomeUsuario
    }

    fun setDataDeNascimentoUsuarioAtributo(dataDeNascimentoUsuario : LocalDate)
    {
        this.dataDeNascimentoUsuario = dataDeNascimentoUsuario
    }

    fun getDataDeNascimentoUsuarioAtributo() : LocalDate?
    {
        return this.dataDeNascimentoUsuario
    }

    fun setLoginUsuarioAtributo(loginUsuario : String)
    {
        this.loginUsuario = loginUsuario
    }

    fun getLoginUsuarioAtributo() : String
    {
        return this.loginUsuario
    }

    fun setSenhaUsuarioAtributo(senhaUsuario : String)
    {
        this.senhaUsuario = senhaUsuario
    }

    fun getSenhaUsuarioAtributo() : String
    {
        return this.senhaUsuario
    }

    override fun toString(): String {

        val formatadorDia = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        return    "ID Usuário: " + this.idUsuario + "\n" +
                  "Nome: " + this.nomeUsuario + "\n" +
                  "Data de Nascimento: " + this.dataDeNascimentoUsuario!!.format(formatadorDia) + "\n" +
                  "Login: " + this.loginUsuario +"\n" +
                  "Senha: " + this.senhaUsuario

    }

}