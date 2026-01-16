package com.example.vibing.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vibing.R;
import com.example.vibing.models.Poi;
import com.example.vibing.utils.PoiMigrationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Interface d'administration pour gérer les mots-clés des POI
 */
public class PoiAdminActivity extends AppCompatActivity {
    private static final String TAG = "PoiAdminActivity";
    
    private FirebaseFirestore db;
    private ListView poiListView;
    private Button migrateAllButton;
    private ArrayList<Poi> poiList;
    private PoiAdapter poiAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_admin);
        
        // Debug message
        Toast.makeText(this, "Admin activity started!", Toast.LENGTH_LONG).show();
        Log.d(TAG, "PoiAdminActivity onCreate started");
        
        db = FirebaseFirestore.getInstance();
        
        initViews();
        loadPois();
    }
    
    private void initViews() {
        Log.d(TAG, "Initializing views");
        poiListView = findViewById(R.id.poi_list_view);
        migrateAllButton = findViewById(R.id.migrate_all_button);
        
        Log.d(TAG, "poiListView: " + (poiListView != null ? "found" : "null"));
        Log.d(TAG, "migrateAllButton: " + (migrateAllButton != null ? "found" : "null"));
        
        poiList = new ArrayList<>();
        poiAdapter = new PoiAdapter(poiList);
        poiListView.setAdapter(poiAdapter);
        
        migrateAllButton.setOnClickListener(v -> {
            Log.d(TAG, "Migrate button clicked");
            showMigrateAllDialog();
        });
    }
    
    private void loadPois() {
        db.collection("pois")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    poiList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Poi poi = doc.toObject(Poi.class);
                        poi.setId(doc.getId());
                        poiList.add(poi);
                    }
                    poiAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors du chargement des POI", e);
                    Toast.makeText(this, "Erreur lors du chargement des POI", Toast.LENGTH_SHORT).show();
                });
    }
    
    private void showMigrateAllDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Migrer tous les POI")
                .setMessage("Cette action va ajouter des mots-clés par défaut à tous les POI qui n'en ont pas. Continuer ?")
                .setPositiveButton("Oui", (dialog, which) -> {
                    PoiMigrationUtils.migrateAllPoisWithKeywords();
                    Toast.makeText(this, "Migration lancée...", Toast.LENGTH_SHORT).show();
                    // Recharger après un délai
                    poiListView.postDelayed(this::loadPois, 2000);
                })
                .setNegativeButton("Non", null)
                .show();
    }
    
    private void showEditKeywordsDialog(Poi poi) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_keywords, null);
        
        TextView poiNameText = dialogView.findViewById(R.id.poi_name_text);
        EditText keywordsEdit = dialogView.findViewById(R.id.keywords_edit);
        
        poiNameText.setText(poi.getName());
        
        // Afficher les mots-clés actuels
        List<String> currentKeywords = poi.getKeywords();
        if (currentKeywords != null && !currentKeywords.isEmpty()) {
            keywordsEdit.setText(String.join(", ", currentKeywords));
        }
        
        new AlertDialog.Builder(this)
                .setTitle("Modifier les mots-clés")
                .setView(dialogView)
                .setPositiveButton("Sauvegarder", (dialog, which) -> {
                    String keywordsText = keywordsEdit.getText().toString().trim();
                    List<String> newKeywords = parseKeywords(keywordsText);
                    
                    updatePoiKeywords(poi.getId(), newKeywords);
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
    
    private List<String> parseKeywords(String keywordsText) {
        List<String> keywords = new ArrayList<>();
        if (keywordsText != null && !keywordsText.isEmpty()) {
            String[] parts = keywordsText.split(",");
            for (String part : parts) {
                String keyword = part.trim().toLowerCase();
                if (!keyword.isEmpty()) {
                    keywords.add(keyword);
                }
            }
        }
        return keywords;
    }
    
    private void updatePoiKeywords(String poiId, List<String> keywords) {
        db.collection("pois").document(poiId)
                .update("keywords", keywords)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Mots-clés mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    loadPois();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Erreur lors de la mise à jour des mots-clés", e);
                    Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                });
    }
    
    private class PoiAdapter extends ArrayAdapter<Poi> {
        public PoiAdapter(ArrayList<Poi> pois) {
            super(PoiAdminActivity.this, R.layout.item_poi_admin, pois);
        }
        
        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_poi_admin, parent, false);
            }
            
            Poi poi = getItem(position);
            if (poi != null) {
                TextView nameText = convertView.findViewById(R.id.poi_name);
                TextView keywordsText = convertView.findViewById(R.id.poi_keywords);
                Button editButton = convertView.findViewById(R.id.edit_keywords_button);
                
                nameText.setText(poi.getName());
                
                List<String> keywords = poi.getKeywords();
                if (keywords != null && !keywords.isEmpty()) {
                    keywordsText.setText(String.join(", ", keywords));
                } else {
                    keywordsText.setText("Aucun mot-clé");
                    keywordsText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                }
                
                editButton.setOnClickListener(v -> showEditKeywordsDialog(poi));
            }
            
            return convertView;
        }
    }
}