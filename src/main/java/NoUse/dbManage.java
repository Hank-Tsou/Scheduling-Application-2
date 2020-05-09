package NoUse;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class dbManage
{
	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");

	// Size of Database = System.out.print(mongoCollectionMeeting.count());

	public dbManage()
	{
		//System.out.print("success");
		DBtest();
	}
	
	private void DBtest()
	{
		
// Employee Database //////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		String username = "Hank";
		
		
		// Delete Member
		/*
		for(int i = 0; i<10; i++)
		{
			Bson filter = new Document("Availability", "Available");
			mongoCollection.deleteOne(filter);
		}
        
		//*/
		
		// New Member
		/* 
		ArrayList< DBObject > array = new ArrayList< DBObject >();
			Document document = new Document("EID", 0);
			document.append("Name", "Admin");
			document.append("Availability", "Available");
			document.append("Username", "Admin");
			document.append("Password", "null");
			
			document.append("University", "Cal Poly Pomona");
			document.append("Department", "Computer Science");
			document.append("Degree", "Staff");
			document.append("Byear", "1978");
			
			document.append("Meeting", array);
			mongoCollection.insertOne(document);
		//*/
        
		// Add meeting detail in meeting array
		/* 
		BasicDBObject match = new BasicDBObject();
        match.put( "Name", username );

        BasicDBObject addressSpec = new BasicDBObject();
        addressSpec.put("MeetingID", 2);
        addressSpec.put("Respond", "A");
        addressSpec.put("Update", "0");

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
        mongoCollection.updateMany( match, update );
		//*/

//Meeting/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 
		int MeetingID = 1;
		
		// Delete Meeting
		/*
		for(int i =1; i<9; i++)
		{
			Bson filter = new Document("Host", "Hank");
		    mongoCollectionMeeting.deleteOne(filter);
		}
			
	    //*/  

//MeetingID=1, Host=Hank, Date=0321, StartTime=10, EndTime=11, Room=1002, Member=[Hank, Roger, Todd, Vincent
		// Add New Meeting
        /*
        ArrayList< DBObject > array = new ArrayList< DBObject >();
        
		Document document = new Document("MeetingID", MeetingID);
		document.append("Host", "Hank");
		document.append("Date", "0328");
		document.append("StartTime", "16");
		document.append("EndTime", "17");
		document.append("Room", "1003");
		document.append("Member", array);
		mongoCollectionMeeting.insertOne(document);
		//*/
		
		/*
		BasicDBObject match = new BasicDBObject();
        match.put("MeetingID", MeetingID);

        BasicDBObject addressSpec = new BasicDBObject();
        addressSpec.put("member", "Hank");

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "Member", addressSpec ) );
        mongoCollectionMeeting.updateMany( match, update );
		//*/
		///*
		// Add member in member array
		/*
		mongoCollectionMeeting. updateOne( Filters.eq( "MeetingID", 1),  
                new Document( "$addToSet", new Document( "Member", "Vincent")))  
                .wasAcknowledged ();
		//*/
		// how to create an array [aaa, bbb, ccc, ddd, eee] ????????????????????????????????
		
//ROOM///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
        String RoomNo = "1002";
        
        // Delete Room
		/*
        Bson filter = new Document("RoomNo", RoomNo);
        mongoCollectionRooms.deleteOne(filter);
        //*/
        
        // New a Room
        /*
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		Document document = new Document("RID", "5");
		document.append("RoomNo", RoomNo);
		document.append("TimeBooked", array);
		mongoCollectionRooms.insertOne(document);
		//*/
        
        // Add new booked time in the room schedule
        /*
        BasicDBObject match = new BasicDBObject();
        match.put( "RoomNo", RoomNo );

        BasicDBObject addressSpec = new BasicDBObject();
        addressSpec.put("Date", "0321");
        addressSpec.put("StartTime", "10");
        addressSpec.put("EndTime", "11");
        addressSpec.put("Host", "Hank");
        addressSpec.put("MeetingID", MeetingID);

        BasicDBObject update = new BasicDBObject();
        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
        mongoCollectionRooms.updateMany( match, update );
        //*/


////////// Update Meeting //////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// delete meeting
		/*
		   mongoCollection.updateOne(  
                new Document ("Name","Lisa"),  
                new Document( "$pull", new Document("Meeting" ,  
                        new Document( "MeetingID", 1))))  
                .wasAcknowledged ();  
                //*/
		
		// delete room meeting
					/*
				   mongoCollectionRooms.updateOne(  
		                new Document ("RoomNo","1002"),  
		                new Document( "$pull", new Document("TimeBooked" ,  
		                        new Document( "Host", "Hank"))))  
		                .wasAcknowledged ();  
		                //*/

		//Document myDoc = mongoCollectionMeeting.find(Filters.eq("MeetingID", 1 )).first(); //get member
		
		//List<Document> MeetingLists = (List<Document>) myDoc.get("Member");
		//Document myMeeting = MeetingLists.
	    //System.out.println(MeetingLists.get(0)); 
	    
////////// Print Database //////////////////////////////////////////////////////////////////////////////////////////////////////////
        // mongoCollection
        // mongoCollectionRooms
        // mongoCollectionMeeting
        ///*
	    FindIterable<Document> findIterable = mongoCollection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        } 
        System.out.print("\n");
        FindIterable<Document> findIterableMeeting = mongoCollectionMeeting.find();
        MongoCursor<Document> mongoCursorMeeting = findIterableMeeting.iterator();  
        while(mongoCursorMeeting.hasNext()){  
           System.out.println(mongoCursorMeeting.next());  
        } 
        System.out.print("\n");
        FindIterable<Document> findIterableRoom = mongoCollectionRooms.find();
        MongoCursor<Document> mongoCursorRoom = findIterableRoom.iterator();  
        while(mongoCursorRoom.hasNext()){  
           System.out.println(mongoCursorRoom.next());  
        } 
        System.out.print("\n");
        FindIterable<Document> findIterableAdmin = mongoCollectionAdmin.find();
        MongoCursor<Document> mongoCursorAdmin = findIterableAdmin.iterator();  
        while(mongoCursorAdmin.hasNext()){  
           System.out.println(mongoCursorAdmin.next());  
        } 
        //*/
       // Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", 1 )).first();
        //System.out.print(myMeeting+"++++++++++++++++ \n");
        
        //Document myDoc = (Document)mongoCollectionMeeting.find().sort(new BasicDBObject("MeetingID",-1)).first();
        //System.out.print(myDoc.getInteger("MeetingID"));
       // Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", 1)).first();
        //System.out.println(myMeeting);  
        
	}

}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Roger Tutorial //////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//this connects to the database
//String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
//MongoClientURI clientUri = new MongoClientURI(uri);
//MongoClient mongoClient = new MongoClient(clientUri);
//MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
//MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
//MongoCollection<Document> mongoCollectionMessages = mongoDatabase.getCollection("Messages");
//MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
//
//used to test the connection
//Document document = new Document("EID", "11");
//document.append("Name", "");
//document.append("Availability", "Available");
//document.append("Username", "");
//document.append("Password", "1234");
//
//mongoCollection.insertOne(document);

//Document document = new Document("MID", "1");
//document.append("From", "Roger");
//document.append("To", "Hank");
//document.append("Message", "Hello! Meeting at 9 in Rhodes");
//document.append("Response", "");
//
//mongoCollectionMessages.insertOne(document);

//Used to update only one document by a filter ("Name") and field("Availability")
//Bson filter = new Document("Name", "Roger");
//Bson newValue = new Document("Availability", "Unavailable");
//Bson updateOperationDocument = new Document("$set", newValue);
//mongoCollection.updateOne(filter, updateOperationDocument);

//updating a sub document
//Bson filter = new Document("Name", "Roger");
//Bson newValue = new Document("TimeAvailable.Monday.Start1", "9");
//Bson updateOperationDocument = new Document("$set", newValue);
//mongoCollection.updateOne(filter, updateOperationDocument);

//updated everyone with the new documents
//Document document = new Document("Start1", "");
//document.append("End1", "");
//document.append("Start2", "");
//document.append("End2", "");
//document.append("Start3", "");
//document.append("End3", "");
//
//BasicDBObject Available = new BasicDBObject();
//Available.put("Monday",  document);
//Available.put("Tuesday",document );
//Available.put("Wednesday",document );
//Available.put("Thursday", document);
//Available.put("Friday", document);
//Available.put("Saturday", document);
//Available.put("Sunday", document);
//Bson update = new Document("TimeAvailable", Available);
//Bson updateOperationDocument = new Document("$set", update);
//mongoCollection.updateMany(Filters.exists("Name"), updateOperationDocument);

//used to read from the database by a specific Filter
//filter can be "EID", "Name", "Availability", "Username", "Password"

//how read sub documents
//Document subDoc = (Document) myDoc.get("TimeAvailable");
//Document subSubDoc = (Document) subDoc.get("Monday");
//System.out.println(subSubDoc.get("Start1"));

//try{
//	Document myDoc = mongoCollectionMessages.find(Filters.eq("From", "Roger")).first();
//	System.out.println(myDoc.get("Message"));
//}
//catch(Exception e) 
//{
//	System.out.println("Failed to find doc");
//}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////// CODE METHOD RECORD //////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*
FindIterable<Document> findIterable = mongoCollection.find();  
MongoCursor<Document> mongoCursor = findIterable.iterator();  
while(mongoCursor.hasNext()){  
   System.out.println(mongoCursor.next());  
}  
*/

//(RECORD FOR USED)create meeting Array
/* 
Bson filter = new Document("Name", username);
ArrayList< DBObject > array = new ArrayList< DBObject >();
Bson new_document = new Document("Meeting", array);
Bson updateOperationDocument2 = new Document("$set", new_document);
mongoCollection.updateOne(filter, updateOperationDocument2);


		
///////// NOT USE just for record (add detail in element) ///////////////////////
        /* 
        Document document = new Document("Date", "0213");
		document.append("StartTime", "9");
		document.append("EndTime", "18");
		document.append("Host", "Amy");
		document.append("Room", "1001");
		document.append("Respond", "W");
		
		BasicDBObject MeetingObj = new BasicDBObject();
		MeetingObj.put("Meeting2",  document);
		
		Bson update = new Document("Meeting", MeetingObj);
		Bson updateOperationDocument = new Document("$set", update);
		
		mongoCollection.updateMany(filter, updateOperationDocument);
		*/
///////// NOT USE just for record (add detail in element) ///////////////////////
		
///////// I FORTGET WHAT IS THIS save for record ////////////////////////////////
		/*
		Bson filter = new Document("Name", "Hank");
        MongoCursor<Document> cursor = mongoCollection.find(filter).iterator();
        
        //print all the array element
        
		try {
        	while (cursor.hasNext()) {
        	    Document str = cursor.next();
        	    List<Document> list = (List<Document>)str.get("Meeting");
        	    System.out.println(str.get(list.get(2))); // display specific field
        	}
        	} finally {
        	cursor.close();
        	}
       */
///////// I FORTGET WHAT IS THIS save for record ////////////////////////////////
		
        // get array element
        /*
        Document myDoc = mongoCollection.find(Filters.eq("Name", "Joe" )).first();
		List<Document> courses = (List<Document>) myDoc.get("Meeting");
		Document course = courses.get(0);
		
		System.out.println(course.getString("Date"));
		System.out.println(course.getString("Host"));
		System.out.println(course.getString("Room"));
         */
        
		//Document myDoc = mongoCollection.find(Filters.eq("Name", username )).first();
		//System.out.print(mongoCollectionRooms);
        //System.out.println(courses);
        //System.out.println(courses.size());
