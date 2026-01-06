package com.example.vibing.ui.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.vibing.R;
import com.example.vibing.services.ImageRecognitionService;

import java.util.List;

import java.io.IOException;

public class CameraCaptureFragment extends Fragment {
    private static final String TAG = "CameraCaptureFragment";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    
    private ImageView capturedImageView;
    private Button capturePhotoButton;
    private Button recognizeButton;
    private TextView resultTextView;
    private ImageRecognitionService imageRecognitionService;
    
    private Bitmap capturedBitmap;
    private String poiName;
    private String poiId;
    
    public static CameraCaptureFragment newInstance(String poiName, String poiId) {
        CameraCaptureFragment fragment = new CameraCaptureFragment();
        Bundle args = new Bundle();
        args.putString("poiName", poiName);
        args.putString("poiId", poiId);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (getArguments() != null) {
            poiName = getArguments().getString("poiName");
            poiId = getArguments().getString("poiId");
        }
        
        imageRecognitionService = new ImageRecognitionService(requireContext());
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera_capture, container, false);
        
        capturedImageView = view.findViewById(R.id.capturedImageView);
        capturePhotoButton = view.findViewById(R.id.capturePhotoButton);
        recognizeButton = view.findViewById(R.id.recognizeButton);
        resultTextView = view.findViewById(R.id.resultTextView);
        
        // Gallery button removed from layout - only camera capture allowed
        
        // Set title
        if (poiName != null) {
            TextView titleTextView = view.findViewById(R.id.titleTextView);
            titleTextView.setText("Prendre une photo de : " + poiName);
        }
        
        // Set click listeners
        capturePhotoButton.setOnClickListener(v -> checkCameraPermissionAndCapture());
        recognizeButton.setOnClickListener(v -> recognizeImage());
        
        // Initially hide recognize button and result
        recognizeButton.setVisibility(View.GONE);
        resultTextView.setVisibility(View.GONE);
        
        return view;
    }
    
    private void checkCameraPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) 
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), 
                    new String[]{Manifest.permission.CAMERA}, 
                    REQUEST_CAMERA_PERMISSION);
        } else {
            openCamera();
        }
    }
    
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    
    // Gallery selection removed - only camera capture allowed
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == getActivity().RESULT_OK && data != null) {
            try {
                if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    Bundle extras = data.getExtras();
                    capturedBitmap = (Bitmap) extras.get("data");
                    displayCapturedImage();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error loading image", e);
                Toast.makeText(getContext(), "Erreur lors du chargement de l'image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void displayCapturedImage() {
        if (capturedBitmap != null) {
            capturedImageView.setImageBitmap(capturedBitmap);
            capturedImageView.setVisibility(View.VISIBLE);
            recognizeButton.setVisibility(View.VISIBLE);
            resultTextView.setVisibility(View.GONE);
        }
    }
    
    private void recognizeImage() {
        if (capturedBitmap == null) {
            Toast.makeText(getContext(), "Veuillez d'abord prendre ou sélectionner une photo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        resultTextView.setText("Analyse de l'image en cours...");
        resultTextView.setVisibility(View.VISIBLE);
        recognizeButton.setEnabled(false);
        
        imageRecognitionService.recognizeImage(capturedBitmap, poiName, 
                new ImageRecognitionService.RecognitionCallback() {
                    @Override
                    public void onSuccess(List<String> labels, boolean isCorrectLocation) {
                        requireActivity().runOnUiThread(() -> {
                            recognizeButton.setEnabled(true);
                            
                            if (isCorrectLocation) {
                                resultTextView.setText("✓ Zone reconnue avec succès!\n" +
                                        "Éléments détectés : " + String.join(", ", labels));
                                resultTextView.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
                                
                                // Navigate to quiz after successful recognition
                                navigateToQuiz();
                            } else {
                                resultTextView.setText("✗ Zone non reconnue.\n" +
                                        "Éléments détectés : " + String.join(", ", labels) + 
                                        "\nVeuillez réessayer avec une photo de : " + poiName);
                                resultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
                            }
                        });
                    }
                    
                    @Override
                    public void onError(String error) {
                        requireActivity().runOnUiThread(() -> {
                            recognizeButton.setEnabled(true);
                            resultTextView.setText("Erreur lors de la reconnaissance : " + error);
                            resultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
                        });
                    }
                });
    }
    
    private void navigateToQuiz() {
        // Navigate back to PoiScoreFragment and trigger quiz
        androidx.navigation.NavController navController = androidx.navigation.Navigation.findNavController(requireView());
        navController.navigateUp(); // Go back to PoiScoreFragment
        
        // Show success message and trigger quiz after a short delay
        new android.os.Handler().postDelayed(() -> {
            if (getActivity() != null) {
                Toast.makeText(getActivity(), "Zone reconnue! Lancement du quiz...", Toast.LENGTH_SHORT).show();
                // The parent fragment will handle showing the quiz
            }
        }, 1000);
    }
    
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Permission de caméra refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (imageRecognitionService != null) {
            imageRecognitionService.close();
        }
    }
}