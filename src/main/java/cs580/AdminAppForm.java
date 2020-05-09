package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;

public class AdminAppForm {

	private JFrame frame;
	private JList Employeelist;
	private JTextArea AppForm;
	
	private ArrayList<String> employees = new ArrayList<String>();
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	
	
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");

	public AdminAppForm() {
		initialize();
		SetForm();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 325);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblApplicationForm = new JLabel("APPLICATION FORM");
		lblApplicationForm.setBounds(211, 16, 170, 16);
		lblApplicationForm.setForeground(Color.WHITE);
		lblApplicationForm.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblApplicationForm);
		
		AppForm = new JTextArea();
		AppForm.setBounds(160, 45, 256, 185);
		frame.getContentPane().add(AppForm);
		
		JButton btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Employeelist.getSelectedValue() != null)
				{
					String StringEmpName = String.valueOf(Employeelist.getSelectedValue());  
					Document myMeeting = mongoCollectionAdmin.find(Filters.eq("Name", StringEmpName )).first();
					
					Document myDoc = (Document)mongoCollection.find().sort(new BasicDBObject("EID",-1)).first();
					int EID = myDoc.getInteger("EID")+1;
					
					ArrayList< DBObject > array = new ArrayList< DBObject >();
					Document document = new Document("EID", EID);
					document.append("Name", myMeeting.getString("Name"));
					document.append("Availability", "Available");
					document.append("Username", myMeeting.getString("Username"));
					document.append("Password", myMeeting.getString("Password"));
					
					document.append("University", myMeeting.getString("University"));
					document.append("Department", myMeeting.getString("Department"));
					document.append("Degree", myMeeting.getString("Degree"));
					document.append("Byear", myMeeting.getString("Byear"));
					
					document.append("Meeting", array);
					mongoCollection.insertOne(document);
					
					Bson filter = new Document("Name", StringEmpName);
			        mongoCollectionAdmin.deleteOne(filter);
			        
			        listModelEmployee.removeElement(StringEmpName);
			        
			        JOptionPane.showMessageDialog(frame, "Employee added!");
			        AppForm.setText(null);
				}
				
				
			}
		});
		btnAccept.setBounds(197, 242, 87, 29);
		frame.getContentPane().add(btnAccept);
		
		JButton btnReject = new JButton("Reject");
		btnReject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Employeelist.getSelectedValue() != null)
				{
					String StringEmpName = String.valueOf(Employeelist.getSelectedValue());  
					
					 Bson filter = new Document("Name", StringEmpName);
				        mongoCollectionAdmin.deleteOne(filter);
				        
				     listModelEmployee.removeElement(StringEmpName);
				     
				     JOptionPane.showMessageDialog(frame, "Application Rejected!");
				     AppForm.setText(null);
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Please select an employee!");
				}
			}
		});
		btnReject.setBounds(294, 242, 87, 29);
		frame.getContentPane().add(btnReject);
		
		Employeelist = new JList(listModelEmployee);
		Employeelist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(Employeelist.getSelectedValue() != null)
				{	
					AppForm.setText(null);
					String StringEmpName = String.valueOf(Employeelist.getSelectedValue());  
					Document myMeeting = mongoCollectionAdmin.find(Filters.eq("Name", StringEmpName )).first();
				
					AppForm.append("Applicant ID:	" + myMeeting.getInteger("EID") + "\n\n" +
							 "Name:	" + myMeeting.getString("Name") + "\n" +
							 "Username:	" + myMeeting.getString("Username") + "\n\n" +
							 "University:	" + myMeeting.getString("University") + "\n" +
							 "Department:	" + myMeeting.getString("Department") + "\n"+ "\n" +
							 "Degree:	"+ myMeeting.getString("Degree") + "\n"+
							 "Byear:	" + myMeeting.getString("Byear") + "\n"
							);
				}
				else
				{
					JOptionPane.showMessageDialog(frame, "Please select an employee!");
				}
			}
		});
		Employeelist.setBounds(26, 79, 111, 151);
		frame.getContentPane().add(Employeelist);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AdminPage myAdmin = new AdminPage();
				frame.dispose();
			}
		});
		btnBack.setBounds(375, 271, 75, 29);
		frame.getContentPane().add(btnBack);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblApplicant = new JLabel("Applicant");
		lblApplicant.setBounds(41, 51, 82, 16);
		lblApplicant.setForeground(Color.WHITE);
		lblApplicant.setFont(new Font("Dialog", Font.BOLD, 16));
		frame.getContentPane().add(lblApplicant);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 450, 303);
		frame.getContentPane().add(lblNewLabel);
	}
	
	private void SetForm()
	{
		MongoCursor<Document> cursor = mongoCollectionAdmin.find().iterator();
		try {
		      while (cursor.hasNext()) {
		        Document doc = cursor.next();
		        	employees.add(doc.get("Name").toString());
		      }
		    } finally {
		      cursor.close();
		    }
		
		for(String s : employees)
		{
			listModelEmployee.addElement(s);
		}
		
	}
}
