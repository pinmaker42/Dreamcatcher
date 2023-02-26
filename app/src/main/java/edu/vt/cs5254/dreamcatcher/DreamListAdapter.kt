package edu.vt.cs5254.dreamcatcher

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import edu.vt.cs5254.dreamcatcher.databinding.FragmentDreamListBinding
import edu.vt.cs5254.dreamcatcher.databinding.ListItemDreamBinding

class DreamHolder(
    private val binding: ListItemDreamBinding
) : RecyclerView.ViewHolder(binding.root){
    fun bind(dream: Dream) {
        binding.listItemTitle.text = dream.title

        val count = dream.entries.filter { it.kind == DreamEntryKind.REFLECTION }
            .count()

        binding.listItemReflectionCount.text = binding.root.context.getString(R.string.dream_reflection_count, count)

        if (dream.isFulfilled) {
            binding.listItemImage.setImageResource(R.drawable.dream_fulfilled_icon)
        }
        else {
            binding.listItemImage.setImageResource(R.drawable.dream_deferred_icon)
        }

        binding.listItemImage.visibility = if (dream.isFulfilled || dream.isDeferred) {
            View.VISIBLE
        }
        else {
            View.GONE
        }
    }
}

class DreamListAdapter(
    private val dreams: List<Dream>
) : RecyclerView.Adapter<DreamHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : DreamHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemDreamBinding.inflate(inflater, parent, false)
        return DreamHolder(binding)
    }
    override fun onBindViewHolder(holder: DreamHolder, position: Int) {
        val dream = dreams[position]
        holder.bind(dream)
    }
    override fun getItemCount() = dreams.size
}