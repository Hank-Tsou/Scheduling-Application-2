package cs580;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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

public class test
{
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:Hank82903*@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");

	// Size of Database = System.out.print(mongoCollectionMeeting.count());

	public test()
	{
		DBtest();
	}
	
	private void DBtest()
	{
		ClearDB();
		SetupEmp();
		SetupRoom();		
		SetupMeeting();
		
		
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
        
        
	}
	
	
	private void ClearDB()
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

	private void SetupEmp()
	{
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		
		Document document = new Document("EID", 0);
		document.append("Name", "Admin");
		document.append("Availability", "Unavailable");
		document.append("Username", "AdminU");
		document.append("Password", "null");
		
		document.append("University", "Cal Poly Pomona");
		document.append("Department", "Computer Science");
		document.append("Degree", "Staff");
		document.append("Byear", "1978");
		
		document.append("Meeting", array);
		mongoCollection.insertOne(document);
		////////////////////////////////////
		Document document1 = new Document("EID", 1);
		document1.append("Name", "Employee1");
		document1.append("Availability", "Available");
		document1.append("Username", "UEmployee1");
		document1.append("Password", "0000");
		
		document1.append("University", "Cal Poly Pomona");
		document1.append("Department", "Computer Science");
		document1.append("Degree", "Master Student");
		document1.append("Byear", "1990");
		
		document1.append("Meeting", array);
		mongoCollection.insertOne(document1);

			BasicDBObject match = new BasicDBObject();
		    match.put( "Name", "Employee1");
		
		    BasicDBObject addressSpec = new BasicDBObject();
		    addressSpec.put("MeetingID", 1);
		    addressSpec.put("Respond", "A");
		    addressSpec.put("Update", "0");
		
		    BasicDBObject update = new BasicDBObject();
		    update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
		    mongoCollection.updateMany( match, update );
		    
		    BasicDBObject addressSpec1 = new BasicDBObject();
		    addressSpec1.put("MeetingID", 6);
		    addressSpec1.put("Respond", "A");
		    addressSpec1.put("Update", "0");
		    
		    BasicDBObject update1 = new BasicDBObject();
		    update1.put( "$push", new BasicDBObject( "Meeting", addressSpec1 ) );
		    mongoCollection.updateMany( match, update1 );

/////////////////////////////////////////////////////////////////////////
		    
	    Document document2 = new Document("EID", 2);
	    document2.append("Name", "Employee2");
	    document2.append("Availability", "Available");
	    document2.append("Username", "UEmployee2");
	    document2.append("Password", "0000");
		
	    document2.append("University", "Cal Poly Pomona");
	    document2.append("Department", "Computer Science");
	    document2.append("Degree", "Master Student");
	    document2.append("Byear", "1990");
		
	    document2.append("Meeting", array);
		mongoCollection.insertOne(document2);

			BasicDBObject match2 = new BasicDBObject();
			match2.put( "Name", "Employee2");
		
		    BasicDBObject addressSpec21 = new BasicDBObject();
		    addressSpec21.put("MeetingID", 1);
		    addressSpec21.put("Respond", "A");
		    addressSpec21.put("Update", "0");
		
		    BasicDBObject update21 = new BasicDBObject();
		    update21.put( "$push", new BasicDBObject( "Meeting", addressSpec21 ) );
		    mongoCollection.updateMany( match2, update21 );
		    /////
		    BasicDBObject addressSpec22 = new BasicDBObject();
		    addressSpec22.put("MeetingID", 2);
		    addressSpec22.put("Respond", "A");
		    addressSpec22.put("Update", "0");
		    
		    BasicDBObject update22 = new BasicDBObject();
		    update22.put( "$push", new BasicDBObject( "Meeting", addressSpec22 ) );
		    mongoCollection.updateMany( match2, update22 );
		    /////
		    BasicDBObject addressSpec23 = new BasicDBObject();
		    addressSpec23.put("MeetingID", 5);
		    addressSpec23.put("Respond", "A");
		    addressSpec23.put("Update", "0");
		    
		    BasicDBObject update23 = new BasicDBObject();
		    update23.put( "$push", new BasicDBObject( "Meeting", addressSpec23 ) );
		    mongoCollection.updateMany( match2, update23 );
/////////////////////////////////////////////////////////////////////////
		Document document3 = new Document("EID", 3);
	    document3.append("Name", "Employee3");
	    document3.append("Availability", "Available");
	    document3.append("Username", "UEmployee3");
	    document3.append("Password", "0000");
		
	    document3.append("University", "Cal Poly Pomona");
	    document3.append("Department", "Computer Science");
	    document3.append("Degree", "Master Student");
	    document3.append("Byear", "1990");
		
	    document3.append("Meeting", array);
		mongoCollection.insertOne(document3);

			BasicDBObject match3 = new BasicDBObject();
			match3.put( "Name", "Employee3");
		
		    BasicDBObject addressSpec31 = new BasicDBObject();
		    addressSpec31.put("MeetingID", 1);
		    addressSpec31.put("Respond", "A");
		    addressSpec31.put("Update", "0");
		
		    BasicDBObject update31 = new BasicDBObject();
		    update31.put( "$push", new BasicDBObject( "Meeting", addressSpec31 ) );
		    mongoCollection.updateMany( match3, update31 );
		   
///////////////////////////////////////////////////////////////////////
		    Document document4 = new Document("EID", 4);
		    document4.append("Name", "Employee4");
		    document4.append("Availability", "Available");
		    document4.append("Username", "UEmployee4");
		    document4.append("Password", "0000");
			
		    document4.append("University", "Cal Poly Pomona");
		    document4.append("Department", "Computer Science");
		    document4.append("Degree", "Master Student");
		    document4.append("Byear", "1990");
			
		    document4.append("Meeting", array);
			mongoCollection.insertOne(document4);

				BasicDBObject match4 = new BasicDBObject();
				match4.put( "Name", "Employee4");
			
			    BasicDBObject addressSpec41 = new BasicDBObject();
			    addressSpec41.put("MeetingID", 2);
			    addressSpec41.put("Respond", "A");
			    addressSpec41.put("Update", "0");
			
			    BasicDBObject update41 = new BasicDBObject();
			    update41.put( "$push", new BasicDBObject( "Meeting", addressSpec41 ) );
			    mongoCollection.updateMany( match4, update41 );
///////////////////////////////////////////////////////////////////////
		    Document document5 = new Document("EID", 5);
		    document5.append("Name", "Employee5");
		    document5.append("Availability", "Available");
		    document5.append("Username", "UEmployee5");
		    document5.append("Password", "0000");
			
		    document5.append("University", "Cal Poly Pomona");
		    document5.append("Department", "Computer Science");
		    document5.append("Degree", "Master Student");
		    document5.append("Byear", "1990");
			
		    document5.append("Meeting", array);
			mongoCollection.insertOne(document5);

				BasicDBObject match5 = new BasicDBObject();
				match5.put( "Name", "Employee5");
			
			    BasicDBObject addressSpec51 = new BasicDBObject();
			    addressSpec51.put("MeetingID", 3);
			    addressSpec51.put("Respond", "A");
			    addressSpec51.put("Update", "0");
			
			    BasicDBObject update51 = new BasicDBObject();
			    update51.put( "$push", new BasicDBObject( "Meeting", addressSpec51 ) );
			    mongoCollection.updateMany( match5, update51 );
			    /////
			    BasicDBObject addressSpec52 = new BasicDBObject();
			    addressSpec52.put("MeetingID", 4);
			    addressSpec52.put("Respond", "A");
			    addressSpec52.put("Update", "0");
			
			    BasicDBObject update52 = new BasicDBObject();
			    update52.put( "$push", new BasicDBObject( "Meeting", addressSpec52 ) );
			    mongoCollection.updateMany( match5, update52 );
///////////////////////////////
		    Document document6 = new Document("EID", 6);
		    document6.append("Name", "Employee6");
		    document6.append("Availability", "Available");
		    document6.append("Username", "UEmployee6");
		    document6.append("Password", "0000");
			
		    document6.append("University", "Cal Poly Pomona");
		    document6.append("Department", "Computer Science");
		    document6.append("Degree", "Master Student");
		    document6.append("Byear", "1990");
			
		    document6.append("Meeting", array);
			mongoCollection.insertOne(document6);

				BasicDBObject match6 = new BasicDBObject();
				match6.put( "Name", "Employee6");
			
			    BasicDBObject addressSpec61 = new BasicDBObject();
			    addressSpec61.put("MeetingID", 4);
			    addressSpec61.put("Respond", "A");
			    addressSpec61.put("Update", "0");
			
			    BasicDBObject update61 = new BasicDBObject();
			    update61.put( "$push", new BasicDBObject( "Meeting", addressSpec61 ) );
			    mongoCollection.updateMany( match6, update61 );
//////////////////////////////////////////////
		    Document document7 = new Document("EID", 7);
		    document7.append("Name", "Employee7");
		    document7.append("Availability", "Available");
		    document7.append("Username", "UEmployee7");
		    document7.append("Password", "0000");
			
		    document7.append("University", "Cal Poly Pomona");
		    document7.append("Department", "Computer Science");
		    document7.append("Degree", "Master Student");
		    document7.append("Byear", "1990");
			
		    document7.append("Meeting", array);
			mongoCollection.insertOne(document7);

				BasicDBObject match7 = new BasicDBObject();
				match7.put( "Name", "Employee7");
			
			    BasicDBObject addressSpec71 = new BasicDBObject();
			    addressSpec71.put("MeetingID", 3);
			    addressSpec71.put("Respond", "P");
			    addressSpec71.put("Update", "0");
			
			    BasicDBObject update71 = new BasicDBObject();
			    update71.put( "$push", new BasicDBObject( "Meeting", addressSpec71 ) );
			    mongoCollection.updateMany( match7, update71 );
			    /////
			    BasicDBObject addressSpec72 = new BasicDBObject();
			    addressSpec72.put("MeetingID", 5);
			    addressSpec72.put("Respond", "A");
			    addressSpec72.put("Update", "0");
			
			    BasicDBObject update72 = new BasicDBObject();
			    update72.put( "$push", new BasicDBObject( "Meeting", addressSpec72 ) );
			    mongoCollection.updateMany( match7, update72 );
	}

	private void SetupRoom()
	{
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		Document document = new Document("RID", 1);
		document.append("RoomNo", "1001");
		document.append("TimeBooked", array);
		mongoCollectionRooms.insertOne(document);
		/////
	        BasicDBObject match = new BasicDBObject();
	        match.put( "RoomNo", "1001" );
	
	        BasicDBObject addressSpec = new BasicDBObject();
	        addressSpec.put("Date", "0528");
	        addressSpec.put("StartTime", "7");
	        addressSpec.put("EndTime", "9");
	        addressSpec.put("Host", "Employee1");
	        addressSpec.put("MeetingID", 1);
	
	        BasicDBObject update = new BasicDBObject();
	        update.put( "$push", new BasicDBObject( "TimeBooked", addressSpec ) );
	        mongoCollectionRooms.updateMany( match, update );
        /////
	        BasicDBObject addressSpec12 = new BasicDBObject();
	        addressSpec12.put("Date", "0528");
	        addressSpec12.put("StartTime", "13");
	        addressSpec12.put("EndTime", "14");
	        addressSpec12.put("Host", "Employee7");
	        addressSpec12.put("MeetingID", 5);
	
	        BasicDBObject update12 = new BasicDBObject();
	        update12.put( "$push", new BasicDBObject( "TimeBooked", addressSpec12 ) );
	        mongoCollectionRooms.updateMany( match, update12 );
 ///////////////////////////////////////////////////////      
		Document document2 = new Document("RID", 2);
		document2.append("RoomNo", "1002");
		document2.append("TimeBooked", array);
		mongoCollectionRooms.insertOne(document2);
		/////
	        BasicDBObject match2 = new BasicDBObject();
	        match2.put( "RoomNo", "1002" );
	
	        BasicDBObject addressSpec21 = new BasicDBObject();
	        addressSpec21.put("Date", "0528");
	        addressSpec21.put("StartTime", "8");
	        addressSpec21.put("EndTime", "11");
	        addressSpec21.put("Host", "Employee5");
	        addressSpec21.put("MeetingID", 3);
	
	        BasicDBObject update21 = new BasicDBObject();
	        update21.put( "$push", new BasicDBObject( "TimeBooked", addressSpec21 ) );
	        mongoCollectionRooms.updateMany( match2, update21 );
	        /////
	        BasicDBObject addressSpec22 = new BasicDBObject();
	        addressSpec22.put("Date", "0528");
	        addressSpec22.put("StartTime", "12");
	        addressSpec22.put("EndTime", "14");
	        addressSpec22.put("Host", "Employee5");
	        addressSpec22.put("MeetingID", 4);
	
	        BasicDBObject update22 = new BasicDBObject();
	        update22.put( "$push", new BasicDBObject( "TimeBooked", addressSpec22 ) );
	        mongoCollectionRooms.updateMany( match2, update22 );
///////////////////////////////////////////////////////      
		Document document3 = new Document("RID", 3);
		document3.append("RoomNo", "1003");
		document3.append("TimeBooked", array);
		mongoCollectionRooms.insertOne(document3);
		/////
		BasicDBObject match3 = new BasicDBObject();
		match3.put( "RoomNo", "1003" );
		
		BasicDBObject addressSpec31 = new BasicDBObject();
		addressSpec31.put("Date", "0528");
		addressSpec31.put("StartTime", "10");
		addressSpec31.put("EndTime", "11");
		addressSpec31.put("Host", "Employee2");
		addressSpec31.put("MeetingID", 2);
		
		BasicDBObject update31 = new BasicDBObject();
		update31.put( "$push", new BasicDBObject( "TimeBooked", addressSpec31 ) );
		mongoCollectionRooms.updateMany( match3, update31 );
		/////
		BasicDBObject addressSpec32 = new BasicDBObject();
		addressSpec32.put("Date", "0528");
		addressSpec32.put("StartTime", "13");
		addressSpec32.put("EndTime", "15");
		addressSpec32.put("Host", "Employee1");
		addressSpec32.put("MeetingID", 6);
		
		BasicDBObject update32 = new BasicDBObject();
		update32.put( "$push", new BasicDBObject( "TimeBooked", addressSpec32 ) );
		mongoCollectionRooms.updateMany( match3, update32 );
	}

	private void SetupMeeting()
	{
		ArrayList< DBObject > array = new ArrayList< DBObject >();
		
		Document document = new Document("MeetingID", 1);
		document.append("Host", "Employee1");
		document.append("Date", "0528");
		document.append("StartTime", "7");
		document.append("EndTime", "9");
		document.append("Room", "1001");
		document.append("Member", array);
		mongoCollectionMeeting.insertOne(document);
        ///////
		BasicDBObject match = new BasicDBObject();
		match.put( "MeetingID", 1 );

		mongoCollectionMeeting. updateOne( match,  
                new Document( "$addToSet", new Document( "Member", "Employee1")))  
                .wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match,  
                new Document( "$addToSet", new Document( "Member", "Employee2")))  
                .wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match,  
                new Document( "$addToSet", new Document( "Member", "Employee3")))  
                .wasAcknowledged ();
/////////////////////////////////////////////////////
		Document document2 = new Document("MeetingID", 2);
		document2.append("Host", "Employee2");
		document2.append("Date", "0528");
		document2.append("StartTime", "10");
		document2.append("EndTime", "11");
		document2.append("Room", "1003");
		document2.append("Member", array);
		mongoCollectionMeeting.insertOne(document2);
        ///////
		BasicDBObject match2 = new BasicDBObject();
		match2.put( "MeetingID", 2 );

		mongoCollectionMeeting. updateOne( match2,  
                new Document( "$addToSet", new Document( "Member", "Employee2")))  
                .wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match2,  
                new Document( "$addToSet", new Document( "Member", "Employee4")))  
                .wasAcknowledged ();
/////////////////////////////////////////////////////
		Document document3 = new Document("MeetingID", 3);
		document3.append("Host", "Employee5");
		document3.append("Date", "0528");
		document3.append("StartTime", "8");
		document3.append("EndTime", "11");
		document3.append("Room", "1002");
		document3.append("Member", array);
		mongoCollectionMeeting.insertOne(document3);
		///////
		BasicDBObject match3 = new BasicDBObject();
		match3.put( "MeetingID", 3 );
		
		mongoCollectionMeeting. updateOne( match3,  
		new Document( "$addToSet", new Document( "Member", "Employee5")))  
		.wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match3,  
		new Document( "$addToSet", new Document( "Member", "Employee7")))  
		.wasAcknowledged ();
/////////////////////////////////////////////////////
		Document document4 = new Document("MeetingID", 4);
		document4.append("Host", "Employee5");
		document4.append("Date", "0528");
		document4.append("StartTime", "12");
		document4.append("EndTime", "14");
		document4.append("Room", "1002");
		document4.append("Member", array);
		mongoCollectionMeeting.insertOne(document4);
		///////
		BasicDBObject match4 = new BasicDBObject();
		match4.put( "MeetingID", 4 );
		
		mongoCollectionMeeting. updateOne( match4,  
		new Document( "$addToSet", new Document( "Member", "Employee5")))  
		.wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match4,  
		new Document( "$addToSet", new Document( "Member", "Employee6")))  
		.wasAcknowledged ();
/////////////////////////////////////////////////////
		Document document5 = new Document("MeetingID", 5);
		document5.append("Host", "Employee7");
		document5.append("Date", "0528");
		document5.append("StartTime", "13");
		document5.append("EndTime", "14");
		document5.append("Room", "1001");
		document5.append("Member", array);
		mongoCollectionMeeting.insertOne(document5);
		///////
		BasicDBObject match5 = new BasicDBObject();
		match5.put( "MeetingID", 5 );
		
		mongoCollectionMeeting. updateOne( match5,  
		new Document( "$addToSet", new Document( "Member", "Employee2")))  
		.wasAcknowledged ();
		mongoCollectionMeeting. updateOne( match5,  
		new Document( "$addToSet", new Document( "Member", "Employee7")))  
		.wasAcknowledged ();
/////////////////////////////////////////////////////
		Document document6 = new Document("MeetingID", 6);
		document6.append("Host", "Employee1");
		document6.append("Date", "0528");
		document6.append("StartTime", "13");
		document6.append("EndTime", "15");
		document6.append("Room", "1003");
		document6.append("Member", array);
		mongoCollectionMeeting.insertOne(document6);
		///////
		BasicDBObject match6 = new BasicDBObject();
		match6.put( "MeetingID", 6 );
		
		mongoCollectionMeeting. updateOne( match6,  
		new Document( "$addToSet", new Document( "Member", "Employee1")))  
		.wasAcknowledged ();

	}
}