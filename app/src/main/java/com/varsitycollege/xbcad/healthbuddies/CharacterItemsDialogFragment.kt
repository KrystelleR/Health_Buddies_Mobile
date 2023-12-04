package com.varsitycollege.xbcad.healthbuddies

// CharacterItemsDialogFragment.kt

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class CharacterItemsDialogFragment(private val uid: String, private val callback: settingspage) : DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialogContainer: LinearLayout // Declare dialogContainer at the class level

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            databaseReference = FirebaseDatabase.getInstance().reference.child("PurchasedItems").child(uid).child("pfp")

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_banner_items, null)

            // Initialize dialogContainer
            dialogContainer = view.findViewById(R.id.dialogContainer)

            // Wrap dialogContainer in a ScrollView
            val scrollView = ScrollView(requireContext())
            scrollView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            scrollView.addView(dialogContainer)

            // Set the ScrollView as the content view of the dialog
            builder.setView(scrollView)
                .setTitle("Character Items")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()

            // Call fetchCharacterItems to start loading and displaying images
            fetchCharacterItems()

            dialog // Return the created dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun fetchCharacterItems() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    val imageUrl = itemSnapshot.child("ImageUrl").value.toString()
                    addImageViewToDialog(imageUrl)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }

    private fun onImageClicked(imageUrl: String) {
        callback.onCharacterImageClick(imageUrl)

    }

    private fun addImageViewToDialog(imageUrl: String) {
        val imageView = ImageView(requireContext())
        imageView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.image_height)
        )

        Glide.with(requireContext())
            .load(imageUrl)
            .into(imageView)

        imageView.setOnClickListener {
            // When the image is clicked, invoke the callback
            onImageClicked(imageUrl)
            // Close the dialog
            (dialog as? AlertDialog)?.dismiss()
        }

        dialogContainer.addView(imageView)
    }
}

interface OnCharacterImageClickListener {
    fun onCharacterImageClick(imageUrl: String)
}
