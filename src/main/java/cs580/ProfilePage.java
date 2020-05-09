package cs580;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.UIManager;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JRadioButton;

public class ProfilePage {

	private JFrame frame;
	private String LoginUsername;
	private boolean NewMeeting = false;
	
	private JRadioButton[] radioButton;
	private JPanel contentPane;
	private JRadioButton rdbtnUnavailable;
	private JRadioButton rdbtnAvailable;
	private Object University;
	private Object Department;
	private Object Degree;
	private Object Byear;
	

//////Database Setup /////////////////////////////////////////////////////////////////////////
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:Hank82903*@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////////////

	
	public ProfilePage(String username){
		LoginUsername = username;
		initialize();
		setAvailable();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Home Page");
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(255, 32, 137, 161);
		ImageIcon MyImage = new ImageIcon(currentDirectory + "/image/"+ LoginUsername + ".jpg");
		Image img = MyImage.getImage();
		Image NewImg = img.getScaledInstance(lblNewLabel.getWidth(), lblNewLabel.getHeight(), Image.SCALE_SMOOTH);  
		ImageIcon image = new ImageIcon(NewImg);
		lblNewLabel.setIcon(image);
		frame.getContentPane().add(lblNewLabel);
		 
		Document myMeeting = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
		University = myMeeting.get("University") ;
		Department = myMeeting.get("Department");
		Byear = myMeeting.get("Byear") ;
		Degree = myMeeting.get("Degree");
		
		JTextPane txtpnCaliforniaState = new JTextPane();
		txtpnCaliforniaState.setText("             "+LoginUsername + "\n\n         "+University+"\n          "
				+Degree+"\n        "+Department+"\n                  "+Byear);
		txtpnCaliforniaState.setBounds(236, 205, 177, 106);
		frame.getContentPane().add(txtpnCaliforniaState);
		
		JPanel panel = new JPanel();
		panel.setBounds(20, 37, 131, 261);
		panel.setBackground(new Color(0,0,0,0));
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(5, 1, 0, 10));
		
//////// Button ////////////////////////////////////////////////////////
		JButton btnCalendar = new JButton("Calendar");
		btnCalendar.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCalendar);
		btnCalendar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PersonalCalendar perCal = new PersonalCalendar(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////
		JButton btnMeeting = new JButton("My Meeting");
		btnMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnMeeting);
		btnMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JOptionPane.showMessageDialog(null, "Show Meeting List");
				MyMeeting Mymet = new MyMeeting(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////		
		JButton btnNotification = new JButton("<html><center>Notification Center</center></html>");
		btnNotification.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnNotification);
		btnNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Document myStatus = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
				List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
				
				for(int i=0; i<meetingRes.size(); i++)
				{
					Document MeetingElement = meetingRes.get(i);
					String StringRespond = MeetingElement.getString("Respond");
					String StringUpdate = MeetingElement.getString("Update");
					if(StringRespond.equals("P")||StringUpdate.equals("1")){
						NewMeeting = true;
					}
				}
				
				if(NewMeeting == true){
					Notification Notice = new Notification(LoginUsername);
					frame.dispose();
				}
				
				else{
					JOptionPane.showMessageDialog(frame, "No New Notification!");
				}
				
			}
		});
//////////////////////////////////
		JButton btnCreateMeeting = new JButton();
		btnCreateMeeting.setText("<html><center>Create Meeting</center></html>");
		btnCreateMeeting.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnCreateMeeting);
		btnCreateMeeting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemberSelection memslct = new MemberSelection(LoginUsername);
				frame.dispose();
			}
		});
//////////////////////////////////
		JButton btnLogout = new JButton("Logout");
		btnLogout.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(btnLogout);
		
		btnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage login = new LoginPage();
				frame.dispose();
			}
		});
		
		rdbtnAvailable = new JRadioButton("Available");
		rdbtnAvailable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bson filter = new Document("Username", LoginUsername);
				Bson newValue = new Document("Availability", "Available");
				Bson updateOperationDocument = new Document("$set", newValue);
				mongoCollection.updateOne(filter, updateOperationDocument);
				
				rdbtnUnavailable.setSelected(false);
			}
		});
		rdbtnAvailable.setBounds(404, 6, 89, 23);
		frame.getContentPane().add(rdbtnAvailable);
		
		rdbtnUnavailable = new JRadioButton("Busy");
		rdbtnUnavailable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Bson filter = new Document("Username", LoginUsername);
				Bson newValue = new Document("Availability", "Unavailable");
				Bson updateOperationDocument = new Document("$set", newValue);
				mongoCollection.updateOne(filter, updateOperationDocument);
				
				rdbtnAvailable.setSelected(false);
			}
		});
		rdbtnUnavailable.setBounds(404, 26, 89, 23);
		frame.getContentPane().add(rdbtnUnavailable);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(currentDirectory + "/image/calendarB2.jpg"));
		lblNewLabel_1.setBounds(-30, 0, 199, 328);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel ImgBackground = new JLabel("");
		ImgBackground.setBounds(248, 26, 150, 174);
		ImgBackground.setOpaque(true);
		ImgBackground.setBackground(new Color(255, 255, 224));
		frame.getContentPane().add(ImgBackground);
		
		
//////////////////////////////////	
	}
	
	private void setAvailable()
	{
		Document myStatus = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
		if(myStatus.getString("Availability").equals("Available"))
		{
			rdbtnAvailable.setSelected(true);
		}
		else
		{
			rdbtnUnavailable.setSelected(true);
		}
	}
}
