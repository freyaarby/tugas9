package com.example.projectpraktikumpam9

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etDesc: EditText
    private lateinit var btnUpdate: Button
    private lateinit var noteId: String
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        etTitle = findViewById(R.id.et_update_title)
        etDesc = findViewById(R.id.et_update_desc)
        btnUpdate = findViewById(R.id.btn_update_note)

        noteId = intent.getStringExtra("noteId") ?: ""
        etTitle.setText(intent.getStringExtra("noteTitle"))
        etDesc.setText(intent.getStringExtra("noteDesc"))

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        btnUpdate.setOnClickListener {
            val updatedNote = mapOf(
                "title" to etTitle.text.toString(),
                "description" to etDesc.text.toString()
            )

            FirebaseDatabase.getInstance().getReference("notes")
                .child(userId).child(noteId).updateChildren(updatedNote)
                .addOnSuccessListener {
                    Toast.makeText(this, "Catatan diperbarui", Toast.LENGTH_SHORT).show()
                    finish()
                }
        }
    }
}
