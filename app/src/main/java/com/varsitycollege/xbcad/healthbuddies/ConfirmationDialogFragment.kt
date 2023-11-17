import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.varsitycollege.xbcad.healthbuddies.R

class ConfirmationDialogFragment : DialogFragment() {

    interface ConfirmationDialogListener {
        fun onConfirmClicked()
        fun onCancelClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.conf_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.confirmButton).setOnClickListener {
            (activity as? ConfirmationDialogListener)?.onConfirmClicked()
            dismiss()
        }

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            (activity as? ConfirmationDialogListener)?.onCancelClicked()
            dismiss()
        }
    }
}
