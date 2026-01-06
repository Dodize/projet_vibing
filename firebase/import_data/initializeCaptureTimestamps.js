const admin = require('firebase-admin');
const serviceAccount = require('../google-services.json');

// Initialize Firebase Admin SDK
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();
const now = new Date();

async function initializeAllCaptureTimestamps() {
  try {
    console.log('Starting to initialize all capture timestamps to current time...');
    
    // Get all POIs
    const poisSnapshot = await db.collection('pois').get();
    
    if (poisSnapshot.empty) {
      console.log('No POIs found in database.');
      return;
    }
    
    console.log(`Found ${poisSnapshot.size} POIs to update.`);
    
    const batch = db.batch();
    let updatedCount = 0;
    
    poisSnapshot.forEach(doc => {
      const poiRef = db.collection('pois').doc(doc.id);
      
      // Update with current timestamp and default score
      batch.update(poiRef, {
        captureTime: now,
        currentScore: 100,
        lastUpdated: now
      });
      
      updatedCount++;
      
      // Firebase batch limit is 500 operations
      if (updatedCount % 500 === 0) {
        console.log(`Processed ${updatedCount} POIs...`);
      }
    });
    
    // Commit the batch
    await batch.commit();
    
    console.log(`Successfully updated ${updatedCount} POIs with captureTime: ${now.toISOString()}`);
    console.log('All POIs now have current timestamp for score calculation.');
    
  } catch (error) {
    console.error('Error initializing capture timestamps:', error);
  } finally {
    // Close the Firebase connection
    admin.app().delete();
  }
}

// Run the initialization
initializeAllCaptureTimestamps();