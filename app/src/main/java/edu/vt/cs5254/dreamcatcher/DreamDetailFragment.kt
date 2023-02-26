package edu.vt.cs5254.dreamcatcher

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.vt.cs5254.dreamcatcher.databinding.FragmentDreamDetailBinding
import java.text.SimpleDateFormat

class DreamDetailFragment : Fragment() {

    private lateinit var buttonList: List<Button>
    private var _binding: FragmentDreamDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "FragmentDreamDetailBinding is null"
        }

    private val viewModel: DreamDetailViewModel by lazy {
        ViewModelProvider(this)[DreamDetailViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDreamDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonList = listOf(
            binding.entry0Button,
            binding.entry1Button,
            binding.entry2Button,
            binding.entry3Button,
            binding.entry4Button
        )



        Log.d("dreamEventListener", viewModel.dream.title)
        //set event listener here
        binding.apply {
            titleText.doOnTextChanged{ text, _, _, _ ->
                val entries = viewModel.dream.entries
                viewModel.dream = viewModel.dream.copy(title = text.toString())
                viewModel.dream.entries = entries
                updateView()
            }

            lastUpdatedText.apply {
                val currDate = viewModel.dream.lastUpdated
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss a").format(currDate)
                text = "Last Updated " + simpleDateFormat
            }

            fulfilledCheckbox.apply {
                setOnClickListener{
                    if (isChecked) {
                        if (!viewModel.dream.entries.any{dreamEntry -> dreamEntry.kind == DreamEntryKind.FULFILLED }) {
                            viewModel.dream.entries += DreamEntry(
                                kind = DreamEntryKind.FULFILLED,
                                dreamId = viewModel.dream.id
                            )
                        }
                    }
                    else {
                        viewModel.dream.entries = viewModel.dream.entries.filter { dreamEntry ->
                            dreamEntry.kind != DreamEntryKind.FULFILLED
                        }
                    }
                    updateView()
                }

            }

            deferredCheckbox.apply {
                setOnClickListener{
                    if(isChecked) {
                        if (!viewModel.dream.entries.any{dreamEntry -> dreamEntry.kind == DreamEntryKind.DEFERRED }) {
                            viewModel.dream.entries += DreamEntry(
                                kind = DreamEntryKind.DEFERRED,
                                dreamId = viewModel.dream.id
                            )
                        }
                    }
                    else {
                        viewModel.dream.entries = viewModel.dream.entries.filter { dreamEntry ->
                            dreamEntry.kind != DreamEntryKind.DEFERRED
                        }
                    }
                    updateView()
                }
            }
        }
        updateView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView() {
        if (binding.titleText.text.toString() != viewModel.dream.title) {
            binding.titleText.setText(viewModel.dream.title)
        }
        updateCheckbox()
        buttonList.forEach{ it.visibility = View.GONE }
        buttonList.zip(viewModel.dream.entries).forEach{(button, dreamEntry) ->
            button.displayEntry(dreamEntry)
        }
    }

    private fun updateCheckbox() {
        when {
            viewModel.dream.entries.any { it.kind == DreamEntryKind.DEFERRED } -> {
                binding.deferredCheckbox.isChecked = true
                binding.deferredCheckbox.isEnabled = true
                binding.fulfilledCheckbox.isChecked = false
                binding.fulfilledCheckbox.isEnabled = false
            }
            viewModel.dream.entries.any { it.kind == DreamEntryKind.FULFILLED } -> {
                binding.deferredCheckbox.isChecked = false
                binding.deferredCheckbox.isEnabled = false
                binding.fulfilledCheckbox.isChecked = true
                binding.fulfilledCheckbox.isEnabled = true
            }
            else -> {
                binding.deferredCheckbox.isChecked = false
                binding.deferredCheckbox.isEnabled = true
                binding.fulfilledCheckbox.isChecked = false
                binding.fulfilledCheckbox.isEnabled = true
            }
        }
    }

    private fun Button.displayEntry(entry: DreamEntry) {
        visibility = View.VISIBLE
        isEnabled = false

        when(entry.kind) {
            DreamEntryKind.CONCEIVED -> {
                text = entry.kind.toString()
                setBackgroundWithContrastingText("#FF03DAC5")
            }

            DreamEntryKind.DEFERRED -> {
                text = entry.kind.toString()
                setBackgroundWithContrastingText("#FFBB86FC")
            }

            DreamEntryKind.FULFILLED -> {
                text = entry.kind.toString()
                setBackgroundWithContrastingText("#9400D3")
            }

            DreamEntryKind.REFLECTION -> {
                isAllCaps = false
                text = entry.text
                setBackgroundWithContrastingText("#FF6200EE")
            }

        }
    }

}