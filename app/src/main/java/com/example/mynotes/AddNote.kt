package com.example.mynotes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.databinding.AddNoteBinding
import com.example.mynotes.models.Note
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date


class AddNote: AppCompatActivity() {

    private lateinit var note: Note
    private lateinit var old_note : Note
    var isUpdate = false

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
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK, intent)
                finish()

            }
            else{

                Toast.makeText(this@AddNote, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

        }

        binding.btnBack.setOnClickListener{

            onBackPressed()
        }
    }
}