package com.example.wikiart.ui.artists

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
import com.example.wikiart.model.ArtistCategory
import com.example.wikiart.model.ArtistSection

class ArtistOptionsBottomSheetFragment : BottomSheetDialogFragment() {

    interface Listener {
        fun onOptionsSelected(category: ArtistCategory, section: ArtistSection?, layout: ArtistAdapter.Layout)
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
        return inflater.inflate(R.layout.fragment_artist_options_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        val sectionSpinner: Spinner = view.findViewById(R.id.sectionSpinner)
        val layoutGroup: RadioGroup = view.findViewById(R.id.layoutGroup)
        val applyButton: Button = view.findViewById(R.id.applyButton)

        val categories = ArtistCategory.values()
        val titles = categories.map { getString(it.titleRes) }
        categorySpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, titles)

        val currentCat = ArtistCategory.valueOf(requireArguments().getString(ARG_CATEGORY) ?: ArtistCategory.POPULAR.name)
        categorySpinner.setSelection(categories.indexOf(currentCat))

        @Suppress("UNCHECKED_CAST")
        val sections = arguments?.getSerializable(ARG_SECTIONS) as? ArrayList<ArtistSection> ?: arrayListOf()
        val sectionTitles = arrayListOf(getString(R.string.section_all)) + sections.map { it.Title }
        sectionSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sectionTitles)

        val currentSection = arguments?.getSerializable(ARG_SECTION) as? ArtistSection
        val sectionIndex = sections.indexOfFirst { it.url == currentSection?.url }.let { if (it >= 0) it + 1 else 0 }
        sectionSpinner.setSelection(sectionIndex)

        val currentLayout = ArtistAdapter.Layout.valueOf(requireArguments().getString(ARG_LAYOUT) ?: ArtistAdapter.Layout.LIST.name)
        when (currentLayout) {
            ArtistAdapter.Layout.LIST -> layoutGroup.check(R.id.listButton)
            ArtistAdapter.Layout.GRID -> layoutGroup.check(R.id.gridButton)
        }

        applyButton.setOnClickListener {
            val selectedCategory = categories[categorySpinner.selectedItemPosition]
            val selectedLayout = when (layoutGroup.checkedRadioButtonId) {
                R.id.gridButton -> ArtistAdapter.Layout.GRID
                else -> ArtistAdapter.Layout.LIST
            }
            val selectedSection = if (sectionSpinner.selectedItemPosition == 0) null else sections[sectionSpinner.selectedItemPosition - 1]
            listener?.onOptionsSelected(selectedCategory, selectedSection, selectedLayout)
            dismiss()
        }
    }

    companion object {
        private const val ARG_CATEGORY = "category"
        private const val ARG_LAYOUT = "layout"
        private const val ARG_SECTION = "section"
        private const val ARG_SECTIONS = "sections"

        fun newInstance(
            category: ArtistCategory,
            layout: ArtistAdapter.Layout,
            section: ArtistSection?,
            sections: List<ArtistSection>
        ): ArtistOptionsBottomSheetFragment {
            val f = ArtistOptionsBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category.name)
            args.putString(ARG_LAYOUT, layout.name)
            args.putSerializable(ARG_SECTION, section)
            args.putSerializable(ARG_SECTIONS, ArrayList(sections))
            f.arguments = args
            return f
        }
    }
}
