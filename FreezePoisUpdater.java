import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FreezePoisUpdater {
    
    public static void addFreezeUntilToAllPois() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Date future : 24 heures à partir de maintenant
        Date tomorrow = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("freezeUntil", tomorrow);
        
        db.collection("pois")
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                for (var doc : queryDocumentSnapshots) {
                    // Mettre à jour chaque POI avec la date de gel
                    db.collection("pois").document(doc.getId())
                        .update(updates);
                }
                System.out.println("Champ freezeUntil ajouté à tous les POIs");
            })
            .addOnFailureListener(e -> {
                System.err.println("Erreur: " + e.getMessage());
            });
    }
    
    public static void addFreezeUntilToSpecificPoi(String poiId, Date freezeDate) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("freezeUntil", freezeDate);
        
        db.collection("pois").document(poiId)
            .update(updates)
            .addOnSuccessListener(aVoid -> {
                System.out.println("Champ freezeUntil ajouté au POI: " + poiId);
            })
            .addOnFailureListener(e -> {
                System.err.println("Erreur: " + e.getMessage());
            });
    }
}