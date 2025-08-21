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
                putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
                putExtra(Intent.EXTRA_TEXT, message + if (email.isNotBlank()) "\n\nFrom: $email" else "")
            }
            try {
                startActivity(Intent.createChooser(intent, "Send Email"))
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireContext(), "No email app found", Toast.LENGTH_SHORT).show()
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
