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

async function checkJardinRoyalTimestamps() {
    try {
        console.log('🔍 Vérification des timestamps pour Jardin Royal...');
        
        const jardinRoyalDoc = await db.collection('pois').where('name', '==', 'Jardin Royal').get();
        
        if (jardinRoyalDoc.empty) {
            console.log('❌ Jardin Royal non trouvé');
            return;
        }
        
        const doc = jardinRoyalDoc.docs[0];
        const data = doc.data();
        
        console.log('📊 Données complètes du POI:');
        console.log(`  Nom: ${data.name}`);
        console.log(`  ID: ${doc.id}`);
        console.log(`  currentScore: ${data.currentScore}`);
        
        if (data.captureTime) {
            const captureTime = data.captureTime.toDate();
            console.log(`  captureTime: ${captureTime.toString()}`);
            console.log(`  captureTime (ISO): ${captureTime.toISOString()}`);
            console.log(`  captureTime (timestamp): ${captureTime.getTime()}`);
        } else {
            console.log(`  captureTime: NON`);
        }
        
        if (data.lastUpdated) {
            const lastUpdated = data.lastUpdated.toDate();
            console.log(`  lastUpdated: ${lastUpdated.toString()}`);
            console.log(`  lastUpdated (ISO): ${lastUpdated.toISOString()}`);
            console.log(`  lastUpdated (timestamp): ${lastUpdated.getTime()}`);
        } else {
            console.log(`  lastUpdated: NON`);
        }
        
        // Calculer l'âge
        const now = new Date();
        console.log(`  Heure actuelle: ${now.toString()}`);
        console.log(`  Heure actuelle (ISO): ${now.toISOString()}`);
        console.log(`  Heure actuelle (timestamp): ${now.getTime()}`);
        
        if (data.captureTime) {
            const captureTime = data.captureTime.toDate();
            const ageMs = now.getTime() - captureTime.getTime();
            const ageHours = ageMs / (1000 * 60 * 60);
            const ageDays = ageHours / 24;
            
            console.log(`\n⏰ Âge du captureTime:`);
            console.log(`  Millisecondes: ${ageMs}`);
            console.log(`  Minutes: ${ageMs / (1000 * 60)}`);
            console.log(`  Heures: ${ageHours.toFixed(2)}`);
            console.log(`  Jours: ${ageDays.toFixed(2)}`);
            
            // Calcul du score attendu
            const DECREMENT_RATE_HOURS = 1; // 1 point par heure
            const MIN_SCORE = 10;
            const decrementedAmount = Math.floor(ageHours / DECREMENT_RATE_HOURS);
            const expectedScore = Math.max(MIN_SCORE, data.currentScore - decrementedAmount);
            
            console.log(`\n🧮 Calcul du score attendu:`);
            console.log(`  Score base: ${data.currentScore}`);
            console.log(`  Âge (heures): ${ageHours.toFixed(2)}`);
            console.log(`  Décrément: ${decrementedAmount} points`);
            console.log(`  Score attendu: ${expectedScore}`);
            console.log(`  Score minimum: ${MIN_SCORE}`);
        }
        
    } catch (error) {
        console.error('❌ Erreur:', error);
    } finally {
        process.exit(0);
    }
}

checkJardinRoyalTimestamps();