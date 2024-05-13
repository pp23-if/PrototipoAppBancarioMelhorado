package prototipoappbancariomelhorado.main.model

import android.content.ContentValues
import android.content.Context
import java.util.Locale

class ContaDAO (context: Context) {

    var bancoDeDados = BancoDeDados(context);

    fun cadastrarConta(conta: Conta): Boolean {
        // Obtém uma instância do banco de dados para escrita
        val db = bancoDeDados.writableDatabase

        return try {
            // Inicia a transação
            db.beginTransaction()

            val contentValuesUsuario = ContentValues().apply {

                put("nome", conta.getUsuarioAtributo().getNomeUsuarioAtributo())
                put("dataNascimento", conta.getUsuarioAtributo().getDataDeNascimentoUsuarioAtributo().toString())
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

    fun pegaIdUsuarioRecemInserido(conta: Conta) : Int
    {

        var idUsuario : Int = -1

        var recuperarIdUsuario = bancoDeDados.readableDatabase
        val consulta = "SELECT idUsuario FROM Usuario WHERE login = ?"
        val argumentos = arrayOf(conta.getUsuarioAtributo().getLoginUsuarioAtributo())
        var cursorIdUsuario = recuperarIdUsuario.rawQuery(consulta,argumentos);

        with(cursorIdUsuario) {
            while (moveToNext()) {
                idUsuario = getInt(getColumnIndexOrThrow("idUsuario"))
            }
        }
        cursorIdUsuario.close()

        return idUsuario;
    }


   /* fun pegarContasComoTexto() : String
    {

        var contas = "";

        var listaDeUsuario = pegarDadosUsuarios()

        var recuperarDadosContas = bancoDeDados.readableDatabase
        val consulta = "SELECT * FROM Conta WHERE fkUsuario = ?"

        listaDeUsuario.forEach { usuario ->

            val argumentos = arrayOf(usuario.id.toString())

            var cursorContas = recuperarDadosContas.rawQuery(consulta,argumentos);

            with(cursorContas) {
                while (moveToNext()) {

                    var numero = getInt(getColumnIndexOrThrow("numero"))
                    var saldo = getDouble(getColumnIndexOrThrow("saldo"))

                    var conta = Conta();
                    conta.numero = numero;
                    conta.saldo = saldo;
                    conta.usuario = usuario;

                    contas += conta.toString() + "\n\n"

                }
            }
            cursorContas.close()
        }
        return contas;
    }*/

}