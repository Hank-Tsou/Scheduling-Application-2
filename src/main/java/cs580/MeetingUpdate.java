package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JLabel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class MeetingUpdate {

	private DefaultListModel<String> listModelInvitee = new DefaultListModel<String>();
	
	private JFrame frmMeetingUpdate; //private JFrame frame;
	private String LoginUsername;
	private int MeetingID;
	private Document myMeeting;
	
	private JTextArea Datetxt;
	private JTextArea Timetxt;
	private JTextArea Roomtxt;
	private JTextArea Memtxt;
	
	private Boolean ExistMeeting = true;

//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");

	
	public MeetingUpdate(String username, int meetingid) {
		LoginUsername = username;
		MeetingID = meetingid;
		initialize();
		setUpMeetingDetail();
		frmMeetingUpdate.setLocationRelativeTo(null);
		frmMeetingUpdate.setVisible(true);
	}


	private void initialize() {
		
		String currentDirectory = System.getProperty("user.dir");
		
		frmMeetingUpdate = new JFrame();
		frmMeetingUpdate.setTitle("Meeting Update");
		frmMeetingUpdate.setBounds(100, 100, 469, 356);
		frmMeetingUpdate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmMeetingUpdate.getContentPane().setLayout(null);
		
		Datetxt = new JTextArea();
		Datetxt.setBounds(170, 53, 125, 32);
		frmMeetingUpdate.getContentPane().add(Datetxt); 
		
		Timetxt = new JTextArea();
		Timetxt.setBounds(170, 97, 125, 32);
		frmMeetingUpdate.getContentPane().add(Timetxt);
		
		Roomtxt = new JTextArea();
		Roomtxt.setBounds(170, 145, 125, 32);
		frmMeetingUpdate.getContentPane().add(Roomtxt); 
		
		Memtxt = new JTextArea();
		Memtxt.setBounds(170, 189, 125, 105);
		frmMeetingUpdate.getContentPane().add(Memtxt); 
		
		JLabel lblDate = new JLabel("  Date");
		lblDate.setBounds(107, 56, 61, 29);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingUpdate.getContentPane().add(lblDate);
		
		JLabel lblTime = new JLabel(" Time");
		lblTime.setBounds(107, 97, 55, 32);
		lblTime.setForeground(Color.WHITE);
		lblTime.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingUpdate.getContentPane().add(lblTime);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(107, 145, 61, 32);
		lblRoom.setForeground(Color.WHITE);
		lblRoom.setFont(new Font("Dialog", Font.BOLD, 16));
		frmMeetingUpdate.getContentPane().add(lblRoom);
		
		JLabel lblMember = new JLabel("Member");
		lblMember.setForeground(Color.WHITE);
		lblMember.setFont(new Font("Dialog", Font.BOLD, 16));
		lblMember.setBounds(87, 228, 81, 16);
		frmMeetingUpdate.getContentPane().add(lblMember);
		
		JButton btnDateEdit = new JButton("Edit");
		btnDateEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ScheduleCalendar(listModelInvitee, LoginUsername, ExistMeeting, MeetingID);
				frmMeetingUpdate.dispose();
			}
		});
		btnDateEdit.setBounds(305, 95, 75, 29);
		frmMeetingUpdate.getContentPane().add(btnDateEdit);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(307, 224, 75, 29);
		frmMeetingUpdate.getContentPane().add(btnAdd);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddMember AdMem = new AddMember(LoginUsername, MeetingID, ExistMeeting);
				frmMeetingUpdate.dispose();
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MyMeeting(LoginUsername);
				frmMeetingUpdate.dispose();
			}
		});
		btnCancel.setBounds(357, 277, 86, 29);
		frmMeetingUpdate.getContentPane().add(btnCancel);
		
		JLabel lblMeetingUpdate = new JLabel("Meeting Update");
		lblMeetingUpdate.setBounds(140, 9, 189, 32);
		lblMeetingUpdate.setForeground(Color.WHITE);
		lblMeetingUpdate.setFont(new Font("Dialog", Font.BOLD, 23));
		frmMeetingUpdate.getContentPane().add(lblMeetingUpdate);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 469, 325);
		frmMeetingUpdate.getContentPane().add(lblNewLabel);
	}
	
	private void setUpMeetingDetail()
	{
		myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", MeetingID )).first();    //get member
		
		int IntDate = Integer.parseInt(myMeeting.getString("Date"));
		String StringDate = " 2018 / "+IntDate/100 + " / " + IntDate%100;
		Datetxt.append(StringDate);
		Timetxt.append(" "+myMeeting.getString("StartTime") + ":00 - " + myMeeting.getString("EndTime")+ ":00" );
		Roomtxt.append(" "+myMeeting.getString("Room"));
		
		ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
		
		for(int i=0; i<MemberLists.size(); i++)
		{
			String StringMember = String.valueOf(MemberLists.get(i)); 
			Memtxt.append(" "+StringMember+"\n");
			listModelInvitee.addElement(StringMember);
		}
		
	}
}
