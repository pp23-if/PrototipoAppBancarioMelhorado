package prototipoappbancariomelhorado.main.model

import android.content.ContentValues
import android.content.Context
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

    /*fun pegarDadosUsuarios() : ArrayList<Usuario>
    {
        val listaDeUsuario = ArrayList<Usuario>();

        var recuperarDadosCliente = bancoDeDados.readableDatabase
        var cursorCliente = recuperarDadosCliente.rawQuery("select * from Usuario",null)

        with(cursorCliente) {
            while (moveToNext()) {

                var id = getInt(getColumnIndexOrThrow("id"))
                var nome = getString(getColumnIndexOrThrow("nome"))
                var idade = getInt(getColumnIndexOrThrow("idade"))


                var usuario = Usuario()
                usuario.id = id
                usuario.nome = nome
                usuario.idade = idade

                listaDeUsuario.add(usuario);

            }
        }
        cursorCliente.close()

        return listaDeUsuario;
    }*/

}