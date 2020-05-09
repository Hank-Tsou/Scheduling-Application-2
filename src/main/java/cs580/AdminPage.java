package cs580;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;

public class AdminPage {

	private JFrame frmAdminProfilePage;
	private JList EmployeeList;
	private JList RoomList;
	private JLabel lblListEmployee;
	private ArrayList<String> employees = new ArrayList<String>();
	private ArrayList<String> rooms = new ArrayList<String>();
	
	private String SelectedEmployee;
	private int EmployeeSelected;
	
	private JList<String> listEmployee;
	private JList<String> listRoom;
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelRoom = new DefaultListModel<String>();
	
//////Database Setup ////////////////////////////////////////////////////////////////	
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";

	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionRooms = mongoDatabase.getCollection("Rooms");
	MongoCollection<Document> mongoCollectionAdmin = mongoDatabase.getCollection("AdminUse");
///////////////////////////////////////////////////////////////////////////////////////

	public AdminPage() {
		initModel();
		initialize();
		frmAdminProfilePage.setLocationRelativeTo(null);
		frmAdminProfilePage.setVisible(true);
		
	}

	private void initialize() {

		EmployeeList = new JList<String>(listModelEmployee);
		RoomList = new JList<String>(listModelRoom);
		
		frmAdminProfilePage = new JFrame();
		frmAdminProfilePage.setTitle("Admin Profile Page");
		frmAdminProfilePage.setBounds(100, 100, 450, 340);
		frmAdminProfilePage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAdminProfilePage.getContentPane().setLayout(null);
		
		JScrollPane scrollPane_Emp = new JScrollPane(EmployeeList);
		scrollPane_Emp.setBounds(45, 45, 164, 165);
		frmAdminProfilePage.getContentPane().add(scrollPane_Emp);
		
		JButton btnAddEmployee = new JButton("Add");
		btnAddEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Boolean FromEmployee = false;
				Boolean FromAdmin = true;
				ApplicationForm Apply= new ApplicationForm(FromEmployee, FromAdmin);
				frmAdminProfilePage.dispose();
			}
		});
		btnAddEmployee.setBounds(55, 215, 70, 29);
		frmAdminProfilePage.getContentPane().add(btnAddEmployee);
		
		JButton btnDeleteEmployee = new JButton("Delete");
		btnDeleteEmployee.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(EmployeeList.getSelectedValue()!=null)
				{
					if(String.valueOf(EmployeeList.getSelectedValue()).equals("Admin"))
					{
						JOptionPane.showMessageDialog(frmAdminProfilePage, "Admin Can not delete!");
					}
					else
					{
						Bson filter = new Document("Name", String.valueOf(EmployeeList.getSelectedValue()));
						mongoCollection.deleteOne(filter);
				
						listModelEmployee.remove(EmployeeList.getSelectedIndex());
						
						JOptionPane.showMessageDialog(frmAdminProfilePage, "Employee Delete!");
					}
				}
				else
				{
					JOptionPane.showMessageDialog(frmAdminProfilePage, "Please select an Employee!");
				}
				
				
			}
		});
		btnDeleteEmployee.setBounds(127, 215, 75, 29);
		frmAdminProfilePage.getContentPane().add(btnDeleteEmployee);
		
		JScrollPane scrollPane_Room = new JScrollPane(RoomList);
		scrollPane_Room.setBounds(243, 45, 160, 165);
		frmAdminProfilePage.getContentPane().add(scrollPane_Room);
		
		JButton btnAddRoom = new JButton("Add");
		btnAddRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Document myDoc = (Document)mongoCollectionRooms.find().sort(new BasicDBObject("RID",-1)).first();
				int largestRoomID = myDoc.getInteger("RID");
				
				//long TotalRoom = mongoCollectionRooms.count();
				String RoomNo = String.valueOf(1000+largestRoomID+1);
				
				ArrayList< DBObject > array = new ArrayList< DBObject >();
				Document document = new Document("RID", largestRoomID+1);
				document.append("RoomNo", RoomNo);
				document.append("TimeBooked", array);
				mongoCollectionRooms.insertOne(document);
				
				listModelRoom.addElement(RoomNo);
				
				JOptionPane.showMessageDialog(frmAdminProfilePage, "Room Add");
			}
		});
		btnAddRoom.setBounds(253, 215, 70, 29);
		frmAdminProfilePage.getContentPane().add(btnAddRoom);
		
		JButton btnDeleteRoom = new JButton("Delete");
		btnDeleteRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(RoomList.getSelectedValue()!=null)
				{
					//System.out.print(String.valueOf(RoomList.getSelectedValue()));
					Bson filter = new Document("RoomNo", String.valueOf(RoomList.getSelectedValue()));
					mongoCollectionRooms.deleteOne(filter);
			
					listModelRoom.remove(RoomList.getSelectedIndex());
					
					JOptionPane.showMessageDialog(frmAdminProfilePage, "Room Delete!");
				}
				else
				{
					JOptionPane.showMessageDialog(frmAdminProfilePage, "Please select a Room!");
				}
			}
		});
		btnDeleteRoom.setBounds(324, 215, 75, 29);
		frmAdminProfilePage.getContentPane().add(btnDeleteRoom);
		
		JLabel lblEmployee = new JLabel("Employee");
		lblEmployee.setBounds(85, 17, 86, 16);
		lblEmployee.setForeground(Color.WHITE);
		lblEmployee.setFont(new Font("Dialog", Font.BOLD, 16));
		frmAdminProfilePage.getContentPane().add(lblEmployee);
		
		JLabel lblRoom = new JLabel("Room");
		lblRoom.setBounds(298, 17, 61, 16);
		lblRoom.setForeground(Color.WHITE);
		lblRoom.setFont(new Font("Dialog", Font.BOLD, 16));
		frmAdminProfilePage.getContentPane().add(lblRoom);
		
		JButton btnCancel = new JButton("Logout");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LoginPage login = new LoginPage();
				frmAdminProfilePage.dispose();
			}
		});
		btnCancel.setBounds(334, 263, 92, 29);
		frmAdminProfilePage.getContentPane().add(btnCancel);
		
		JButton btnApplicationForm = new JButton("Application Form");
		btnApplicationForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(mongoCollectionAdmin.count()==0)
				{
					JOptionPane.showMessageDialog(frmAdminProfilePage, "No Application!");
				}
				else
				{
					AdminAppForm Appform = new AdminAppForm();
					frmAdminProfilePage.dispose();
				}
				
			}
		});
		btnApplicationForm.setBounds(172, 263, 152, 29);
		frmAdminProfilePage.getContentPane().add(btnApplicationForm);
		
		String currentDirectory = System.getProperty("user.dir");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
		lblNewLabel.setBounds(0, 0, 450, 298);
		frmAdminProfilePage.getContentPane().add(lblNewLabel);

	}
	
	private void initModel()
	{
		MongoCursor<Document> cursor = mongoCollection.find().iterator();
		try {
		      while (cursor.hasNext()) {
		        Document doc = cursor.next();
		        if(doc.get("Availability").equals("Available"))
		        {
		        	employees.add(doc.get("Name").toString());
		        }
		      }
		    } finally {
		      cursor.close();
		    }
		
		for(String s : employees)
		{
			listModelEmployee.addElement(s);
		}
		
		MongoCursor<Document> roomcursor = mongoCollectionRooms.find().iterator();
		try {
		      while (roomcursor.hasNext()) {
		        Document doc = roomcursor.next();
		        	rooms.add(doc.get("RoomNo").toString());
		      }
		    } finally {
		    	roomcursor.close();
		    }
		
		for(String r : rooms)
		{
			listModelRoom.addElement(r);
		}
	}
}
