package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.renderscript.Element
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.ColumnInfo
import com.example.mynotes.database.NoteDao
import com.example.mynotes.database.NoteDatabase
import com.example.mynotes.databinding.AddNoteBinding
import com.example.mynotes.models.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.log


class AddNote: AppCompatActivity() {

    private lateinit var note: Note
    private lateinit var old_note: Note
    var isUpdate = false
    val database = FirebaseDatabase.getInstance().reference


    private lateinit var binding: AddNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)




        try {

            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitleAddnotes.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate = true

        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitleAddnotes.text.toString()
            val note_desc = binding.etNote.text.toString()


            if (title.isNotEmpty() || note_desc.toString().isNotEmpty()) {

                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:MM a")

                note = if (isUpdate) {
                    Note(old_note.id, title, note_desc, formatter.format(Date())
                    )
                } else {

                    Note(null, title, note_desc, formatter.format(Date())
                    )
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {

                Toast.makeText(this@AddNote, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

        }

        binding.btnBack.setOnClickListener {
            val title = binding.etTitleAddnotes.text.toString()
            val note_desc = binding.etNote.text.toString()
            if (title.isNotEmpty() || note_desc.toString().isNotEmpty()) {

                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:MM a")

                if (isUpdate) {
                    note = Note(
                        old_note.id, title, note_desc, formatter.format(Date())
                    )
                } else {

                    note = Note(
                        null, title, note_desc, formatter.format(Date())
                    )
                }
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {

                Toast.makeText(this@AddNote, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            onBackPressed()
        }

    }
}