package com.example.sosapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sosapp.R
import com.example.sosapp.databinding.FragmentTriggerAnAlertBinding
import com.example.sosapp.util.launchFragment

class TriggerAnAlertFragment : Fragment() {

    private var _binding: FragmentTriggerAnAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriggerAnAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnClickListener {
            launchFragment(R.id.go_to_cameraViewFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.lottieAnimationView.cancelAnimation()
        _binding = null
    }
}
