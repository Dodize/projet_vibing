const fs = require('fs');
const path = require('path');

// Read the original file
const filePath = path.join(__dirname, 'importToulousePois.js');
let content = fs.readFileSync(filePath, 'utf8');

// Read corrected coordinates
const correctedCoords = require('./pois_toulouse_corrected.json');

// Replace coordinates for each POI
Object.keys(correctedCoords).forEach(poiId => {
    const coords = correctedCoords[poiId];
    const oldPattern = new RegExp(`"${poiId}":\\s*{[^}]*"location":\\s*{[^}]*"latitude":\\s*[\\d.]+[^}]*"longitude":\\s*[\\d.]+[^}]*}`, 'g');
    
    const newLocation = `"location": { "latitude": ${coords.latitude}, "longitude": ${coords.longitude} }`;
    
    // Replace the location part while preserving other properties
    content = content.replace(oldPattern, (match) => {
        return match.replace(/"location":\s*{[^}]*}/, newLocation);
    });
});

// Write the updated content back to the file
fs.writeFileSync(filePath, content, 'utf8');

console.log('✅ Updated importToulousePois.js with corrected coordinates from JSON file!');
console.log(`Updated ${Object.keys(correctedCoords).length} POIs with their corrected coordinates.`);