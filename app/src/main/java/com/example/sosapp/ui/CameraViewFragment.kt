package com.example.sosapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.sosapp.databinding.FragmentCameraViewBinding
import com.example.sosapp.model.AlertRequest
import com.example.sosapp.util.hide
import com.example.sosapp.util.show
import com.example.sosapp.util.showDialog
import com.example.sosapp.viewmodel.AlertUiState
import com.example.sosapp.viewmodel.CameraViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CameraViewFragment : Fragment() {

    private var _binding: FragmentCameraViewBinding? = null
    private val binding get() = _binding!!
    private val cameraViewModel: CameraViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var capturedImageBase64: String? = null
    companion object{
        var REQUEST_LOCATION_PERMISSION=1001
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                capturePictureWithLocation()
            } else {
                showDialog("Location permission denied")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraViewBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCamera()
        observeData()
    }
    @SuppressLint("SetTextI18n")
    private fun observeData() = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                cameraViewModel.state.observe(viewLifecycleOwner) { uiState ->
                    when (uiState) {
                        is AlertUiState.LOADING -> {
                            if (uiState.loading) {
                                progressBar.show()
                            } else {
                                progressBar.hide()
                            }
                        }
                        is AlertUiState.ERROR -> {
                            progressBar.hide()
                            uiState.errorMessage?.let {
                                showDialog(message = it, onPositiveAction = {
                                    camera.open()
                                })
                            }
                        }
                        is AlertUiState.SUCCESS -> {
                            progressBar.hide()
                            showDialog(message = "We are Sending Help Now",
                                titleType = "Success",
                                onPositiveAction = { requireActivity().finish() })
                        }
                        else -> Unit
                    }
                }
            }
        }
    }



    private fun setupCamera() {
        binding.apply {
            camera.setLifecycleOwner(viewLifecycleOwner)
            camera.addCameraListener(object : CameraListener() {
                override fun onPictureTaken(result: PictureResult) {
                    capturedImageBase64 = convertToBase64(result)
                    getLastKnownLocation { location ->
                        capturedImageBase64?.let {
                            onPictureTaken(location, it)
                        }
                    }
                    camera.close()
                }
            })

            captureButton.setOnClickListener {
                capturePictureWithLocation()
            }
        }
    }

    private fun capturePictureWithLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Capture picture first, and location will be retrieved afterwards in the listener
            if (binding.camera.isTakingPicture) return
            if (!binding.camera.isOpened) {
                cameraViewModel.safeOpenCamera { success ->
                    if (success) {
                        binding.camera.takePicture()
                    } else {
                    }
                }
            } else {
                binding.camera.takePicture()
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun convertToBase64(pictureResult: PictureResult): String {
        val imageData = pictureResult.data
        return Base64.encodeToString(imageData, Base64.DEFAULT)
    }

    private fun getLastKnownLocation(onLocationRetrieved: (Location) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    onLocationRetrieved(location)
                } else {
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { currentLocation: Location? ->
                            if (currentLocation != null) {
                                onLocationRetrieved(currentLocation)
                            } else {
                                showDialog("Unable to retrieve location")
                            }
                        }
                        .addOnFailureListener { exception ->
                            showDialog(message = "Failed to get location: ${exception.message}")
                        }
                }
            }
            .addOnFailureListener { exception ->
                showDialog("Failed to get location: ${exception.message}")
            }
    }
    private fun onPictureTaken(location: Location, imageBase64: String) {
        cameraViewModel.alertPolice(
            AlertRequest(
                image = imageBase64,
                location = com.example.sosapp.model.Location(
                    latitude = location.latitude.toString(),
                    longitude = location.longitude.toString()
                ),
                phoneNumbers = listOf("080333333333", "080444444444")
            )
        )
    }
    override fun onStart() {
        super.onStart()
        cameraViewModel.safeOpenCamera {
            if (!it) Log.e("com.example.sosapp.ui.CameraViewFragment", "Error opening camera on start")
        }
    }

    override fun onStop() {
        super.onStop()
        cameraViewModel.safeCloseCamera {
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.camera.destroy()
        _binding = null
    }

}

