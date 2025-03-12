package com.win.kalkulatornilaiubj

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Menyesuaikan tampilan agar tidak tertutup status bar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Menghubungkan variabel dengan ID di XML
        val inputAbsen = findViewById<EditText>(R.id.input_absen)
        val inputTugas = findViewById<EditText>(R.id.input_tugas)
        val inputUTS = findViewById<EditText>(R.id.input_uts)
        val inputUAS = findViewById<EditText>(R.id.input_uas)
        val nilaiAngka = findViewById<TextView>(R.id.nilaiangka)
        val nilaiHuruf = findViewById<TextView>(R.id.nilaihuruf)

        // Menutup keyboard setelah mengetik 2 angka
        listOf(inputAbsen).forEach { input ->
            input.setOnKeyListener { _, _, event ->
                if (event.action == KeyEvent.ACTION_UP && input.text.length == 3) {
                    closeKeyboard(input)
                    input.clearFocus()
                    true
                } else {
                    false
                }
            }
        }

        listOf(inputTugas, inputUTS, inputUAS).forEach { input ->
            input.setOnKeyListener { _, _, event ->
                if (event.action == KeyEvent.ACTION_UP && input.text.length == 2) {
                    closeKeyboard(input)
                    input.clearFocus()
                    true
                } else {
                    false
                }
            }
        }

        // Memastikan input berubah, lalu hitung nilai
        listOf(inputAbsen, inputTugas, inputUTS, inputUAS).forEach { input ->
            input.setOnFocusChangeListener { _, _ ->
                hitungNilai(inputAbsen, inputTugas, inputUTS, inputUAS, nilaiAngka, nilaiHuruf)
            }
        }
    }

    // Fungsi untuk menutup keyboard
    private fun closeKeyboard(editText: EditText) {
        val imm = editText.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    // Fungsi untuk menghitung nilai
    private fun hitungNilai(
        inputAbsen: EditText,
        inputTugas: EditText,
        inputUTS: EditText,
        inputUAS: EditText,
        nilaiAngka: TextView,
        nilaiHuruf: TextView
    ) {
        // Mengambil nilai dari input (jika kosong, default 0)
        val absen = inputAbsen.text.toString().toFloatOrNull() ?: 0f
        val tugas = inputTugas.text.toString().toFloatOrNull() ?: 0f
        val uts = inputUTS.text.toString().toFloatOrNull() ?: 0f
        val uas = inputUAS.text.toString().toFloatOrNull() ?: 0f

        // Rumus perhitungan
        val nilaiAkhir = (0.1f * absen) + (0.2f * tugas) + (0.3f * uts) + (0.4f * uas)

        // Menentukan nilai huruf berdasarkan nilai akhir
        val nilaiHurufResult = when {
            nilaiAkhir >= 80 -> "A"
            nilaiAkhir >= 76 -> "A-"
            nilaiAkhir >= 72 -> "B+"
            nilaiAkhir >= 68 -> "B"
            nilaiAkhir >= 64 -> "B-"
            nilaiAkhir >= 60 -> "C+"
            nilaiAkhir >= 56 -> "C"
            nilaiAkhir >= 45 -> "D"
            else -> "E"
        }

        // Menampilkan hasil ke TextView
        nilaiAngka.text = String.format("%.2f", nilaiAkhir)
        nilaiHuruf.text = nilaiHurufResult
    }
}
