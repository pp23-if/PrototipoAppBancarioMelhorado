package prototipoappbancariomelhorado.main.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import java.util.Locale

class MovimentacaoDAO (context: Context) {

    var bancoDeDados = BancoDeDados(context);

    fun realizarMovimentacao(movimentacao: Movimentacao, saldoAtualizado : Double): Boolean {
        // Obtém uma instância do banco de dados para escrita
        val db = bancoDeDados.writableDatabase

        return try {
            // Inicia a transação
            db.beginTransaction()

            val contentValuesMovimentacao = ContentValues().apply {

                put("fkConta", movimentacao.getContaAtributo().getIdContaAtributo())
                put(
                    "tipoMovimentacao",
                    movimentacao.getTipoMovimentacaoAtributo()
                )
                put("valorMovimentacao", movimentacao.getValorMovimentacaoAtributo())
                put("dataMovimentacao", movimentacao.getDataMovimentacaoAtributo().toString())
            }

            val inseridaMovimentacao = db.insert("Movimentacao", null, contentValuesMovimentacao) != -1L


            /*Aqui comeca os operacoes no banco de dados, de UPDATE SALDO CONTA*/

            val contentValuesAtualizacaoSaldoConta = ContentValues().apply {

                put("saldo", saldoAtualizado)
                put("idConta", movimentacao.getContaAtributo().getIdContaAtributo())
            }
            //val inseridoConta = db.insert("Conta", null, contentValuesConta) != -1L

            val condicao = "idConta = ?"
            val argumentos = arrayOf(movimentacao.getContaAtributo().getIdContaAtributo().toString())

            val confirmaAtualizacao = db.update("Conta", contentValuesAtualizacaoSaldoConta, condicao, argumentos) >= 1

            // Define que a transação foi bem-sucedida
            db.setTransactionSuccessful()

            inseridaMovimentacao
            confirmaAtualizacao
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

}