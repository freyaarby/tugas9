package com.example.projectpraktikumpam9

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class NoteListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var noteList: ArrayList<Note>
    private lateinit var adapter: NoteAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)

        recyclerView = findViewById(R.id.recycler_notes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        noteList = ArrayList()
        adapter = NoteAdapter(this, noteList)
        recyclerView.adapter = adapter

        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        databaseRef = FirebaseDatabase.getInstance().getReference("notes").child(userId)

        fetchNotes()
    }

    private fun fetchNotes() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                noteList.clear()
                for (noteSnap in snapshot.children) {
                    val note = noteSnap.getValue(Note::class.java)
                    note?.id = noteSnap.key
                    note?.let { noteList.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
