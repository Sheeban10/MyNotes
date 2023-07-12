package com.example.mynotes

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.mynotes.LoginActivity.Companion.EXTRA_NAME
import com.example.mynotes.adpter.NotesAdapter
import com.example.mynotes.database.NoteDatabase
import com.example.mynotes.databinding.ActivityMainBinding
import com.example.mynotes.models.Note
import com.example.mynotes.models.NotesViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NotesAdapter.NotesitemclickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var profileImageView: ImageView
    private lateinit var database: NoteDatabase
    private lateinit var adapter : NotesAdapter
    private lateinit var selectedNote : Note
    private lateinit var viewModel : NotesViewModel

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode == Activity.RESULT_OK){

            val note = result.data?.getSerializableExtra("note") as? Note
            if (note != null){
                viewModel.updateNote(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        profileImageView = binding.accountPhoto
        auth = FirebaseAuth.getInstance()
        val photoUrl = intent.getStringExtra("photoUrl")
        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .transform(CircleCrop())
                .override(100, 100)
                .into(profileImageView)
        }

        binding.username.text = intent.getStringExtra(EXTRA_NAME)
        binding.logout.setOnClickListener{

            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        initUI() // Initilizing view

        viewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)

        viewModel.allnotes.observe(this) { list ->
            list?.let {

                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getDatabase(this)
    }




    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter =  NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->

            if (result.resultCode == Activity.RESULT_OK){

                val note = result.data?.getSerializableExtra("note") as? Note

                if (note != null){
                    viewModel.insertNote(note)
                }
            }
        }
        binding.fbAddNote.setOnClickListener{

            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchNotes.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText != null){

                    adapter.filterList(newText)
                }
                return true
            }

        })
    }

    override fun onitemClicked(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onlongitemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {

        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()

    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if ( item?.itemId == R.id.delete_note){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}