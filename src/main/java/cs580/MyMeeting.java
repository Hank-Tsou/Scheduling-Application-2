package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;
import javax.swing.ImageIcon;

public class MyMeeting {

	private JFrame frmMeetingManagement;
	private DefaultListModel<String> AcceptMeetinglistModel;
	private DefaultListModel<String> MyMeetinglistModel;
	//private DefaultListModel<String> UpdateMeetinglistModel;
	private String LoginUsername; 
	private JList MyMeetinglist;
	private JTextArea MeetingDetail;
	private JList AcceptMeetinglist;
	//private JList UpdateMeetingList;
	private int SelectAcceptMeeting = 0;
	private int IntSelectMyMeeting = 0;
	//private int IntSelectUpdateMeeting = 0;
	
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");

	public MyMeeting(String username) {
		LoginUsername = username;
		initialize();
		setMeetinglist();
		frmMeetingManagement.setLocationRelativeTo(null);
		frmMeetingManagement.setVisible(true);
	}


	private void initialize() {
		String currentDirectory = System.getProperty("user.dir");
		
		frmMeetingManagement = new JFrame();
		frmMeetingManagement.setTitle("Meeting Management");
		frmMeetingManagement.setBounds(100, 100, 545, 325);
		frmMeetingManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMeetingManagement.getContentPane().setLayout(null);
		
		AcceptMeetinglistModel = new DefaultListModel();
		AcceptMeetinglist = new JList(AcceptMeetinglistModel);
		AcceptMeetinglist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(AcceptMeetinglist.getSelectedValue() != null)
				{
					SelectAcceptMeeting = Integer.valueOf((String) AcceptMeetinglist.getSelectedValue());
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", SelectAcceptMeeting )).first();
					if(myMeeting != null)
					{
						MeetingDetail.setText(null);
						String meetingDate1 = getDateWithBackslash(myMeeting.getString("Date"));
						
						MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
											 "Room: " + myMeeting.getString("Room") + "\n" +
											 "Date: " + meetingDate1 + "\n" +
											 "Start time: " + myMeeting.getString("StartTime") + "\n" +
											 "End Time: " + myMeeting.getString("EndTime")  
											);
					}
					else 
					{
						MeetingDetail.setText(null);
						MeetingDetail.append("Meeting has been Canceled");
					}
					
				}
			}
		});
		AcceptMeetinglist.setBounds(24, 51, 143, 166);
		frmMeetingManagement.getContentPane().add(AcceptMeetinglist);
		
		MyMeetinglistModel = new DefaultListModel();
		MyMeetinglist = new JList(MyMeetinglistModel);
		MyMeetinglist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(MyMeetinglist.getSelectedValue() != null)
				{	
					Boolean DeclineMeeting;
					MeetingDetail.setText(null);
					IntSelectMyMeeting = Integer.valueOf((String) MyMeetinglist.getSelectedValue());
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectMyMeeting )).first();
					String meetingDate1 = getDateWithBackslash(myMeeting.getString("Date"));
					MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
										 "Room: " + myMeeting.getString("Room") + "\n" +
										 "Date: " + meetingDate1 + "\n" +
										 "Start time: " + myMeeting.getString("StartTime") + "\n" +
										 "End Time: " + myMeeting.getString("EndTime") + "\n"+
										 "-------------------"+ "\n"+
										 "Member: "+ "\n"
										);
					
					ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
					
					for(int i=0; i<MemberLists.size(); i++)
					{
						DeclineMeeting = true;
						String StringMember = String.valueOf(MemberLists.get(i)); 
						Document myDoc = mongoCollection.find(Filters.eq("Name", StringMember )).first();
					
						List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 			//get meeting list
						int MeetingListSize = MeetingLists.size(); 		

						for(int j=0; j<MeetingListSize; j++)
						{
							//System.out.print(StringMember);
							Document MeetingElement = MeetingLists.get(j);
							String StringRespond = MeetingElement.getString("Respond");
							String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));
							
							if (StringMeetingID.equals(MyMeetinglist.getSelectedValue()))
							{	
								if(StringRespond.equals("A"))
								{
									MeetingDetail.append(StringMember +":	Accept" + "\n");
									DeclineMeeting = false;
								}
								else
								{
									DeclineMeeting = false;
								}
							}
						}
						
						if(DeclineMeeting == true)
						{
							MeetingDetail.append(StringMember +":	Decline" + "\n");
						}
					}
					//Document myDoc =  mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectMyMeeting )).first();
				}
			}
		});
		MyMeetinglist.setBounds(188, 51, 149, 166);
		frmMeetingManagement.getContentPane().add(MyMeetinglist);
		/*
		 * this part move to notification class
		 * here just for record
		 */
		/*
		UpdateMeetinglistModel = new DefaultListModel();
		UpdateMeetingList = new JList(UpdateMeetinglistModel);
		UpdateMeetingList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(UpdateMeetingList.getSelectedValue() != null)
				{
					MeetingDetail.setText(null);
					IntSelectUpdateMeeting = Integer.valueOf((String) UpdateMeetingList.getSelectedValue());
					
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectUpdateMeeting )).first();
					if(myMeeting != null)
					{
						MeetingDetail.append("Host: " + myMeeting.getString("Host") + "\n" +
								 "Room: " + myMeeting.getString("Room") + "\n" +
								 "Date: " + myMeeting.getString("Date") + "\n" +
								 "Start time: " + myMeeting.getString("StartTime") + "\n" +
								 "End Time: " + myMeeting.getString("EndTime")  
								);
		
						JOptionPane.showMessageDialog(frame, "Host: " + myMeeting.getString("Host") + "\n" +
								 "Room: " + myMeeting.getString("Room") + "\n" +
								 "Date: " + myMeeting.getString("Date") + "\n" +
								 "Start time: " + myMeeting.getString("StartTime") + "\n" +
								 "End Time: " + myMeeting.getString("EndTime") );
						
						int isSelected = UpdateMeetingList.getSelectedIndex();
						UpdateMeetinglistModel.remove(isSelected);
				        MeetingDetail.setText(null);
				        
				        UserNoticed();
					}
					else
					{
						mongoCollection.updateOne(  
				                new Document ("Name",LoginUsername),  
				                new Document( "$pull", new Document("Meeting" ,  
				                        new Document( "MeetingID", IntSelectUpdateMeeting))))  
				                .wasAcknowledged ();  
						
						JOptionPane.showMessageDialog(frame, "Meeting"+IntSelectUpdateMeeting+" has been cancel");
						int isSelected = UpdateMeetingList.getSelectedIndex();
						UpdateMeetinglistModel.remove(isSelected);
				        MeetingDetail.setText(null);
						
					}
					
				
				}
			}
		});
		UpdateMeetingList.setBounds(333, 45, 134, 166);
		frame.getContentPane().add(UpdateMeetingList);
		*/
		
		//MeetingDetail = new JTextArea();
		//MeetingDetail.setBounds(333, 59, 289, 166);
		//frame.getContentPane().add(MeetingDetail);
		
		JLabel lblAcceptMeeting = new JLabel("Accept Meeting");
		lblAcceptMeeting.setHorizontalAlignment(SwingConstants.CENTER);
		lblAcceptMeeting.setBounds(31, 18, 135, 21);
		lblAcceptMeeting.setForeground(Color.WHITE);
		lblAcceptMeeting.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingManagement.getContentPane().add(lblAcceptMeeting);
		
		JLabel lblMyMeeting = new JLabel("My Meeting");
		lblMyMeeting.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyMeeting.setBounds(185, 18, 152, 20);
		lblMyMeeting.setForeground(Color.WHITE);
		lblMyMeeting.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingManagement.getContentPane().add(lblMyMeeting);
		
		JLabel lblMeetingDetail = new JLabel("Meeting Detail");
		lblMeetingDetail.setBounds(379, 18, 125, 21);
		lblMeetingDetail.setForeground(Color.WHITE);
		lblMeetingDetail.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingManagement.getContentPane().add(lblMeetingDetail);
		
		JButton btnDecline = new JButton("Decline");
		btnDecline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(SelectAcceptMeeting == 0)
				{
					JOptionPane.showMessageDialog(frmMeetingManagement, "Please select a meeting!");
				}
				else
				{
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", SelectAcceptMeeting )).first();
					if(myMeeting != null)
					{
						mongoCollection.updateOne(  
				                new Document ("Username",LoginUsername),  
				                new Document( "$pull", new Document("Meeting" ,  
				                        new Document( "MeetingID", SelectAcceptMeeting))))  
				                .wasAcknowledged ();  
						/*
						BasicDBObject match = new BasicDBObject();
				        match.put( "Name", LoginUsername );

				        BasicDBObject addressSpec = new BasicDBObject();
				        addressSpec.put("MeetingID", SelectAcceptMeeting);
				        addressSpec.put("Respond", "D");
				        addressSpec.put("Update", "0");

				        BasicDBObject update = new BasicDBObject();
				        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
				        mongoCollection.updateMany( match, update );
				        */
				        int isSelected = AcceptMeetinglist.getSelectedIndex();
				        AcceptMeetinglistModel.remove(isSelected);
				        MeetingDetail.setText(null);
				        
				        JOptionPane.showMessageDialog(frmMeetingManagement, "Meeting DECLINE!");
					}
					else
					{	
						JOptionPane.showMessageDialog(frmMeetingManagement, "Meeting "+SelectAcceptMeeting+" has been cancel");
					}
					
				}
			}
		});
		btnDecline.setBounds(24, 233, 135, 29);
		frmMeetingManagement.getContentPane().add(btnDecline);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProfilePage profile = new ProfilePage(LoginUsername);
				frmMeetingManagement.dispose();
			}
		});
		btnCancel.setBounds(438, 233, 86, 29);
		frmMeetingManagement.getContentPane().add(btnCancel);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMyMeeting!=0)
				{
					MeetingUpdate MeetUp = new MeetingUpdate(LoginUsername, IntSelectMyMeeting);
					frmMeetingManagement.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(frmMeetingManagement, "Pleace select a meeting!");
				}
				
			}
		});
		btnUpdate.setBounds(186, 233, 79, 29);
		frmMeetingManagement.getContentPane().add(btnUpdate);
		
		JButton MeetCancel = new JButton("Delete");
		MeetCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IntSelectMyMeeting == 0)
				{
					JOptionPane.showMessageDialog(frmMeetingManagement, "Pleace select a meeting!");
					
				}
				else
				{
					Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntSelectMyMeeting )).first();
					
					ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
					String previousRoom = myMeeting.getString("Room");
					
					for(int i=0; i<MemberLists.size(); i++)
					{
						String StringMember = String.valueOf(MemberLists.get(i)); 
						
						mongoCollection.updateOne(  
				                new Document ("Name",StringMember),  
				                new Document( "$pull", new Document("Meeting" ,  
				                        new Document( "MeetingID", IntSelectMyMeeting))))  
				                .wasAcknowledged (); 
						
					
						 BasicDBObject matchEmployee = new BasicDBObject();
				 		 matchEmployee.put( "Name", StringMember);
		
					     BasicDBObject Employee_addressSpec = new BasicDBObject();
					     Employee_addressSpec.put("MeetingID", IntSelectMyMeeting);
					     Employee_addressSpec.put("Respond", "X");
					     Employee_addressSpec.put("Update", "1");
					
					     BasicDBObject updateEmployee = new BasicDBObject();
					     updateEmployee.put( "$push", new BasicDBObject( "Meeting", Employee_addressSpec ) );
					     mongoCollection.updateMany( matchEmployee, updateEmployee );
					     
					}

					 int isSelected = MyMeetinglist.getSelectedIndex();
					 MyMeetinglistModel.remove(isSelected);
			         MeetingDetail.setText(null);
			         
			         Bson filter = new Document("MeetingID", IntSelectMyMeeting);
					    mongoCollectionMeeting.deleteOne(filter);
					 
				    mongoCollectionRooms.updateOne(  
			                new Document ("RoomNo",previousRoom),  
			                new Document( "$pull", new Document("TimeBooked" ,  
			                        new Document( "MeetingID", IntSelectMyMeeting))))  
			                .wasAcknowledged ();  
			         
			        
				    JOptionPane.showMessageDialog(frmMeetingManagement, "Meeting Delete! Room reservation cancel!");
				}

			}
		});
		MeetCancel.setBounds(259, 233, 79, 29);
		frmMeetingManagement.getContentPane().add(MeetCancel);
		
		JScrollPane MeetingDetailS = new JScrollPane();
		MeetingDetailS.setBounds(352, 51, 172, 166);
		frmMeetingManagement.getContentPane().add(MeetingDetailS);
		
		MeetingDetail = new JTextArea();
		MeetingDetailS.setViewportView(MeetingDetail);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 545, 291);
		frmMeetingManagement.getContentPane().add(lblNewLabel);
	}
	
	private void setMeetinglist()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		String RealName = myDoc.getString("Name");
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));  
			
			Integer IntMeetingID = Integer.valueOf(StringMeetingID);
			Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();

			if(MeetingElement.get("Respond").equals("A") && !myMeeting.getString("Host").equals(RealName))
			{
				AcceptMeetinglistModel.addElement(StringMeetingID);
			}
			
			if(myMeeting!=null)
			{
				if(myMeeting.getString("Host").equals(RealName))
				{
					MyMeetinglistModel.addElement(StringMeetingID);
				}
			}
		}	
	}
	
	private String getDateWithBackslash(String meetingDate)
	{
		String meetingDate1 = meetingDate;
		if (meetingDate1.length() == 4)
		{
			meetingDate1 = meetingDate1.substring(0, 2) + "/" + meetingDate1.substring(2, meetingDate1.length());
		}
		else if (meetingDate1.length() == 3)
		{
			meetingDate1 = meetingDate1.substring(0, 1) + "/" + meetingDate1.substring(1, meetingDate1.length());
		}
		
		return meetingDate1;
	}
}
