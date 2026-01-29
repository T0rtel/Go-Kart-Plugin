package tortel.gokartsecondtry.data

class ServerData {
    //todo:save
    //----Saved-----
    var raceData : HashMap<String, RaceData> = HashMap()
    //----Temp-----
    var races : HashMap<String, TempRaceData> = HashMap()
    var queues : HashMap<String, TempQueue> = HashMap()
}