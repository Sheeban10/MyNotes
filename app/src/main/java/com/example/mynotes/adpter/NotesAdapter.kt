package com.example.mynotes.adpter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.models.Note

class NotesAdapter(private val context : Context, val listener: NotesitemclickListener ) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>()  {

        private val NotesList = ArrayList<Note>()
        private val fullList = ArrayList<Note>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.notes, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList : List<Note>){

        fullList.clear()
        fullList.addAll(newList)
        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search :String){

        NotesList.clear()

        for (item in fullList){

            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                    item.note?.toLowerCase()?.contains(search.lowercase()) == true){

                NotesList.add(item)
            }
        }

        notifyDataSetChanged()

        
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val currentNote = NotesList[position]
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note.text = currentNote.note

        holder.date.text = currentNote.date
        holder.date.isSelected = true

        holder.notes_layout.setOnClickListener{

            listener.onitemClicked(NotesList[holder.adapterPosition])
        }

        holder.notes_layout.setOnLongClickListener {
            listener.onlongitemClicked(NotesList[holder.adapterPosition], holder.notes_layout)
            true
        }
    }

    inner class NoteViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }
    interface NotesitemclickListener{
        fun onitemClicked(note:Note)
        fun onlongitemClicked(note: Note, cardView: CardView)

    }
}