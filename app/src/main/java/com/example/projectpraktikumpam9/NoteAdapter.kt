package com.example.projectpraktikumpam9

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class NoteAdapter(
    private val context: Context,
    private val noteList: List<Note>
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.item_title)
        val tvDesc: TextView = view.findViewById(R.id.item_desc)
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
        val btnDelete: ImageButton = view.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.tvTitle.text = note.title
        holder.tvDesc.text = note.description

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, UpdateNoteActivity::class.java)
            intent.putExtra("noteId", note.id)
            intent.putExtra("noteTitle", note.title)
            intent.putExtra("noteDesc", note.description)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val dbRef = FirebaseDatabase.getInstance("https://pam-modul-9-b2394-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("notes").child(userId ?: "")
            dbRef.child(note.id ?: "").removeValue()
            Toast.makeText(context, "Catatan dihapus", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = noteList.size
}
