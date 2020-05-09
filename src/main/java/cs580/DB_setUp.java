package cs580;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import javax.swing.ImageIcon;

public class DB_setUp {

	private JFrame frame;
	private int currentMonth = LocalDate.now().getMonthValue();
	private int currentDay = LocalDate.now().getDayOfMonth();
	private int currentDate = currentMonth*100 + currentDay;
	
/////Database Setup /////////////////////////////////////////////////////////////////////////
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:Hank82903*@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////////////

	public DB_setUp() {
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		RemoveExpireMeeting();
		
		
		// five second is for database setup
		try {
			Thread.sleep(2000);
			LoginPage login = new LoginPage();
			frame.dispose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
	private void initialize() {
		String currentDirectory = System.getProperty("user.dir");
		
		frame = new JFrame();
		frame.setBounds(100, 100, 330, 150);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblProgram = new JLabel("Scheduling CS580");
		lblProgram.setBounds(6, 31, 318, 64);
		lblProgram.setForeground(Color.WHITE);
		lblProgram.setFont(new Font("Dialog", Font.BOLD, 33));
		frame.getContentPane().add(lblProgram);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, -130, 404, 258);
		frame.getContentPane().add(lblNewLabel);
		
	}
	
	private void RemoveExpireMeeting()
	{

		FindIterable<Document> findIterableMeeting = mongoCollectionMeeting.find();
        MongoCursor<Document> mongoCursorMeeting = findIterableMeeting.iterator();  
        while(mongoCursorMeeting.hasNext()){
        	
        	Document Meeting = (Document) mongoCursorMeeting.next(); 
    		List<Document> Member = (List<Document>) Meeting.get("Member"); 

        String StringDate = String.valueOf(Meeting.get("Date")); 
        int IntDate = Integer.parseInt(StringDate);
        
        String RoomNo = Meeting.getString("Room");

			if(IntDate < currentDate)
			{
				String StringMeetingID = String.valueOf(Meeting.get("MeetingID")); 
			    int IntSelectMyMeeting = Integer.parseInt(StringMeetingID);
			    
				for(int i=0; i<Member.size(); i++)
				{
					String StringMember = String.valueOf(Member.get(i)); 
					//System.out.print(StringMember+" "+IntSelectMyMeeting+"\n");
					mongoCollection.updateOne(  
			                new Document ("Name",StringMember),  
			                new Document( "$pull", new Document("Meeting" ,  
			                        new Document( "MeetingID", IntSelectMyMeeting))))  
			                .wasAcknowledged ();    
				}
				
				mongoCollectionRooms.updateOne(  
		                new Document ("RoomNo",RoomNo),  
		                new Document( "$pull", new Document("TimeBooked" ,  
		                        new Document( "MeetingID", IntSelectMyMeeting))))  
		                .wasAcknowledged ();
				
				Bson filter = new Document("MeetingID", IntSelectMyMeeting);
			    mongoCollectionMeeting.deleteOne(filter);
			}
        }
	}
}
