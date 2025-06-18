package com.example.wikiart.ui.paintings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.wikiart.R
import com.example.wikiart.model.PaintingCategory

class OptionsBottomSheetFragment : BottomSheetDialogFragment() {

    interface Listener {
        fun onOptionsSelected(category: PaintingCategory, layout: PaintingAdapter.Layout)
    }

    private var listener: Listener? = null

    fun setListener(l: Listener) {
        listener = l
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_options_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        val layoutGroup: RadioGroup = view.findViewById(R.id.layoutGroup)
        val applyButton: Button = view.findViewById(R.id.applyButton)

        val categories = PaintingCategory.values()
        val titles = categories.map { getString(it.titleRes) }
        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, titles)

        val currentCat = PaintingCategory.valueOf(requireArguments().getString(ARG_CATEGORY) ?: PaintingCategory.POPULAR.name)
        categorySpinner.setSelection(categories.indexOf(currentCat))

        val currentLayout = PaintingAdapter.Layout.valueOf(requireArguments().getString(ARG_LAYOUT) ?: PaintingAdapter.Layout.LIST.name)
        when (currentLayout) {
            PaintingAdapter.Layout.LIST -> layoutGroup.check(R.id.listButton)
            PaintingAdapter.Layout.GRID -> layoutGroup.check(R.id.gridButton)
            PaintingAdapter.Layout.SHEET -> layoutGroup.check(R.id.sheetButton)
        }

        applyButton.setOnClickListener {
            val selectedCategory = categories[categorySpinner.selectedItemPosition]
            val selectedLayout = when (layoutGroup.checkedRadioButtonId) {
                R.id.gridButton -> PaintingAdapter.Layout.GRID
                R.id.sheetButton -> PaintingAdapter.Layout.SHEET
                else -> PaintingAdapter.Layout.LIST
            }
            listener?.onOptionsSelected(selectedCategory, selectedLayout)
            dismiss()
        }
    }

    companion object {
        private const val ARG_CATEGORY = "category"
        private const val ARG_LAYOUT = "layout"

        fun newInstance(category: PaintingCategory, layout: PaintingAdapter.Layout): OptionsBottomSheetFragment {
            val f = OptionsBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category.name)
            args.putString(ARG_LAYOUT, layout.name)
            f.arguments = args
            return f
        }
    }
}
