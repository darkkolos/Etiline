package com.example.etiline.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.etiline.entidades.Taxa

class DataBaseAdapter() {
    val dbNome: String = "BDEtiline"
    val dbTabela: String = "Taxa"
    val dbVersao: Int = 1
    val sqlCriar: String = "CREATE TABLE $dbTabela (T_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "T_PERCENTAGEM INTEGER NOT NULL, " +
            "T_QUANTIDADE INTEGER NOT NULL, " +
            "T_PESO INTEGER NOT NULL, " +
            "T_FEMININO BOOLEAN NOT NULL, " +
            "T_REFEICAO BOOLEAN NOT NULL, " +
            "T_TAXAFINAL FLOAT NOT NULL, " +
            "T_DATA DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
            "T_DESCRICAO STRING NOT NULL)"
    val sqlDestruir: String = "DROP TABLE IF EXISTS $dbTabela"
    lateinit var databaseHelper: DatabaseHelper
    lateinit var sqlSQLiteDatabase: SQLiteDatabase

    constructor(context: Context?) : this() {
        databaseHelper = DatabaseHelper(context, sqlCriar, sqlDestruir, dbNome, null, dbVersao)
    }

    class DatabaseHelper(
        val context: Context?,
        val sqlCriarDatabaseHelper: String,
        val sqlDestruirDatabaseHelper: String,
        name: String?,
        factory: SQLiteDatabase.CursorFactory?,
        version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlCriarDatabaseHelper)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL(sqlDestruirDatabaseHelper)
            onCreate(db)
        }
    }

    fun abrir() {
        sqlSQLiteDatabase = databaseHelper.writableDatabase
    }

    fun fechar() {
        databaseHelper.close()
    }

    fun inserirTaxa(taxa: Taxa): Long {
        val vals = ContentValues()
        vals.put("T_PERCENTAGEM", taxa.percentagem)
        vals.put("T_QUANTIDADE", taxa.quantidade)
        vals.put("T_PESO", taxa.peso)
        if (taxa.feminino) {
            vals.put("T_FEMININO", 1)
        } else {
            vals.put("T_FEMININO", 0)
        }
        if (taxa.refeicao) {
            vals.put("T_REFEICAO", 1)
        } else {
            vals.put("T_REFEICAO", 0)
        }
        vals.put("T_TAXAFINAL", taxa.taxaFinal)
        vals.put("T_DESCRICAO", taxa.descricao)
        return sqlSQLiteDatabase.insert(
            dbTabela,
            null,
            vals
        )

    }

    fun editarTaxa(taxa: Taxa): Int {
        val vals = ContentValues()
        vals.put("T_PERCENTAGEM", taxa.percentagem)
        vals.put("T_QUANTIDADE", taxa.quantidade)
        vals.put("T_PESO", taxa.peso)
        if (taxa.feminino) {
            vals.put("T_FEMININO", 1)
        } else {
            vals.put("T_FEMININO", 0)
        }
        if (taxa.refeicao) {
            vals.put("T_REFEICAO", 1)
        } else {
            vals.put("T_REFEICAO", 0)
        }
        vals.put("T_TAXAFINAL", taxa.taxaFinal)
        vals.put("T_DESCRICAO", taxa.descricao)
        return sqlSQLiteDatabase.update(
            dbTabela,
            vals,
            "T_ID=?", arrayOf(taxa.id.toString())
        )
    }

    fun obterTaxas(tipo: Boolean, ordem: Boolean): Cursor? {
        var ordenar = ""
        ordenar = if (tipo) {
            if (ordem) {
                "T_DATA DESC"
            } else {
                "T_DATA ASC"
            }
        } else {
            if (ordem) {
                "T_TAXAFINAL DESC"
            } else {
                "T_TAXAFINAL ASC"
            }
        }
        return sqlSQLiteDatabase.query(
            dbTabela,
            arrayOf(
                "T_ID",
                "T_PERCENTAGEM",
                "T_QUANTIDADE",
                "T_PESO",
                "T_FEMININO",
                "T_REFEICAO",
                "T_TAXAFINAL",
                "T_DATA",
                "T_DESCRICAO"
            ),
            null,
            null,
            null,
            null,
            ordenar
        )
    }

    fun obterTaxa(id: Int): Cursor? {
        return sqlSQLiteDatabase.query(
            dbTabela,
            arrayOf(
                "T_ID",
                "T_PERCENTAGEM",
                "T_QUANTIDADE",
                "T_PESO",
                "T_FEMININO",
                "T_REFEICAO",
                "T_TAXAFINAL",
                "T_DATA",
                "T_DESCRICAO"
            ),
            "T_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
    }

    fun removerTaxa(id: Int): Int {
        return sqlSQLiteDatabase.delete(
            dbTabela,
            "T_ID=?", arrayOf(id.toString())
        )
    }
}


