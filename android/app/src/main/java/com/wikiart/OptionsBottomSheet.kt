package com.wikiart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Spinner
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wikiart.model.LayoutType

class OptionsBottomSheet<T : CategoryItem>(
    private val categories: Array<T>? = null,
    private var selectedCategory: T? = null,
    private var layoutType: LayoutType,
    private val callback: (T?, LayoutType) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_options, container, false)
        val spinner = view.findViewById<Spinner>(R.id.categorySpinner)
        if (categories.isNullOrEmpty()) {
            spinner.visibility = View.GONE
        } else {
            val adapter = CategorySpinnerAdapter(requireContext(), categories)
            spinner.adapter = adapter
            spinner.setSelection(categories.indexOf(selectedCategory))
        }

        val group = view.findViewById<RadioGroup>(R.id.layoutGroup)
        when (layoutType) {
            LayoutType.LIST -> group.check(R.id.layoutList)
            LayoutType.SHEET -> group.check(R.id.layoutSheet)
            else -> group.check(R.id.layoutGrid)
        }

        view.findViewById<Button>(R.id.applyButton).setOnClickListener {
            val category = if (categories.isNullOrEmpty()) null else categories[spinner.selectedItemPosition]
            val layout = when (group.checkedRadioButtonId) {
                R.id.layoutList -> LayoutType.LIST
                R.id.layoutSheet -> LayoutType.SHEET
                else -> LayoutType.COLUMN
            }
            callback(category, layout)
            dismiss()
        }
        return view
    }
}
