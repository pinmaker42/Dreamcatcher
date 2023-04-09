package edu.vt.cs5254.dreamcatcher

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import edu.vt.cs5254.dreamcatcher.databinding.FragmentReflectionDialogBinding

class ReflectionDialogFragment : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = FragmentReflectionDialogBinding.inflate(layoutInflater)

        //If positive - add, if negative delete.  check the results to send back and set / bind it
        val positiveListener = DialogInterface.OnClickListener { _, _  ->
            val resultText = binding.reflectionText.text.toString()
            setFragmentResult(
                REQUEST_KEY,
                bundleOf(BUNDLE_KEY to resultText)
            )
        }

        //adding a listener for changes
        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle(R.string.reflection_dialog_title)
            .setPositiveButton(R.string.reflection_dialog_positive, positiveListener)
            .setNegativeButton(R.string.reflection_dialog_negative, null)
            .show()
    }

    companion object {
        const val REQUEST_KEY = "REQUEST_KEY"
        const val BUNDLE_KEY = "BUNDLE_KEY"
    }
}