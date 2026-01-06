const admin = require('firebase-admin');
const serviceAccount = require('../vibingn7-882705adcdad.json');

// Initialiser Firebase Admin
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const validTeams = [1, 2, 3, 4]; // Équipes valides

async function fixTeam5Pois() {
  console.log('🔧 Début de la correction des POIs avec team_5...');
  
  try {
    // Récupérer tous les POIs
    const poisSnapshot = await db.collection('pois').get();
    
    if (poisSnapshot.empty) {
      console.log('❌ Aucun POI trouvé dans la base de données');
      return;
    }
    
    console.log(`📊 ${poisSnapshot.size} POIs trouvés, vérification des équipes...`);
    
    let correctedCount = 0;
    let batch = db.batch();
    let operationsInBatch = 0;
    const MAX_OPERATIONS_PER_BATCH = 500; // Limite Firestore
    
    for (const doc of poisSnapshot.docs) {
      const poiData = doc.data();
      
      // Vérifier si le POI a une équipe team_5 (ou 5) - vérifier owningTeam ET ownerTeamId
      const hasInvalidTeam = (poiData.owningTeam === 5 || poiData.owningTeam === 'team_5') ||
                            (poiData.ownerTeamId === 'team_5');
      
      if (hasInvalidTeam) {
        const currentTeam = poiData.owningTeam || poiData.ownerTeamId;
        console.log(`🔍 POI "${poiData.name || doc.id}" a une équipe invalide: ${currentTeam}`);
        
        // Assigner une équipe aléatoire valide
        const randomTeam = validTeams[Math.floor(Math.random() * validTeams.length)];
        
        console.log(`🎯 Attribution de l'équipe ${randomTeam} au POI "${poiData.name || doc.id}"`);
        
        // Mettre à jour le document - corriger les deux champs possibles
        const docRef = db.collection('pois').doc(doc.id);
        const updateData = {};
        
        if (poiData.owningTeam !== undefined) {
          updateData.owningTeam = randomTeam;
        }
        if (poiData.ownerTeamId !== undefined) {
          updateData.ownerTeamId = `team_${randomTeam}`;
        }
        
        batch.update(docRef, updateData);
        
        operationsInBatch++;
        correctedCount++;
        
        // Exécuter le batch si on atteint la limite
        if (operationsInBatch >= MAX_OPERATIONS_PER_BATCH) {
          await batch.commit();
          console.log(`✅ Batch de ${operationsInBatch} opérations exécuté`);
          batch = db.batch();
          operationsInBatch = 0;
        }
      }
    }
    
    // Exécuter le dernier batch s'il reste des opérations
    if (operationsInBatch > 0) {
      await batch.commit();
      console.log(`✅ Dernier batch de ${operationsInBatch} opérations exécuté`);
    }
    
    console.log(`🎉 Correction terminée ! ${correctedCount} POIs ont été mis à jour avec des équipes aléatoires valides.`);
    
  } catch (error) {
    console.error('❌ Erreur lors de la correction des POIs:', error);
  }
}

// Fonction pour vérifier les équipes après correction
async function verifyTeams() {
  console.log('\n🔍 Vérification des équipes après correction...');
  
  try {
    const poisSnapshot = await db.collection('pois').get();
    let invalidTeams = [];
    
    poisSnapshot.forEach(doc => {
      const poiData = doc.data();
      const hasInvalidTeam = (poiData.owningTeam === 5 || poiData.owningTeam === 'team_5') ||
                            (poiData.ownerTeamId === 'team_5');
      
      if (hasInvalidTeam) {
        const currentTeam = poiData.owningTeam || poiData.ownerTeamId;
        invalidTeams.push({
          id: doc.id,
          name: poiData.name || 'Sans nom',
          team: currentTeam
        });
      }
    });
    
    if (invalidTeams.length === 0) {
      console.log('✅ Tous les POIs ont maintenant des équipes valides !');
    } else {
      console.log(`⚠️  ${invalidTeams.length} POIs ont encore des équipes invalides :`);
      invalidTeams.forEach(poi => {
        console.log(`   - ${poi.name} (${poi.id}): équipe ${poi.team}`);
      });
    }
    
  } catch (error) {
    console.error('❌ Erreur lors de la vérification:', error);
  }
}

// Exécuter la correction puis la vérification
async function main() {
  await fixTeam5Pois();
  await verifyTeams();
  
  // Fermer la connexion Firebase
  await admin.app().delete();
  console.log('\n👋 Script terminé');
}

// Lancer le script
main().catch(console.error);