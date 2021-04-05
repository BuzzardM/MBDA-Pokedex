package com.deadlinehunters.pokedex.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.deadlinehunters.pokedex.R

class EditPokemonNameFragment : DialogFragment(), TextView.OnEditorActionListener {

    private lateinit var editText: EditText

    interface EditPokemonNameDialogListener {
        fun onFinishEditDialog(inputText: String?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setTitle("Enter Pokemon name")
        editText = view.findViewById<EditText>(R.id.pokemon_edit_name_edittext)
        editText.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)
        editText.setOnEditorActionListener(this)
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            val act = activity as EditPokemonNameDialogListener
            act.onFinishEditDialog(editText.text.toString())
            this.dismiss()
            return true
        }
        return false
    }

}