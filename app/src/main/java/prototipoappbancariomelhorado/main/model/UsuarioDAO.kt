package prototipoappbancariomelhorado.main.model

import android.content.ContentValues
import android.content.Context
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class UsuarioDAO (context: Context) {

    var bancoDeDados = BancoDeDados(context);


    fun verificaExistenciaLoginInformado(login : String) : String
    {

        var verificarLogin = bancoDeDados.readableDatabase
        val consulta = "SELECT login FROM Usuario WHERE login = ? or login = ?"
        val argumentos = arrayOf(login.lowercase(), login.uppercase())
        var cursorLoginUsuario = verificarLogin.rawQuery(consulta,argumentos);

        var login = "";

        with(cursorLoginUsuario) {
            while (moveToNext()) {
                login = getString(getColumnIndexOrThrow("login"))
            }
        }
        cursorLoginUsuario.close()

        return login;
    }


    fun pegarDadosUsuarios(): ArrayList<Usuario> {
        val listaDeUsuario = ArrayList<Usuario>()

        val recuperarDadosUsuario = bancoDeDados.readableDatabase
        val cursorUsuario = recuperarDadosUsuario.rawQuery("select * from Usuario", null)

        val dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        with(cursorUsuario) {
            while (moveToNext()) {
                val idUsuario = getInt(getColumnIndexOrThrow("idUsuario"))
                val nome = getString(getColumnIndexOrThrow("nome"))
                val dataNascimentoStr = getString(getColumnIndexOrThrow("dataNascimento"))
                val login = getString(getColumnIndexOrThrow("login"))
                val senha = getString(getColumnIndexOrThrow("senha"))

                val dataNascimentoFormatada = LocalDate.parse(dataNascimentoStr, dataFormatada)

                val usuario = Usuario(idUsuario, nome, dataNascimentoFormatada, login, senha)

                listaDeUsuario.add(usuario)
            }
        }
        cursorUsuario.close()

        return listaDeUsuario
    }

}