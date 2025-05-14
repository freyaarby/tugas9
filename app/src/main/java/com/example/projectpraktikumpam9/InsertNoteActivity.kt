package com.example.projectpraktikumpam9

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertNoteActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var tvEmail: TextView
    private lateinit var tvUid: TextView
    private lateinit var btnKeluar: Button
    private lateinit var etTitle: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnSubmit: Button
    private lateinit var btnViewNotes: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_note)

        // Inisialisasi UI
        tvEmail = findViewById(R.id.tv_email)
        tvUid = findViewById(R.id.tv_uid)
        btnKeluar = findViewById(R.id.btn_keluar)
        etTitle = findViewById(R.id.et_title)
        etDesc = findViewById(R.id.et_description)
        btnSubmit = findViewById(R.id.btn_submit)
        btnViewNotes = findViewById(R.id.btn_view_notes)

        // Inisialisasi Firebase
        mAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        // Set listener tombol
        btnKeluar.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnViewNotes.setOnClickListener {
            startActivity(Intent(this, NoteListActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
        if (currentUser != null) {
            tvEmail.text = currentUser.email
            tvUid.text = currentUser.uid
        } else {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_keluar -> logOut()
            R.id.btn_submit -> submitData()
        }
    }

    private fun logOut() {
        mAuth.signOut()
        val intent = Intent(this@InsertNoteActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun submitData() {
        if (!validateForm()) return

        val title = etTitle.text.toString()
        val desc = etDesc.text.toString()
        val note = Note(title, desc)

        val userId = mAuth.currentUser?.uid
        if (userId != null) {
            databaseReference.child("notes").child(userId).push().setValue(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    etTitle.text.clear()
                    etDesc.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        if (TextUtils.isEmpty(etTitle.text.toString())) {
            etTitle.error = "Judul wajib diisi"
            isValid = false
        }
        if (TextUtils.isEmpty(etDesc.text.toString())) {
            etDesc.error = "Deskripsi wajib diisi"
            isValid = false
        }
        return isValid
    }
}
