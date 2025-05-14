package com.example.projectpraktikumpam9

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var etEmail: EditText? = null
    private var etPass: EditText? = null
    private var btnMasuk: Button? = null
    private var btnDaftar: Button? = null
    private lateinit var btnLoginAuto: Button
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen dari layout
        etEmail = findViewById(R.id.et_email)
        etPass = findViewById(R.id.et_pass)
        btnMasuk = findViewById(R.id.btn_masuk)
        btnDaftar = findViewById(R.id.btn_daftar)
        btnLoginAuto = findViewById(R.id.btn_login_auto)

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Listener
        btnMasuk?.setOnClickListener(this)
        btnDaftar?.setOnClickListener(this)

        btnLoginAuto.setOnClickListener {
            val email = "ptadelia@gmail.com"
            val password = "12345678"

            mAuth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth?.currentUser
                        Toast.makeText(this, "Login berhasil: ${user?.email}", Toast.LENGTH_SHORT).show()
                        updateUI(user)
                    } else {
                        Toast.makeText(this, "Login otomatis gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_masuk -> login(etEmail?.text.toString(), etPass?.text.toString())
            R.id.btn_daftar -> signUp(etEmail?.text.toString(), etPass?.text.toString())
        }
    }

    private fun signUp(email: String, password: String) {
        if (!validateForm()) return

        mAuth?.createUserWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Toast.makeText(this, "Pendaftaran berhasil: ${user?.email}", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Pendaftaran gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun login(email: String, password: String) {
        if (!validateForm()) return

        mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = mAuth?.currentUser
                    Toast.makeText(this, "Login berhasil: ${user?.email}", Toast.LENGTH_SHORT).show()
                    updateUI(user)
                } else {
                    Toast.makeText(this, "Login gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var result = true

        if (TextUtils.isEmpty(etEmail?.text.toString())) {
            etEmail?.error = "Email diperlukan"
            result = false
        } else {
            etEmail?.error = null
        }

        if (TextUtils.isEmpty(etPass?.text.toString())) {
            etPass?.error = "Password diperlukan"
            result = false
        } else {
            etPass?.error = null
        }

        return result
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this@MainActivity, InsertNoteActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
