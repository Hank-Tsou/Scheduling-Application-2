package cs580;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;

public class ApplicationForm {

	private JFrame frame;
	private JTextField txtEployeeName;
	private JTextField txtUserName;
	private JTextField txtPassword;
	
	private JComboBox CBoxDepartment ;
	private JComboBox CBoxDegree;
	private JComboBox CBoxByear;

	private String EmployeeName="";
	private String EmployeePassword="";
	private String EmployeeUserName="";
	private String ByearVal="";
	private String UniversityName="";
	private String DepartmentVal = "";
	private String DegreeVal = "";
	
	private Boolean FromEmployee;
	private Boolean FromAdmin;
	
	String[] Department={"Biological Sciences","Chemistry","Computer Science", "Geological Sciences", "Mathematics & Statistics", "Physics & Astronomy", "Health Promotion "};
	String[] Degree={"Bachelar Student", "Master Student", "PHD"};
	String[] Byear={"1980","1981","1982","1983","1984","1985","1986","1987","1988","1989","1990","1991","1992","1993","1994","1995","1996","1997","1998","1999","2000"};
	
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	
	private JTextField textUniversity;
	
	public ApplicationForm(Boolean fromemployee, Boolean fromadmin) {
		FromEmployee = fromemployee;
		FromAdmin = fromadmin;
		
		initialize();
		
		Document myDoc = (Document)mongoCollectionAdmin.find().sort(new BasicDBObject("EID",-1)).first();
		//int EID = myDoc.getInteger("EID")+1;
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 320, 410);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblName = new JLabel("Name: ");
		lblName.setBounds(73, 57, 49, 16);
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblName);
		
		JLabel lblUserName = new JLabel("User Name: ");
		lblUserName.setBounds(35, 96, 87, 16);
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblUserName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(45, 135, 72, 16);
		lblPassword.setForeground(Color.WHITE);
		lblPassword.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblPassword);
		
		txtEployeeName = new JTextField();
		txtEployeeName.setBounds(124, 53, 170, 27);
		frame.getContentPane().add(txtEployeeName);
		txtEployeeName.setColumns(10);
		
		txtUserName = new JTextField();
		txtUserName.setBounds(124, 92, 170, 27);
		frame.getContentPane().add(txtUserName);
		txtUserName.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(124, 131, 170, 27);
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnConfirm = new JButton("Submit");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean UserNameUsed = false;
				 
				EmployeeUserName = txtUserName.getText();
				EmployeePassword = txtPassword.getText();
				EmployeeName = txtEployeeName.getText();
				UniversityName = textUniversity.getText();
				DepartmentVal = (String)CBoxDepartment.getSelectedItem();
				DegreeVal = (String)CBoxDegree.getSelectedItem();
				ByearVal = (String)CBoxByear.getSelectedItem();
				
				if(EmployeeUserName.equals("")||EmployeePassword.equals("")||EmployeeName.equals("")||
						ByearVal==null||UniversityName.equals("")||DepartmentVal==null||DegreeVal==null)
				{
					JOptionPane.showMessageDialog(frame, "Please input all the information!");
				}
				else 
				{
					//if()
						
					FindIterable<Document> findIterable = mongoCollection.find();
			        MongoCursor<Document> mongoCursor = findIterable.iterator();  
			        while(mongoCursor.hasNext()){
			        		Document doc = mongoCursor.next();
			        		if(doc.getString("Username").equals(EmployeeUserName))
			        		{
			        			JOptionPane.showMessageDialog(frame, "User name has been used!");
			        			UserNameUsed = true;
			        			txtUserName.setText(null);
			        			break;
			        		}
			        } 
			        
			        if(UserNameUsed == false && FromEmployee == true)
					{
						Document myDoc = (Document)mongoCollectionAdmin.find().sort(new BasicDBObject("EID",-1)).first();
						int EID;
						
						if(myDoc == null){
							EID = 1;
						}
						else{
							EID = myDoc.getInteger("EID")+1;
						}
						
						ArrayList< DBObject > array = new ArrayList< DBObject >();
						Document document = new Document("EID", EID);
						document.append("Name", EmployeeName);
						document.append("Availability", "Available");
						document.append("Username", EmployeeUserName);
						document.append("Password", EmployeePassword);
						
						document.append("University", UniversityName);
						document.append("Department", DepartmentVal);
						document.append("Degree", DegreeVal);
						document.append("Byear", ByearVal);
						
						mongoCollectionAdmin.insertOne(document);
						
						JOptionPane.showMessageDialog(frame, "SUCCESSFUL SUBMITTED!");
						frame.dispose();
					}
			        
			        else if(UserNameUsed == false && FromAdmin == true)
					{
			        	Document myDoc = (Document)mongoCollection.find().sort(new BasicDBObject("EID",-1)).first();
						int EID;
						
						if(myDoc == null){
							EID = 1;
						}
						else{
							EID = myDoc.getInteger("EID")+1;
						}
						
						ArrayList< DBObject > array = new ArrayList< DBObject >();
						Document document = new Document("EID", EID);
						document.append("Name", EmployeeName);
						document.append("Availability", "Available");
						document.append("Username", EmployeeUserName);
						document.append("Password", EmployeePassword);
						
						document.append("University", UniversityName);
						document.append("Department", DepartmentVal);
						document.append("Degree", DegreeVal);
						document.append("Byear", ByearVal);
						document.append("Meeting", array);
						
						mongoCollection.insertOne(document);
						
						JOptionPane.showMessageDialog(frame, "SUCCESSFUL SUBMITTED!");
						AdminPage mytest = new AdminPage();
						frame.dispose();
					}
				}
				

			}
		});
		btnConfirm.setBounds(124, 330, 80, 30);
		frame.getContentPane().add(btnConfirm);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(FromEmployee == true)
				{
					LoginPage login = new LoginPage();
				}
				if(FromAdmin == true)
				{
					AdminPage mytest = new AdminPage();
				}
				
				frame.dispose();
			}
		});
		btnCancel.setBounds(214, 330, 80, 30);
		frame.getContentPane().add(btnCancel);
		
		textUniversity = new JTextField();
		textUniversity.setText("Cal Poly Pomona");
		textUniversity.setBounds(124, 169, 170, 27);
		frame.getContentPane().add(textUniversity);
		textUniversity.setColumns(10);
		
		JLabel lblUniversity = new JLabel("University: ");
		lblUniversity.setBounds(40, 173, 82, 16);
		lblUniversity.setForeground(Color.WHITE);
		lblUniversity.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblUniversity);
		
		JLabel lblDepartment = new JLabel("Department:");
		lblDepartment.setBounds(28, 210, 94, 16);
		lblDepartment.setForeground(Color.WHITE);
		lblDepartment.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblDepartment);
		
		JLabel lblDegree = new JLabel("Degree:");
		lblDegree.setBounds(63, 238, 59, 39);
		lblDegree.setForeground(Color.WHITE);
		lblDegree.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblDegree);
		
		JLabel lblBday = new JLabel("B Year:");
		lblBday.setBounds(68, 289, 58, 16);
		lblBday.setForeground(Color.WHITE);
		lblBday.setFont(new Font("Dialog", Font.BOLD, 14));
		frame.getContentPane().add(lblBday);
		
		CBoxDepartment = new JComboBox(Department);
		CBoxDepartment.setBounds(124, 207, 170, 27);
		CBoxDepartment.setSelectedItem(null);
		frame.getContentPane().add(CBoxDepartment);
		
		CBoxDegree = new JComboBox(Degree);
		CBoxDegree.setBounds(124, 246, 170, 27);
		CBoxDegree.setSelectedItem(null);
		frame.getContentPane().add(CBoxDegree);
		
		JLabel lblApplicationForm = new JLabel("APPLICATION FORM");
		lblApplicationForm.setBounds(63, 6, 206, 39);
		lblApplicationForm.setForeground(Color.WHITE);
		lblApplicationForm.setFont(new Font("Dialog", Font.BOLD, 20));
		frame.getContentPane().add(lblApplicationForm);
		
		CBoxByear = new JComboBox(Byear);
		CBoxByear.setBounds(124, 286, 170, 27);
		CBoxByear.setSelectedItem(null);
		frame.getContentPane().add(CBoxByear);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 320, 388);
		frame.getContentPane().add(lblNewLabel);
	}
}
