package com.example.mijuego

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var hintTextView: TextView
    private lateinit var respuestaText: EditText
    private lateinit var vidasTextView: TextView
    private var vidas = 3
    private val answers = arrayOf("SOL", "TREN", "FUEGO", "NUBE", "MOUSE")
    private val hints = arrayOf(
        "Sale de día y se esconde de noche",
        "Más rápido que un corredor y no tiene corazón",
        "En tierra no crece, en agua no se moja",
        "Camina sin pies y vuela sin alas",
        "Subo y bajo y no me muevo"
    )

    private var currentHintIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicialización de las vistas
        hintTextView = findViewById(R.id.hint_text)
        respuestaText = findViewById(R.id.respuesta_text)
        vidasTextView = findViewById(R.id.vidas_text)
        val restartButton: Button = findViewById(R.id.button7)

        // Configurar el layout del crucigrama
        val gridLayout: GridLayout = findViewById(R.id.grid_layout)

        // Establecer las celdas del crucigrama
        setupCrosswordCells(gridLayout)

        // Añadir el TextWatcher para el EditText de respuesta
        respuestaText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Solo comprobar cuando la longitud de la respuesta sea correcta
                if (s.toString().length == answers[currentHintIndex].length) {
                    checkAnswer(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Manejar el botón de reinicio
        restartButton.setOnClickListener {
            restartGame()
        }

        // Mostrar la primera pista y las vidas iniciales
        updateLivesAndHint()
    }

    private fun setupCrosswordCells(gridLayout: GridLayout) {
        val cellIds = arrayOf(
            R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2, R.id.cell_0_3, R.id.cell_0_4,
            R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2, R.id.cell_1_3, R.id.cell_1_4,
            R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2, R.id.cell_2_3, R.id.cell_2_4,
            R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2, R.id.cell_3_3, R.id.cell_3_4,
            R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2, R.id.cell_4_3, R.id.cell_4_4
        )

        // Limpiar y habilitar las celdas antes de llenarlas
        for (id in cellIds) {
            val cell: EditText = findViewById(id)
            cell.setText("")
            cell.isEnabled = true // Habilitar las celdas en el reinicio
        }

        // Mostrar la pista actual
        hintTextView.text = hints[currentHintIndex]
    }

    private fun checkAnswer(answer: String) {
        if (answer.equals(answers[currentHintIndex], ignoreCase = true)) {
            // La respuesta es correcta
            fillCorrectAnswer(currentHintIndex) // Rellenar las celdas con las letras de la respuesta
            respuestaText.text.clear() // Limpiar el campo de respuesta

            currentHintIndex++
            if (currentHintIndex >= answers.size) {
                // Si se completaron todas las respuestas
                fillCorrectAnswer(4) // Rellenar "MOUSE"
                hintTextView.text = "¡Ganaste! Has completado el crucigrama."
                respuestaText.isEnabled = false // Desactivar la entrada de texto
            } else {
                // Mostrar la siguiente pista si hay más
                hintTextView.text = hints[currentHintIndex]
            }
        } else {
            // Respuesta incorrecta, restar vida
            vidas--
            if (vidas <= 0) {
                // Manejar fin del juego
                hintTextView.text = "Fin del juego. ¡Intenta de nuevo!"
                respuestaText.isEnabled = false // Desactivar la entrada de texto
            }
        }
        updateLivesAndHint()
    }

    private fun fillCorrectAnswer(index: Int) {
        // Posiciones de las respuestas en el crucigrama
        val answerPositions = arrayOf(
            // "SOL" - horizontal
            arrayOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
            // "TREN" - vertical
            arrayOf(Pair(3, 2), Pair(4, 2), Pair(1, 3), Pair(2, 3)),
            // "FUEGO" - horizontal
            arrayOf(Pair(1, 0), Pair(2, 0), Pair(3, 0), Pair(4, 0), Pair(1, 1)),
            // "NUBE" - horizontal
            arrayOf(Pair(1, 4), Pair(2, 4), Pair(3, 4), Pair(4, 4)),
            // "MOUSE" - Vertical
            arrayOf(Pair(0, 4), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4, 4))
        )

        val cellIds = arrayOf(
            R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2, R.id.cell_0_3, R.id.cell_0_4,
            R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2, R.id.cell_1_3, R.id.cell_1_4,
            R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2, R.id.cell_2_3, R.id.cell_2_4,
            R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2, R.id.cell_3_3, R.id.cell_3_4,
            R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2, R.id.cell_4_3, R.id.cell_4_4
        )

        // Rellenar las celdas con las letras de la respuesta correcta
        for ((positionIndex, pos) in answerPositions[index].withIndex()) {
            val (row, col) = pos
            val cell: EditText = findViewById(cellIds[row * 5 + col])
            // Asignar solo una letra a cada celda
            cell.setText(answers[index][positionIndex].toString())
            cell.isEnabled = false // Desactivar la celda después de rellenar
        }
    }

    private fun updateLivesAndHint() {
        vidasTextView.text = "Vidas: $vidas"
    }

    private fun restartGame() {
        // Lógica para reiniciar el juego
        vidas = 3
        currentHintIndex = 0
        setupCrosswordCells(findViewById(R.id.grid_layout))
        updateLivesAndHint()
        respuestaText.text.clear() // Limpiar la respuesta
        respuestaText.isEnabled = true // Habilitar la entrada de texto
    }
}