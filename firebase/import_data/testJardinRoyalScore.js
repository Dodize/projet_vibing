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

async function testJardinRoyalScore() {
    try {
        console.log('🔍 Test du calcul de score pour Jardin Royal...');
        
        // Récupérer le POI Jardin Royal
        const jardinRoyalDoc = await db.collection('pois').where('name', '==', 'Jardin Royal').get();
        
        if (jardinRoyalDoc.empty) {
            console.log('❌ Jardin Royal non trouvé');
            return;
        }
        
        const doc = jardinRoyalDoc.docs[0];
        const data = doc.data();
        
        console.log('📊 Données du POI:');
        console.log(`  Nom: ${data.name}`);
        console.log(`  ID: ${doc.id}`);
        console.log(`  Score actuel: ${data.currentScore}`);
        console.log(`  captureTime: ${data.captureTime ? data.captureTime.toDate() : 'NON'}`);
        console.log(`  lastUpdated: ${data.lastUpdated ? data.lastUpdated.toDate() : 'NON'}`);
        
        // Calculer le score dynamique
        const DECREMENT_RATE_MILLIS = 60 * 60 * 1000; // 1 heure
        const MIN_SCORE = 10;
        
        let lastUpdatedTime;
        if (data.lastUpdated) {
            lastUpdatedTime = data.lastUpdated.toDate().getTime();
        } else if (data.captureTime) {
            lastUpdatedTime = data.captureTime.toDate().getTime();
        } else {
            console.log('❌ Aucun timestamp trouvé');
            return;
        }
        
        const currentTime = new Date().getTime();
        const timeElapsed = currentTime - lastUpdatedTime;
        const hoursElapsed = timeElapsed / (60 * 60 * 1000);
        const decrementedAmount = Math.floor(timeElapsed / DECREMENT_RATE_MILLIS);
        const dynamicScore = Math.max(MIN_SCORE, data.currentScore - decrementedAmount);
        
        console.log('\n🧮 Calcul du score dynamique:');
        console.log(`  Heure actuelle: ${new Date(currentTime)}`);
        console.log(`  Timestamp de référence: ${new Date(lastUpdatedTime)}`);
        console.log(`  Temps écoulé: ${timeElapsed}ms (${hoursElapsed.toFixed(2)} heures)`);
        console.log(`  Taux de décrément: 1 point par ${DECREMENT_RATE_MILLIS}ms`);
        console.log(`  Montant décrémenté: ${decrementedAmount} points`);
        console.log(`  Score original: ${data.currentScore}`);
        console.log(`  Score dynamique: ${dynamicScore}`);
        console.log(`  Score minimum: ${MIN_SCORE}`);
        
        console.log('\n📋 Résultat:');
        if (dynamicScore !== data.currentScore) {
            console.log(`⚠️  Le score devrait être ${dynamicScore} au lieu de ${data.currentScore}`);
            console.log(`   Différence: ${data.currentScore - dynamicScore} points`);
        } else {
            console.log(`✅ Le score est correct: ${dynamicScore}`);
        }
        
    } catch (error) {
        console.error('❌ Erreur:', error);
    } finally {
        process.exit(0);
    }
}

testJardinRoyalScore();