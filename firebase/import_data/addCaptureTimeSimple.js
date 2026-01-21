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

async function addCaptureTimeToAllPois() {
  try {
    console.log('🚀 Début de l\'ajout du champ captureTime à tous les POIs...');
    
    // Récupérer tous les documents de la collection 'pois'
    const poisSnapshot = await db.collection('pois').get();
    
    if (poisSnapshot.empty) {
      console.log('❌ Aucun POI trouvé dans la base de données.');
      return;
    }
    
    console.log(`📊 Trouvé ${poisSnapshot.size} POIs à vérifier.`);
    
    let updatedCount = 0;
    let skippedCount = 0;
    let errorCount = 0;
    
    // Date par défaut : 7 jours dans le passé
    const defaultDate = new Date();
    defaultDate.setDate(defaultDate.getDate() - 7);
    
    // Traiter chaque POI
    for (const doc of poisSnapshot.docs) {
      try {
        const poiData = doc.data();
        const poiId = doc.id;
        const poiName = poiData.name || 'Sans nom';
        
        // Vérifier si le champ captureTime existe déjà
        if (poiData.captureTime) {
          console.log(`⏭️  POI "${poiName}" (${poiId}) : captureTime existe déjà, skipping.`);
          skippedCount++;
          continue;
        }
        
        // Déterminer la valeur de captureTime à utiliser
        let captureTime;
        
        if (poiData.lastUpdated) {
          // Utiliser lastUpdated s'il existe
          captureTime = poiData.lastUpdated;
          console.log(`📅 POI "${poiName}" (${poiId}) : Utilisation de lastUpdated comme captureTime.`);
        } else {
          // Sinon, utiliser la date par défaut
          captureTime = admin.firestore.Timestamp.fromDate(defaultDate);
          console.log(`🕐 POI "${poiName}" (${poiId}) : Utilisation de la date par défaut (7 jours dans le passé).`);
        }
        
        // Mettre à jour le document avec captureTime
        await db.collection('pois').doc(poiId).update({
          captureTime: captureTime
        });
        
        console.log(`✅ POI "${poiName}" (${poiId}) : captureTime ajouté avec succès.`);
        updatedCount++;
        
        // Petite pause pour éviter de surcharger la base de données
        await new Promise(resolve => setTimeout(resolve, 50));
        
      } catch (error) {
        console.error(`❌ Erreur lors de la mise à jour du POI ${doc.id}:`, error.message);
        errorCount++;
      }
    }
    
    console.log('\n' + '='.repeat(50));
    console.log('📋 RÉSUMÉ DE LA MISE À JOUR');
    console.log('='.repeat(50));
    console.log(`📊 Total POIs trouvés: ${poisSnapshot.size}`);
    console.log(`✅ POIs mis à jour: ${updatedCount}`);
    console.log(`⏭️  POIs ignorés (captureTime existant): ${skippedCount}`);
    console.log(`❌ Erreurs: ${errorCount}`);
    console.log('='.repeat(50));
    
    if (updatedCount > 0) {
      console.log('🎉 Mise à jour terminée avec succès!');
    } else {
      console.log('ℹ️  Aucune mise à jour nécessaire.');
    }
    
  } catch (error) {
    console.error('❌ Erreur générale lors de l\'exécution du script:', error);
  } finally {
    // Fermer la connexion Firebase
    process.exit(0);
  }
}

// Vérification rapide avant mise à jour
async function quickCheck() {
  try {
    console.log('🔍 Vérification rapide des POIs...');
    
    const poisSnapshot = await db.collection('pois').limit(5).get();
    
    if (poisSnapshot.empty) {
      console.log('❌ Aucun POI trouvé.');
      return;
    }
    
    console.log(`📊 Affichage de ${poisSnapshot.size} POIs (échantillon):`);
    
    poisSnapshot.forEach(doc => {
      const data = doc.data();
      const hasCaptureTime = data.captureTime ? '✅' : '❌';
      const hasLastUpdated = data.lastUpdated ? '✅' : '❌';
      console.log(`  ${hasCaptureTime} ${hasLastUpdated} ${data.name || doc.id} - captureTime: ${data.captureTime ? 'OUI' : 'NON'}, lastUpdated: ${data.lastUpdated ? 'OUI' : 'NON'}`);
    });
    
  } catch (error) {
    console.error('❌ Erreur lors de la vérification:', error.message);
  } finally {
    process.exit(0);
  }
}

// Fonction principale
async function main() {
  const args = process.argv.slice(2);
  
  if (args.includes('--check')) {
    await quickCheck();
  } else if (args.includes('--help')) {
    console.log(`
📖 Usage: node addCaptureTimeToPois.js [options]

Options:
  --check    Vérifie l'état actuel des POIs (échantillon de 5)
  --help     Affiche ce message d'aide

Par défaut: Exécute la mise à jour de tous les POIs
    `);
  } else {
    await addCaptureTimeToAllPois();
  }
}

// Gérer les erreurs non capturées
process.on('unhandledRejection', (reason, promise) => {
  console.error('❌ Unhandled Rejection at:', promise, 'reason:', reason);
  process.exit(1);
});

// Exécuter la fonction principale
main().catch(console.error);