package prototipoappbancariomelhorado.main.controller

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import prototipoappbancariomelhorado.main.R
import prototipoappbancariomelhorado.main.model.Conta
import prototipoappbancariomelhorado.main.model.ContaDAO
import prototipoappbancariomelhorado.main.model.Usuario
import prototipoappbancariomelhorado.main.model.UsuarioDAO
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ControladoraVisualizacaoRegistros : AppCompatActivity() {

    lateinit var textoContas : TextView;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros)

        textoContas = findViewById(R.id.textViewRegistros)

        textoContas.text = obterRegistrosDoBundle(intent.extras).toString()


    }

    fun obterRegistrosDoBundle(bundle: Bundle?): String? {
        return bundle?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getString("registros")
            } else {
                getString("registros")
            }
        }
    }

}