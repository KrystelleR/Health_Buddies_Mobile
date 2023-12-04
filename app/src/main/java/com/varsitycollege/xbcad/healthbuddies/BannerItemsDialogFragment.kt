package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.database.*

class BannerItemsDialogFragment(private val uid: String, private val callback: MainActivity) : DialogFragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var dialogContainer: LinearLayout // Declare dialogContainer at the class level

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            databaseReference = FirebaseDatabase.getInstance().reference.child("PurchasedItems").child(uid).child("banner")

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_banner_items, null)

            // Initialize dialogContainer
            dialogContainer = view.findViewById(R.id.dialogContainer)

            // Wrap dialogContainer in a ScrollView
            val scrollView = ScrollView(requireContext())
            scrollView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT // Adjusted to wrap_content
            )
            scrollView.addView(dialogContainer)

            // Set the ScrollView as the content view of the dialog
            builder.setView(scrollView)
                .setTitle("Banner Items")
                .setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = builder.create()

            // Call fetchBannerItems to start loading and displaying images
            fetchBannerItems()

            dialog // Return the created dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    private fun fetchBannerItems() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (itemSnapshot in snapshot.children) {
                    // Check if the child has the "ImageUrl" attribute
                    if (itemSnapshot.hasChild("ImageUrl")) {
                        val imageUrl = itemSnapshot.child("ImageUrl").value.toString()
                        addImageViewToDialog(imageUrl)

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors
            }
        })
    }


    private fun onImageClicked(imageResId: String) {
        callback.onImageClick(imageResId)
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
            callback.onImageClick(imageUrl)
            // Close the dialog
            (dialog as? AlertDialog)?.dismiss()
        }

        dialogContainer.addView(imageView)
    }
}
interface OnImageClickListener {
    fun onImageClick(imageUrl: String)
}

