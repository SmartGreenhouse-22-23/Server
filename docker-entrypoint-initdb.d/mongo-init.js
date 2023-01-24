db = db.getSiblingDB('greenhouse');

db.createCollection('greenhouse');

db.greenhouse.insertMany( [
{
  _id: ObjectId('63af0ae025d55e9840cbc1fa'),
  plant: {
    name: 'lemon',
    description: 'is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.',
    img: 'http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg',
    minTemperature: 8.0,
    maxTemperature: 35.0,
    minBrightness: 4200.0,
    maxBrightness: 130000.0,
    minSoilMoisture: 20.0,
    maxSoilMoisture: 65.0,
    minHumidity: 30.0,
    maxHumidity: 80.0,
    unit: {
        temperature: '° C',
        humidity:'%',
        soilMoisture: '%',
        brightness: 'Lux'
    }
  },
  modality: 'AUTOMATIC'
},
{
  _id: ObjectId('63b29b0a3792e15bae3229a7'),
  plant: {
    name: 'lemon AUTOMATIC',
    description: 'is a species of small evergreen trees in the flowering plant family Rutaceae, native to Asia, primarily Northeast India (Assam), Northern Myanmar or China.',
    img: 'http://www.burkesbackyard.com.au/wp-content/uploads/2014/01/945001_399422270172619_1279327806_n.jpg',
    minTemperature: 8.0,
    maxTemperature: 35.0,
    minBrightness: 4200.0,
    maxBrightness: 130000.0,
    minSoilMoisture: 20.0,
    maxSoilMoisture: 65.0,
    minHumidity: 30.0,
    maxHumidity: 80.0,
    unit: {
        temperature: '° C',
        humidity:'%',
        soilMoisture: '%',
        brightness: 'Lux'
    }
  },
  modality: 'AUTOMATIC'
}
]);
