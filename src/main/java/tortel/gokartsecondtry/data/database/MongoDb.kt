package tortel.gokartsecondtry.data.database

import com.hakan.core.HCore
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.WriteConcern
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.ReplaceOptions
import com.mongodb.connection.ConnectionPoolSettings
import lombok.Getter
import org.bson.Document
import org.bukkit.event.player.PlayerJoinEvent
import tortel.gokartsecondtry.Main


object MongoDb : AutoCloseable {

    @Getter
    var playerProfilesCollection: MongoCollection<Document>? = null
    @Getter
    var connected: Boolean = false

    private lateinit var mongoClient: MongoClient
    private lateinit var mongoDatabase: MongoDatabase

    fun SetupDB(databaseName: String? = null, collectionName: String? = null): MongoDatabase{
        return connect(databaseName, collectionName)
    }


    private fun connect(databaseName: String? = null, collectionName: String? = null): MongoDatabase {
        val connectionString = String.format("mongodb+srv://admin:HtKyprc87BMYKRCo4rTrG3eMECjqdS@clobnet.ucuwlbq.mongodb.net/") //mongodb+srv://admin:HtKyprc87BMYKRCo4rTrG3eMECjqdS@clobnet.ucuwlbq.mongodb.net/
        val connString = ConnectionString(connectionString)
        val clientSettings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .writeConcern(WriteConcern.ACKNOWLEDGED)
            .applyToConnectionPoolSettings { builder: ConnectionPoolSettings.Builder ->
                builder.maxSize(100).minSize(10)
            }
            .build()

        this.mongoClient = MongoClients.create(clientSettings)
        val database = mongoClient.getDatabase(databaseName!!)
        //playerProfilesCollection = database.getCollection(collectionName!!)
        connected = try {
            mongoClient.listDatabaseNames()
            mongoDatabase = database
            true
        } catch (e: Exception) {
            false
        }

        return database
    }

    fun getCollection(collectionName: String): MongoCollection<Document> {
        if (!connected) {
            throw IllegalStateException("MongoClient not initialized. Call connect() first.")
        }

        return mongoDatabase.getCollection(collectionName)
    }

    fun reconnect() {
        if (mongoClient != null) mongoClient.close()
        Main.instance!!.getLogger().info("Trying to re-establish connection to the database...")
        connect()
    }

    fun isNotConnected(): Boolean {
        return !connected
    }

    fun saveProfile(PlayerId: String?, document: Document?) {
        require(!isNotConnected()) { "MongoDB is not connected!" }
        HCore.asyncScheduler().run(Runnable {
            playerProfilesCollection!!.replaceOne(
                Document(
                    "_id",
                    PlayerId
                ), document, ReplaceOptions().upsert(true)
            )
        })
    }

    fun loadProfile(documentID: String?): Document? {
        return if (isNotConnected()) null else try {
            val query = Document("_id", documentID)
            playerProfilesCollection!!.find(query).first()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    fun OnJoin(PlayerId: String?){
        val newdoc = Document("_id",PlayerId)
        playerProfilesCollection!!.insertOne(newdoc)
    }
    /*
    override fun close() {
        mongoClient.close()
    }
     */

    override fun close() {
        if (this::mongoClient.isInitialized) {
            mongoClient.close()
            println("MongoDB connection closed.")
        }
    }
}