package prototipoappbancariomelhorado.main.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.LinkedList
import java.util.Locale

class TransferenciaDAO (context: Context) {

    var bancoDeDados = BancoDeDados(context);

    fun realizarTransferencia(transferenciaFeita: Transferencia, transferenciaRecebida: Transferencia,
                              saldoAtualizadoContaOrigem : Double, saldoAtualizadoContaDestino : Double): Boolean {
        // Obtém uma instância do banco de dados para escrita
        val db = bancoDeDados.writableDatabase

        return try {
            // Inicia a transação

            db.beginTransaction()

            //bloco de insercao da transferencia feita

            val contentValuesTransferenciaFeita = ContentValues().apply {

                put("fkContaEntrada", transferenciaFeita.getContaEntradaAtributo().getIdContaAtributo())
                put(
                    "fkContaSaida",
                    transferenciaFeita.getContaSaidaAtributo().getIdContaAtributo()
                )
                put("tipoTransferencia", "feito")
                put("valorTransferencia", transferenciaFeita.getValorTransferenciaAtributo())
                put("dataTransferencia", transferenciaFeita.getDataTransferenciaoAtributo().toString())
            }

            val inseridaTransferenciaFeita = db.insert("Transferencia", null, contentValuesTransferenciaFeita) != -1L


            //bloco de insercao da transferencia recebida


            val contentValuesTransferenciaRecebido = ContentValues().apply {

                put("fkContaEntrada", transferenciaRecebida.getContaEntradaAtributo().getIdContaAtributo())
                put(
                    "fkContaSaida",
                    transferenciaRecebida.getContaSaidaAtributo().getIdContaAtributo()
                )
                put("tipoTransferencia", "recebido")
                put("valorTransferencia", transferenciaRecebida.getValorTransferenciaAtributo())
                put("dataTransferencia", transferenciaRecebida.getDataTransferenciaoAtributo().toString())
            }

            val inseridaTransferenciaRecebida = db.insert("Transferencia", null, contentValuesTransferenciaRecebido) != -1L



            /*Aqui comeca os operacoes no banco de dados, de UPDATE SALDO CONTA*/


            //Atualizacao saldo Conta Origem

            val contentValuesAtualizacaoSaldoContaOrigem = ContentValues().apply {
                put("saldo", saldoAtualizadoContaOrigem)
            }

            val condicaoContaOrigem = "idConta = ?"
            val argumentosContaOrigem = arrayOf(transferenciaFeita.getContaEntradaAtributo().getIdContaAtributo().toString())

            val confirmaAtualizacaoContaOrigem = db.update("Conta", contentValuesAtualizacaoSaldoContaOrigem, condicaoContaOrigem, argumentosContaOrigem) >= 1


            //Atualizacao saldo Conta Destino

            val contentValuesAtualizacaoSaldoContaDestino = ContentValues().apply {
                put("saldo", saldoAtualizadoContaDestino)
            }

            val condicaoContaDestino = "idConta = ?"
            val argumentosContaDestino = arrayOf(transferenciaRecebida.getContaEntradaAtributo().getIdContaAtributo().toString())

            val confirmaAtualizacaoContaDestino = db.update("Conta", contentValuesAtualizacaoSaldoContaDestino, condicaoContaDestino, argumentosContaDestino) >= 1

            // Define que a transação foi bem-sucedida
            db.setTransactionSuccessful()

            inseridaTransferenciaFeita
            inseridaTransferenciaRecebida
            confirmaAtualizacaoContaOrigem
            confirmaAtualizacaoContaDestino

        } catch (e: Exception) {
            false
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun pegarTransferenciasConta(conta: Conta, contaDAO: ContaDAO, usuarioDAO: UsuarioDAO): LinkedList<Transferencia> {

        val listaDeTransferencia = LinkedList<Transferencia>()

        val recuperarDadosTransferencia = bancoDeDados.readableDatabase
        val cursorTransferencia = recuperarDadosTransferencia.rawQuery("SELECT * FROM Transferencia WHERE fkContaEntrada = ? or fkContaSaida = ?",
            arrayOf(conta.getIdContaAtributo().toString()))

        val dataFormatada: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        with(cursorTransferencia) {
            while (moveToNext()) {

                val idTransferencia = getInt(getColumnIndexOrThrow("idTransferencia"))
                val fkContaEntrada = getInt(getColumnIndexOrThrow("fkContaEntrada"))
                val fkContaSaida = getInt(getColumnIndexOrThrow("fkContaSaida"))
                val tipoTransferencia = getString(getColumnIndexOrThrow("tipoTransferencia"))
                val valorTransferencia = getDouble(getColumnIndexOrThrow("valorTransferencia"))
                val dataTransferencia = getString(getColumnIndexOrThrow("dataTransferencia"))

                var dataAposTratamento  = dataTransferencia.substring(0, 19).replace("T", " ");
                val dataTransferenciaFormatada = LocalDateTime.parse(dataAposTratamento, dataFormatada)

                var transferencia : Transferencia
                
                if(fkContaEntrada == conta.getIdContaAtributo())
                {
                   var conta2 = contaDAO.pegarContaUsuarioPorId(fkContaSaida, usuarioDAO)

                   transferencia = Transferencia(idTransferencia, conta, conta2!!, tipoTransferencia, valorTransferencia, dataTransferenciaFormatada)

                   listaDeTransferencia.add(transferencia)
                }

                if(fkContaSaida == conta.getIdContaAtributo())
                {
                    var conta2 = contaDAO.pegarContaUsuarioPorId(fkContaEntrada, usuarioDAO)

                    transferencia = Transferencia(idTransferencia, conta2!!, conta, tipoTransferencia, valorTransferencia, dataTransferenciaFormatada)

                    listaDeTransferencia.add(transferencia)
                }

            }
        }
        cursorTransferencia.close()

        return listaDeTransferencia
    }

}