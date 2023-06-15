package com.example.mynotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.databinding.AddNoteBinding

class AddNote: AppCompatActivity() {

    private lateinit var binding: AddNoteBinding
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        binding = AddNoteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}