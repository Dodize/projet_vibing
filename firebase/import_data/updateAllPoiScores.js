const admin = require('firebase-admin');

// Utiliser les mêmes identifiants que le script d'importation existant
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialize Firebase Admin SDK
if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount),
    });
}

const db = admin.firestore();

async function updateAllPoiScores() {
    try {
        console.log('🔄 Mise à jour de tous les scores de POIs...');
        
        const poisSnapshot = await db.collection('pois').get();
        
        if (poisSnapshot.empty) {
            console.log('❌ Aucun POI trouvé');
            return;
        }
        
        console.log(`📊 Traitement de ${poisSnapshot.size} POIs...`);
        
        const DECREMENT_RATE_MILLIS = 60 * 60 * 1000; // 1 heure
        const MIN_SCORE = 10;
        const currentTime = new Date().getTime();
        
        let updatedCount = 0;
        
        for (const doc of poisSnapshot.docs) {
            try {
                const data = doc.data();
                const poiId = doc.id;
                const poiName = data.name || 'Sans nom';
                
                // Récupérer le timestamp de référence
                let lastUpdatedTime;
                if (data.lastUpdated) {
                    lastUpdatedTime = data.lastUpdated.toDate().getTime();
                } else if (data.captureTime) {
                    lastUpdatedTime = data.captureTime.toDate().getTime();
                } else {
                    console.log(`⏭️  ${poiName}: Aucun timestamp, skipping`);
                    continue;
                }
                
                // Calculer le score dynamique
                const timeElapsed = currentTime - lastUpdatedTime;
                const hoursElapsed = timeElapsed / (60 * 60 * 1000);
                const decrementedAmount = Math.floor(timeElapsed / DECREMENT_RATE_MILLIS);
                const dynamicScore = Math.max(MIN_SCORE, data.currentScore - decrementedAmount);
                
                // Mettre à jour si le score a changé
                if (dynamicScore !== data.currentScore) {
                    await db.collection('pois').doc(poiId).update({
                        currentScore: dynamicScore,
                        lastUpdated: admin.firestore.Timestamp.now()
                    });
                    
                    console.log(`✅ ${poiName}: ${data.currentScore} → ${dynamicScore} (${hoursElapsed.toFixed(1)}h, -${decrementedAmount}pts)`);
                    updatedCount++;
                } else {
                    console.log(`⏸️  ${poiName}: ${data.currentScore} (pas de changement)`);
                }
                
                // Petite pause
                await new Promise(resolve => setTimeout(resolve, 50));
                
            } catch (error) {
                console.error(`❌ Erreur pour ${doc.id}:`, error.message);
            }
        }
        
        console.log('\n📋 Résumé:');
        console.log(`✅ POIs mis à jour: ${updatedCount}/${poisSnapshot.size}`);
        console.log('🎉 Mise à jour terminée!');
        
    } catch (error) {
        console.error('❌ Erreur générale:', error);
    } finally {
        process.exit(0);
    }
}

updateAllPoiScores();