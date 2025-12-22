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

async function checkJardinRoyalHistory() {
    try {
        console.log('🔍 Vérification de l\'historique de Jardin Royal...');
        
        // Récupérer tous les documents qui pourraient correspondre à Jardin Royal
        const allPois = await db.collection('pois').get();
        
        console.log('📊 Recherche de Jardin Royal parmi ' + allPois.size + ' POIs...');
        
        let jardinRoyalDocs = [];
        
        allPois.forEach(doc => {
            const data = doc.data();
            if (data.name && data.name.toLowerCase().includes('jardin') && data.name.toLowerCase().includes('royal')) {
                jardinRoyalDocs.push({
                    id: doc.id,
                    name: data.name,
                    currentScore: data.currentScore,
                    captureTime: data.captureTime,
                    lastUpdated: data.lastUpdated
                });
            }
        });
        
        console.log('🎯 Trouvé ' + jardinRoyalDocs.length + ' documents Jardin Royal:');
        
        jardinRoyalDocs.forEach((doc, index) => {
            console.log('\n' + (index + 1) + '. Document: ' + doc.id);
            console.log('   Nom: ' + doc.name);
            console.log('   Score: ' + doc.currentScore);
            
            if (doc.captureTime) {
                const captureDate = doc.captureTime.toDate();
                console.log('   captureTime: ' + captureDate.toString());
                console.log('   captureTime (ISO): ' + captureDate.toISOString());
                console.log('   captureTime (timestamp): ' + captureDate.getTime());
            } else {
                console.log('   captureTime: NON');
            }
            
            if (doc.lastUpdated) {
                const lastUpdatedDate = doc.lastUpdated.toDate();
                console.log('   lastUpdated: ' + lastUpdatedDate.toString());
                console.log('   lastUpdated (ISO): ' + lastUpdatedDate.toISOString());
                console.log('   lastUpdated (timestamp): ' + lastUpdatedDate.getTime());
            } else {
                console.log('   lastUpdated: NON');
            }
        });
        
        // Vérifier spécifiquement le document attendu
        console.log('\n🎯 Vérification spécifique du document poi_jardin_royal:');
        const specificDoc = await db.collection('pois').doc('poi_jardin_royal').get();
        
        if (specificDoc.exists) {
            const data = specificDoc.data();
            console.log('   ✅ Document existe');
            console.log('   Nom: ' + data.name);
            console.log('   Score: ' + data.currentScore);
            
            if (data.captureTime) {
                const captureDate = data.captureTime.toDate();
                console.log('   captureTime: ' + captureDate.toString());
                console.log('   captureTime (ISO): ' + captureDate.toISOString());
                console.log('   captureTime (timestamp): ' + captureDate.getTime());
            }
            
            if (data.lastUpdated) {
                const lastUpdatedDate = data.lastUpdated.toDate();
                console.log('   lastUpdated: ' + lastUpdatedDate.toString());
                console.log('   lastUpdated (ISO): ' + lastUpdatedDate.toISOString());
                console.log('   lastUpdated (timestamp): ' + lastUpdatedDate.getTime());
            }
            
            // Calculer l'âge exact
            const now = new Date();
            const referenceTime = data.lastUpdated ? data.lastUpdated.toDate() : data.captureTime.toDate();
            const ageMs = now.getTime() - referenceTime.getTime();
            const ageHours = ageMs / (1000 * 60 * 60);
            
            console.log('\n⏰ Calcul:');
            console.log('   Heure actuelle: ' + now.toString());
            console.log('   Heure référence: ' + referenceTime.toString());
            console.log('   Âge: ' + ageHours.toFixed(2) + ' heures');
            console.log('   Âge: ' + (ageMs / 1000) + ' secondes');
            
        } else {
            console.log('   ❌ Document poi_jardin_royal n\'existe pas');
        }
        
    } catch (error) {
        console.error('❌ Erreur:', error);
    } finally {
        process.exit(0);
    }
}

checkJardinRoyalHistory();