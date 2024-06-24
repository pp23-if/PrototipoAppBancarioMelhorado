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

    fun verificaExistenciaLoginInformadoAtualizacao(login : String, conta: Conta) : String
    {

        var verificarLogin = bancoDeDados.readableDatabase
        val consulta = "SELECT login FROM Usuario WHERE (login = ? or login = ?) and login != ?"
        val argumentos = arrayOf(login.lowercase(), login.uppercase(), conta.getUsuarioAtributo().getLoginUsuarioAtributo())
        var cursorLoginUsuario = verificarLogin.rawQuery(consulta,argumentos);

        var loginEncontrado = "";

        with(cursorLoginUsuario) {
            while (moveToNext()) {
                loginEncontrado = getString(getColumnIndexOrThrow("login"))
            }
        }
        cursorLoginUsuario.close()

        return loginEncontrado;
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

    fun pegarDadosUsuarioPorLoginSenha(login: String, senha: String): Usuario? {

        val recuperarDadosUsuario = bancoDeDados.readableDatabase
        val cursorUsuario = recuperarDadosUsuario.rawQuery("SELECT * FROM Usuario WHERE login = ? AND senha = ?", arrayOf(login, senha))

        val dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var usuario : Usuario? = null

        with(cursorUsuario) {
            while (moveToNext()) {
                val idUsuario = getInt(getColumnIndexOrThrow("idUsuario"))
                val nome = getString(getColumnIndexOrThrow("nome"))
                val dataNascimentoStr = getString(getColumnIndexOrThrow("dataNascimento"))
                val login = getString(getColumnIndexOrThrow("login"))
                val senha = getString(getColumnIndexOrThrow("senha"))

                val dataNascimentoFormatada = LocalDate.parse(dataNascimentoStr, dataFormatada)

                usuario = Usuario(idUsuario, nome, dataNascimentoFormatada, login, senha)

            }
        }
        cursorUsuario.close()

        return usuario
    }

    fun pegarDadosUsuarioPorID (usuario: Usuario): Usuario? {

        val recuperarDadosUsuario = bancoDeDados.readableDatabase
        val cursorUsuario = recuperarDadosUsuario.rawQuery("SELECT * FROM Usuario WHERE idUsuario = ?", arrayOf(usuario.getIdUsuarioAtributo().toString()))

        val dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        var usuario : Usuario? = null

        with(cursorUsuario) {
            while (moveToNext()) {
                val idUsuario = getInt(getColumnIndexOrThrow("idUsuario"))
                val nome = getString(getColumnIndexOrThrow("nome"))
                val dataNascimentoStr = getString(getColumnIndexOrThrow("dataNascimento"))
                val login = getString(getColumnIndexOrThrow("login"))
                val senha = getString(getColumnIndexOrThrow("senha"))

                val dataNascimentoFormatada = LocalDate.parse(dataNascimentoStr, dataFormatada)

                usuario = Usuario(idUsuario, nome, dataNascimentoFormatada, login, senha)

            }
        }
        cursorUsuario.close()

        return usuario
    }


    fun atualizarDadosUsuario(usuario: Usuario, usuarioAtualizado: Usuario) : Boolean
    {
        val atualizacaoUsuario = bancoDeDados.writableDatabase
        var blocoDeValores = ContentValues().apply {

            put("nome", usuarioAtualizado.getNomeUsuarioAtributo())
            put("dataNascimento", usuarioAtualizado.getDataDeNascimentoUsuarioAtributo().toString())
            put("login", usuarioAtualizado.getLoginUsuarioAtributo())
            put("senha", usuarioAtualizado.getSenhaUsuarioAtributo())

        }

        val condicao = "idUsuario = ? and login = ?"
        val argumentos = arrayOf(usuario.getIdUsuarioAtributo().toString(), usuario.getLoginUsuarioAtributo())

        val confirmaAtualizacao = atualizacaoUsuario.update("Usuario", blocoDeValores, condicao, argumentos)

        return confirmaAtualizacao >= 1

    }


}