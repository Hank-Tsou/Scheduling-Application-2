package NoUse;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class ClearDB {
	
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");

	
	public ClearDB()
	{
		DBclear();
	}
	
	private void DBclear()
	{
		MongoCursor<Document> Employeecursor = mongoCollection.find().iterator();
		while (Employeecursor.hasNext()) {
			mongoCollection.deleteOne(Employeecursor.next());
		}
		
		MongoCursor<Document> Meetingcursor = mongoCollectionMeeting.find().iterator();
		while (Meetingcursor.hasNext()) {
			mongoCollectionMeeting.deleteOne(Meetingcursor.next());
		}
		
		MongoCursor<Document> Roomcursor = mongoCollectionRooms.find().iterator();
		while (Roomcursor.hasNext()) {
			mongoCollectionRooms.deleteOne(Roomcursor.next());
		}
		
		MongoCursor<Document> Admincursor = mongoCollectionAdmin.find().iterator();
		while (Admincursor.hasNext()) {
			mongoCollectionAdmin.deleteOne(Admincursor.next());
		}
	}

}
