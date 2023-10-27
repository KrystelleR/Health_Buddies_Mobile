package com.varsitycollege.xbcad.healthbuddies
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import android.widget.Button

class ConfDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.conf_dialog, container, false)

        val confirmButton = view.findViewById<Button>(R.id.confirmButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelButton)

        confirmButton.setOnClickListener {
            //what happens when they click confirm bttn
        }

        cancelButton.setOnClickListener {
            // stuff when they click cancl bttn
            dismiss()
        }

        return view
    }
}
