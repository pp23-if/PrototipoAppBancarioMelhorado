package prototipoappbancariomelhorado.main.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// Factory -> Used to allow returning sub-classes of Cursor when calling query.
class BancoDeDados(context: Context) : SQLiteOpenHelper(context, "BancoDeDados", null, 1)
{
    override fun onCreate(db: SQLiteDatabase)
    {

        /*Tabela De Usuario*/

        val nomeTabela1 = "Usuario"
        val idUsuario = "idUsuario"
        val nome = "nome"
        val dataNascimento = "dataNascimento"
        val login = "login"
        val senha = "senha"
        val SQL_criacaoUsuario =
            "CREATE TABLE ${nomeTabela1} (" +
                    "${idUsuario} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${nome} TEXT," +
                    "${dataNascimento} TEXT," +
                    "${login} TEXT," +
                    "${senha} TEXT)"


        /*Tabela De Conta*/

        val nomeTabela2 = "Conta"
        val idConta = "idConta"
        val fkUsuario = "fkUsuario"
        val saldo = "saldo"
        val SQL_criacaoConta =
            "CREATE TABLE ${nomeTabela2} (" +
                    "${idConta} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${fkUsuario} INTEGER," +
                    "${saldo} DOUBLE," +
                    "FOREIGN KEY(${fkUsuario}) REFERENCES ${nomeTabela1}(${idUsuario}))"


        /*Tabela De Movimentacao*/

        val nomeTabela3 = "Movimentacao"
        val idMovimentacao = "idMovimentacao"
        val fkConta = "fkConta"
        val tipoMovimentacao = "tipoMovimentacao"
        val valorMovimentacao = "valorMovimentacao"
        val dataMovimentacao = "dataMovimentacao"
        val SQL_criacaoMovimentacao =
            "CREATE TABLE ${nomeTabela3} (" +
                    "${idMovimentacao} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${fkConta} INTEGER," +
                    "${tipoMovimentacao} TEXT," +
                    "${valorMovimentacao} DOUBLE," +
                    "${dataMovimentacao} TEXT," +
                    "FOREIGN KEY(${fkConta}) REFERENCES ${nomeTabela2}(${idConta}))"



        /*Tabela De Transferencia*/

        val nomeTabela4 = "Transferencia"
        val idTransferencia = "idTransferencia"
        val fkContaEntrada = "fkContaEntrada"
        val fkContaSaida = "fkContaSaida"
        val tipoTransferencia = "tipoTransferencia"
        val valorTransferencia = "valorTransferencia"
        val dataTransferencia = "dataTransferencia"
        val SQL_criacaoTransferencia =
            "CREATE TABLE ${nomeTabela4} (" +
                    "${idTransferencia} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${fkContaEntrada} INTEGER," +
                    "${fkContaSaida} INTEGER," +
                    "${tipoTransferencia} TEXT," +
                    "${valorTransferencia} DOUBLE," +
                    "${dataTransferencia} TEXT," +
                    "FOREIGN KEY(${fkContaEntrada}) REFERENCES ${nomeTabela2}(${idConta})," +
                    "FOREIGN KEY(${fkContaSaida}) REFERENCES ${nomeTabela2}(${idConta}))"

        db.execSQL(SQL_criacaoUsuario)
        db.execSQL(SQL_criacaoConta)
        db.execSQL(SQL_criacaoMovimentacao)
        db.execSQL(SQL_criacaoTransferencia)

    }
    override fun onUpgrade(db: SQLiteDatabase, versaoAntiga: Int, novaVersao: Int) {
        val SQL_exclusao_Usuario = "DROP TABLE IF EXISTS Usuario"
        val SQL_exclusao_Conta = "DROP TABLE IF EXISTS Conta"
        val SQL_exclusao_Movimentacao = "DROP TABLE IF EXISTS Movimentacao"
        val SQL_exclusao_Transferencia = "DROP TABLE IF EXISTS Transferencia"

        db.execSQL(SQL_exclusao_Usuario)
        db.execSQL(SQL_exclusao_Conta)
        db.execSQL(SQL_exclusao_Movimentacao)
        db.execSQL(SQL_exclusao_Transferencia)

        onCreate(db)
    }


}