package com.example.wikiart.ui.support

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.wikiart.databinding.FragmentSupportBinding
import com.example.wikiart.R

class SupportFragment : Fragment() {

    private var _binding: FragmentSupportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSendFeedback.setOnClickListener {
            val message = binding.inputMessage.text?.toString().orEmpty()
            val email = binding.inputEmail.text?.toString().orEmpty()
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("support@example.com"))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject))
                val extraText = message + if (email.isNotBlank()) "\n\n" + getString(R.string.support_email_from) + " " + email else ""
                putExtra(Intent.EXTRA_TEXT, extraText)
            }
            try {
                startActivity(Intent.createChooser(intent, getString(R.string.support_email_chooser)))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), getString(R.string.support_no_email_app), Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonDonate.setOnClickListener {
            val url = "https://www.buymeacoffee.com/wikiart"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
