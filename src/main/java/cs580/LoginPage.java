package cs580;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.JPasswordField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.FlowLayout;

public class LoginPage {
	
////// Database Setup ////////////////////////////////////////////////////////////////
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:Hank82903*@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");
///////////////////////////////////////////////////////////////////////////////////////
	
	private JFrame frame;
	private JFrame frmLoginPage;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private String password;
	private String username;
	
	private int MeetingDay;
	private int currentDay = LocalDate.now().getDayOfMonth();
	
	String currentDirectory = System.getProperty("user.dir");

	
	public LoginPage() {
		App.setFonts();
		initialize();
		frmLoginPage.setLocationRelativeTo(null);
		frmLoginPage.setVisible(true);
	}

	private void initialize() {
		
		String currentDirectory = System.getProperty("user.dir");
		
		frmLoginPage = new JFrame();
		//frmLoginPage.getContentPane().setBackground( Color.cyan );
		frmLoginPage.setTitle("Login Page");
		frmLoginPage.setBounds(200, 200, 450, 330);
		frmLoginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginPage.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Login");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 30));
		lblNewLabel.setBounds(165, 29, 96, 37);
		frmLoginPage.getContentPane().add(lblNewLabel);
		
		JLabel lblUsername = new JLabel("User Name");
		lblUsername.setForeground(Color.WHITE);
		lblUsername.setFont(new Font("Dialog", Font.BOLD, 16));
		lblUsername.setBounds(93, 94, 88, 21);
		frmLoginPage.getContentPane().add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 16));
		lblPassword.setBounds(102, 144, 79, 32);
		frmLoginPage.getContentPane().add(lblPassword);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(193, 92, 130, 26);
		frmLoginPage.getContentPane().add(txtUsername);
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(193, 148, 130, 26);
		frmLoginPage.getContentPane().add(txtPassword);
		
////////Button ////////////////////////////////////////////////////////		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(193, 192, 130, 32);
		frmLoginPage.getContentPane().add(btnLogin);
		frmLoginPage.getRootPane().setDefaultButton(btnLogin); // Make enter key press login by default
		//////////////////////////////////		
				JButton btnForgotPassword = new JButton("Forgot");
				btnForgotPassword.setBounds(333, 149, 86, 29);
				frmLoginPage.getContentPane().add(btnForgotPassword);
				//////////////////////////////////		
						JButton btnReset = new JButton("Register");
						btnReset.setBounds(324, 243, 95, 29);
						frmLoginPage.getContentPane().add(btnReset);
						
						JLabel lblNewLabel_1 = new JLabel("");
						lblNewLabel_1.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
						lblNewLabel_1.setBounds(-29, 0, 479, 322);
						frmLoginPage.getContentPane().add(lblNewLabel_1);
						btnReset.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								Boolean FromEmployee = true;
								Boolean FromAdmin = false;
								ApplicationForm Apply= new ApplicationForm(FromEmployee, FromAdmin);
								frmLoginPage.dispose();
							}
						});
				btnForgotPassword.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "Pleace Contact Admin to Reset Your Password");
					}
				});
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				password = String.valueOf(txtPassword.getPassword());
				username = txtUsername.getText();
				
				try
				{
					if(username.equals("Admin"))
					{
						if(password.equals("0000"))
						{
							frmLoginPage.dispose();
							NewApplication();
							AdminPage Admin = new AdminPage();
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Login Error");
							txtPassword.setText(null);
							txtUsername.setText(null);
						}
					}
					else
					{
						Document myDoc = mongoCollection.find(Filters.eq("Username", username )).first();
						if(myDoc.get("Password").equals(password))
						{
							frmLoginPage.dispose();
							MeetingNotification();
							ExpireNotification();
							ProfilePage profile = new ProfilePage(username);
							
						}
						else
						{
							JOptionPane.showMessageDialog(null, "Login Error");
							txtPassword.setText(null);
							txtUsername.setText(null);
						}
					}
					
				}
				catch(Exception l) 
				{
					JOptionPane.showMessageDialog(null, "Login Error");
					txtPassword.setText(null);
					txtUsername.setText(null);
				}
				
			}
		});
//////////////////////////////////
		
	}
	
	

	private void MeetingNotification()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Username", username )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringRespond = MeetingElement.getString("Respond");
			String StringUpdate = MeetingElement.getString("Update");

			if(StringRespond.equals("P") || StringUpdate.equals("1")){
				JOptionPane.showMessageDialog(frame, "You have a Notification, Please go to Notification center!");
				break;
			}
		}
	}
	
	private void ExpireNotification()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Username", username )).first();    //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 					//get meeting list
		int MeetingListSize = MeetingLists.size(); 											//get meeting list size
		
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			String StringRespond = String.valueOf(MeetingElement.get("Respond"));  
			if(StringRespond.equals("P"))
			{
				String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));  
				Integer IntMeetingID = Integer.valueOf(StringMeetingID);
				
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first(); 
				
				if(myMeeting != null)
				{
					int Date = Integer.valueOf((String) myMeeting.get("Date"));
					MeetingDay = Date % 100;
					
					if(MeetingDay - currentDay >0 && MeetingDay - currentDay == 3){
						JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 3 days!");
						break;
					}
					else if(MeetingDay - currentDay >0 && MeetingDay - currentDay == 2){
						JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 2 days!");
						break;
					}
					else if(MeetingDay - currentDay >0 && MeetingDay - currentDay == 1){
						JOptionPane.showMessageDialog(frame, "You have a Meeting Expire in 1 days!");
						break;
					}
				}
			}
			else
			{
				String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));  
				Integer IntMeetingID = Integer.valueOf(StringMeetingID);
				
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first(); 
				
				if(myMeeting != null)
				{
					int Date = Integer.valueOf((String) myMeeting.get("Date"));
					MeetingDay = Date % 100;
					
					if(MeetingDay - currentDay >0 && MeetingDay - currentDay == 1){
						JOptionPane.showMessageDialog(frame, "You have a Meeting tomorrow!");
						break;
					}
				}
			}
		}
	}
	
	private void NewApplication()
	{
		if(mongoCollectionAdmin.count()!=0)
		{
			JOptionPane.showMessageDialog(frame, "You have new application!");
		}
		
	}
	
}
