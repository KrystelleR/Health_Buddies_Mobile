package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class MyDialogFragment : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        auth = FirebaseAuth.getInstance()

        // Inflate the dialog layout
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.activity_my_dialog_fragment, null)

        builder.setView(view)
        builder.setTitle("Reset Password")

        // Set up the OK button
        builder.setPositiveButton("OK") { dialog, which ->
            // Check if the fragment is attached to an activity
            val context = context
            if (context != null) {
                val emailEditText = view.findViewById<TextInputEditText>(R.id.emailtxt)
                val userEmail = emailEditText.text.toString()

                auth.sendPasswordResetEmail(userEmail).addOnSuccessListener {
                    Toast.makeText(context, "An email with a link to reset your password was sent to $userEmail", Toast.LENGTH_SHORT).show()
                }
                    .addOnFailureListener{
                        Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
                    }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // Handle Cancel button click here (if needed)
        }

        return builder.create()
    }
}