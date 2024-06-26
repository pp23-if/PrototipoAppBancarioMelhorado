package prototipoappbancariomelhorado.main.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import java.util.LinkedList
import java.util.Locale

class ContaDAO(context: Context) {

    var bancoDeDados = BancoDeDados(context);

    fun cadastrarConta(conta: Conta): Boolean {
        // Obtém uma instância do banco de dados para escrita
        val db = bancoDeDados.writableDatabase

        return try {
            // Inicia a transação
            db.beginTransaction()

            val contentValuesUsuario = ContentValues().apply {

                put("nome", conta.getUsuarioAtributo().getNomeUsuarioAtributo())
                put(
                    "dataNascimento",
                    conta.getUsuarioAtributo().getDataDeNascimentoUsuarioAtributo().toString()
                )
                put("login", conta.getUsuarioAtributo().getLoginUsuarioAtributo())
                put("senha", conta.getUsuarioAtributo().getSenhaUsuarioAtributo())
            }

            val inseridoUsuario = db.insert("Usuario", null, contentValuesUsuario) != -1L

            /*buscar o id do usuario recem inserido*/

            var idUsuarioInserido = pegaIdUsuarioRecemInserido(conta)

            val contentValuesConta = ContentValues().apply {

                put("saldo", 0)
                put("fkUsuario", idUsuarioInserido)
            }
            val inseridoConta = db.insert("Conta", null, contentValuesConta) != -1L

            // Define que a transação foi bem-sucedida
            db.setTransactionSuccessful()

            inseridoUsuario
            inseridoConta
        } catch (e: Exception) {
            // Se ocorrer algum erro, retorna false
            false
        } finally {
            // Finaliza a transação, independentemente do resultado
            db.endTransaction()
            // Fecha o banco de dados
            db.close()
        }
    }

    fun pegaIdUsuarioRecemInserido(conta: Conta): Int {

        var idUsuario: Int = -1

        var recuperarIdUsuario = bancoDeDados.readableDatabase
        val consulta = "SELECT idUsuario FROM Usuario WHERE login = ?"
        val argumentos = arrayOf(conta.getUsuarioAtributo().getLoginUsuarioAtributo())
        var cursorIdUsuario = recuperarIdUsuario.rawQuery(consulta, argumentos);

        with(cursorIdUsuario) {
            while (moveToNext()) {
                idUsuario = getInt(getColumnIndexOrThrow("idUsuario"))
            }
        }
        cursorIdUsuario.close()

        return idUsuario;
    }


    fun pegarContasComoTexto(listaDeUsuarios: ArrayList<Usuario>): String {

        var contas = "";

        var recuperarDadosContas = bancoDeDados.readableDatabase
        val consulta = "SELECT * FROM Conta WHERE fkUsuario = ?"

        listaDeUsuarios.forEach { usuario ->

            val argumentos = arrayOf(usuario.getIdUsuarioAtributo().toString())

            var cursorContas = recuperarDadosContas.rawQuery(consulta, argumentos);

            with(cursorContas) {
                while (moveToNext()) {

                    var idConta = getInt(getColumnIndexOrThrow("idConta"))
                    var saldo = getDouble(getColumnIndexOrThrow("saldo"))


                    var conta = Conta(idConta, usuario, saldo)

                    contas += conta.toString() + "\n\n"

                }
            }
            cursorContas.close()
        }
        return contas;
    }

    @SuppressLint("SuspiciousIndentation")
    fun pegarContaUsuario(usuario: Usuario): Conta? {

        var recuperarDadosContas = bancoDeDados.readableDatabase
        val consulta = "SELECT * FROM Conta WHERE fkUsuario = ?"

        val argumentos = arrayOf(usuario.getIdUsuarioAtributo().toString())

        var conta: Conta? = null

        var cursorContas = recuperarDadosContas.rawQuery(consulta, argumentos);

        with(cursorContas) {
            while (moveToNext()) {

                var idConta = getInt(getColumnIndexOrThrow("idConta"))
                var saldo = getDouble(getColumnIndexOrThrow("saldo"))

                conta = Conta(idConta, usuario, saldo)

            }
        }
        cursorContas.close()

        return conta;
    }

    fun pegaSaldoConta(conta: Conta): Double {

        var saldoConta: Double = 0.0

        var recuperarSaldoConta = bancoDeDados.readableDatabase
        val consulta = "SELECT saldo FROM Conta WHERE idConta = ?"
        val argumentos = arrayOf(conta.getIdContaAtributo().toString())
        var cursorSaldoConta = recuperarSaldoConta.rawQuery(consulta, argumentos);

        with(cursorSaldoConta) {
            while (moveToNext()) {
                saldoConta = getDouble(getColumnIndexOrThrow("saldo"))
            }
        }
        cursorSaldoConta.close()

        return saldoConta;
    }

    fun pegarContasExcetoUsuarioLogado(listaDeUsuarios: ArrayList<Usuario>): LinkedList <SelecaoConta> {

        var recuperarDadosContas = bancoDeDados.readableDatabase
        val consulta = "SELECT * FROM Conta WHERE fkUsuario = ?"

        var listaDeSelecaoContas = LinkedList<SelecaoConta>()


        listaDeUsuarios.forEach { usuario ->

            val argumentos = arrayOf(usuario.getIdUsuarioAtributo().toString())

            var cursorContas = recuperarDadosContas.rawQuery(consulta, argumentos);

            with(cursorContas) {
                while (moveToNext()) {

                    var idConta = getInt(getColumnIndexOrThrow("idConta"))
                    var saldo = getDouble(getColumnIndexOrThrow("saldo"))

                    var conta = Conta(idConta, usuario, saldo)

                    var selecaoConta = SelecaoConta(conta)

                    listaDeSelecaoContas.add(selecaoConta)

                }
            }
            cursorContas.close()
        }

        return listaDeSelecaoContas;
    }

    fun pegarContaUsuarioPorId(idConta : Int, usuarioDAO: UsuarioDAO): Conta? {

        var recuperarDadosContas = bancoDeDados.readableDatabase
        val consulta = "SELECT * FROM Conta WHERE idConta = ?"

        val argumentos = arrayOf(idConta.toString())

        var conta: Conta? = null

        var cursorContas = recuperarDadosContas.rawQuery(consulta, argumentos);

        with(cursorContas) {
            while (moveToNext()) {

                var idConta = getInt(getColumnIndexOrThrow("idConta"))
                var fkUsuario = getInt(getColumnIndexOrThrow("fkUsuario"))
                var saldo = getDouble(getColumnIndexOrThrow("saldo"))

                var usuario = usuarioDAO.pegarDadosUsuarioPorValorID(fkUsuario)


                conta = Conta(idConta, usuario!!, saldo)

            }
        }
        cursorContas.close()

        return conta;
    }

}