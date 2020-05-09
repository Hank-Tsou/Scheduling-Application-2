package cs580;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class AddMember extends JFrame
	implements ActionListener, ItemListener, ListSelectionListener
{

	private static final long serialVersionUID = 1L;
	//boarder
	private Border borderCenter = BorderFactory.createEmptyBorder(10, 10, 10,10); //(top, left, bottom, right) 
	private Border borderContents = BorderFactory.createEmptyBorder(0, 0, 10, 0);
	private Border borderList = BorderFactory.createLineBorder(Color.BLUE, 1);
	
	//container
	private Box boxButtons = Box.createVerticalBox();
	private Box boxListEmployee = Box.createVerticalBox();
	private Box boxListInvitee = Box.createVerticalBox();
	
	private JPanel contents;
	
	//components
	private JButton btnAdd;
	private JButton btnAddAll;
	private JButton btnRemove;
	private JButton btnRemoveAll;
	private JButton btnSearch;
	private JButton btnNext;
	private JButton btnBack;
	private JButton btnClear;
	
	private JLabel lblListEmployee;
	private JLabel lblListInvitee;
	private JLabel lblListSearch;
	private JLabel lblImage;
	
	private JList<String> listEmployee;
	private JList<String> listInvitee;
	private JTextField searchName;
	private JTextArea txtPerInfo;
	
	private ArrayList<String> employees = new ArrayList<String>();
	
	private DefaultListModel<String> listModelEmployee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelInvitee = new DefaultListModel<String>();
	private DefaultListModel<String> listModelTemp = new DefaultListModel<String>();
	
	private boolean pressSearch = false; 
	private String LoginUsername;
	private Boolean ExistMeeting;
	private int ExistMeetingID;
	private JFrame frame;
	
	private String currentDirectory = System.getProperty("user.dir");
	
//////Database Setup ////////////////////////////////////////////////////////////////	
//	String uri = "mongodb://rhalf001:admin@580scheduledb-shard-00-00-w3srb.mongodb.net:27017,580scheduledb-shard-00-01-w3srb.mongodb.net:27017,580scheduledb-shard-00-02-w3srb.mongodb.net:27017/test?ssl=true&replicaSet=580scheduleDB-shard-0&authSource=admin";
	String uri = "mongodb://hank630280888:AdminTest@cluster0-shard-00-00-dxoya.mongodb.net:27017,cluster0-shard-00-01-dxoya.mongodb.net:27017,cluster0-shard-00-02-dxoya.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority";
	MongoClientURI clientUri = new MongoClientURI(uri);
	MongoClient mongoClient = new MongoClient(clientUri);
	MongoDatabase mongoDatabase = mongoClient.getDatabase("580Schedule");
	MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("Users");
	MongoCollection<Document> mongoCollectionMeeting = mongoDatabase.getCollection("Meeting");
///////////////////////////////////////////////////////////////////////////////////////
	
	MongoCursor<Document> cursor = mongoCollection.find().iterator();
	private JLabel lblNewLabel;

	public AddMember(String username, int meetingid, boolean existmeeting)
	{
		super("Member Selection");
		App.setFonts();
		LoginUsername = username;
		ExistMeetingID = meetingid;
		ExistMeeting = existmeeting;
				
		RestEmployeelist();
	
		/////// main container //////
		contents = new JPanel();
		contents.setBorder(borderContents);
		setContentPane(contents);
		contents.setLayout(null);
		
		/////// Add Components ///////
		
		// North Part
		JLabel lblTitle = new JLabel ("Member Selection", SwingConstants.CENTER);
		lblTitle.setBounds(309, 0, 238, 34);
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setFont(new Font("Dialog", Font.BOLD, 25));
		contents.add(lblTitle);
		
		initEmployeeModel();
		
		// Employee List
		lblListEmployee = new JLabel("            Employee");
		lblListEmployee.setForeground(Color.WHITE);
		lblListEmployee.setFont(new Font("Dialog", Font.BOLD, 16));
		listEmployee = new JList<String>(listModelEmployee); // already set default
		listEmployee.setAlignmentX(LEFT_ALIGNMENT);
		listEmployee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listEmployee.setBorder(borderList); // already set before
		
		JScrollPane scrollListEmployee = new JScrollPane(listEmployee);
		scrollListEmployee.setAlignmentX(LEFT_ALIGNMENT);
		scrollListEmployee.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setSpecificSize(scrollListEmployee, new Dimension(200, 300));
		boxListEmployee.setBounds(39, 36, 200, 328);
		contents.add(boxListEmployee);
		
		boxListEmployee.add(lblListEmployee);
		boxListEmployee.add(scrollListEmployee);
		listEmployee.addListSelectionListener(this);
		/////// Space //////////////////////////////////////////////////////			
				
				// Invited List
				lblListInvitee = new JLabel("             Member");
				lblListInvitee.setForeground(Color.WHITE);
				lblListInvitee.setFont(new Font("Dialog", Font.BOLD, 16));
				
				listInvitee = new JList<String>(listModelInvitee);
				listInvitee.setAlignmentX(LEFT_ALIGNMENT);
				listInvitee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listInvitee.setBorder(borderList);
				
						JScrollPane scrollListInvitee = new JScrollPane(listInvitee);
						scrollListInvitee.setAlignmentX(LEFT_ALIGNMENT);
						scrollListInvitee.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
						setSpecificSize(scrollListInvitee, new Dimension(200, 300));
						boxListInvitee.setBounds(417, 43, 200, 321);
						contents.add(boxListInvitee);
						
						boxListInvitee.add(lblListInvitee);
						boxListInvitee.add(scrollListInvitee);
						listInvitee.addListSelectionListener(this);
		/////// Space /////////
				
				// Image Part
				lblListSearch = new JLabel("Personal Information"); // space is for looking
				lblListSearch.setBounds(648, 60, 172, 20);
				contents.add(lblListSearch);
				lblListSearch.setForeground(Color.WHITE);
				lblListSearch.setFont(new Font("Dialog", Font.BOLD, 16));
				//lblListSearch.setAlignmentX(LEFT_ALIGNMENT);
				
				// image position
				lblImage = new JLabel(new ImageIcon());
				lblImage.setBounds(664, 88, 138, 164);
				contents.add(lblImage);
				lblImage.setAlignmentX(LEFT_ALIGNMENT);
				setSpecificSize(lblImage, new Dimension(137, 161));
				
		/////// Space //////////////////////////////////////////////////////		
		
				/////// Add and Remove button ///////
				btnAdd = new JButton("Add >>");
				btnAddAll = new JButton("Add All >>");
				btnRemove = new JButton("<< Remove");
				btnRemoveAll = new JButton("<< Remove All");
				
				
				Dimension dimRemoveAll = btnRemoveAll.getPreferredSize();
				setSpecificSize(btnAdd, dimRemoveAll);
				setSpecificSize(btnAddAll, dimRemoveAll);
				setSpecificSize(btnRemove, dimRemoveAll);
				boxButtons.setBounds(259, 131, 138, 146);
				contents.add(boxButtons);
				
				boxButtons.add(btnAdd);
				boxButtons.add(Box.createRigidArea(new Dimension (1, 5)));
				boxButtons.add(btnAddAll);
				boxButtons.add(Box.createRigidArea(new Dimension (1, 20)));
				boxButtons.add(btnRemove);
				boxButtons.add(Box.createRigidArea(new Dimension (1, 5)));
				boxButtons.add(btnRemoveAll);
				
				// search button
				btnSearch = new JButton("Search");
				btnSearch.setBounds(145, 370, 76, 35);
				contents.add(btnSearch);
				setSpecificSize(btnSearch, new Dimension(80, 20));
				
				//Clear button
				btnClear = new JButton("Clear");
				btnClear.setBounds(231, 370, 76, 35);
				contents.add(btnClear);
				setSpecificSize(btnClear, new Dimension(80, 20));
				
				searchName = new JTextField();
				searchName.setBounds(37, 370, 98, 34);
				contents.add(searchName);
				searchName.setAlignmentX(LEFT_ALIGNMENT);
				
				Dimension dimSearchView = searchName.getPreferredSize();
				setSpecificSize(searchName, new Dimension(100, dimSearchView.height));
				
				// Next button
				btnNext = new JButton("Add");
				btnNext.setBounds(733, 395, 100, 30);
				contents.add(btnNext);
				
				// Back button
				btnBack = new JButton("Cancel");
				btnBack.setBounds(627, 395, 100, 30);
				contents.add(btnBack);
				
				txtPerInfo = new JTextArea();
				txtPerInfo.setBounds(627, 269, 206, 95);
				contents.add(txtPerInfo);
				
				lblNewLabel = new JLabel("");
				lblNewLabel.setIcon(new ImageIcon(currentDirectory + "/image/calendarB2.jpg"));
				lblNewLabel.setBounds(0, 0, 850, 438);
				contents.add(lblNewLabel);
				
				btnBack.addActionListener(this);
				btnNext.addActionListener(this);
				btnClear.addActionListener(this);
				btnSearch.addActionListener(this);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		
		
		/////// event listener ////////
		btnAdd.addActionListener(this);
		btnAddAll.addActionListener(this);
		btnRemove.addActionListener(this);
		btnRemoveAll.addActionListener(this);

		/////// set size /////////
		setSize(850, 460);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	
	 }
	
	private void RestEmployeelist()
	{
		Boolean MemberSelected=false;
		Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", ExistMeetingID )).first();
		ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
		
		try {
		      while (cursor.hasNext()) {
		        Document doc = cursor.next();
		        if(doc.get("Availability").equals("Available"))
		        {
			        	for(int i=0; i<MemberLists.size(); i++)
			    		{
			        		if(doc.get("Name").toString().equals(String.valueOf(MemberLists.get(i))))
			        		{
			        			MemberSelected = true;
			        			break;
			        		}
			    		}

			        	if(MemberSelected==false)
			        	{
			        		employees.add(doc.get("Name").toString());
			        	}
			        	MemberSelected = false;
		        }
		      }
		    } finally {
		      cursor.close();
		    }
	}

	private void initEmployeeModel()
	{
		for(String s : employees)
		{
			listModelEmployee.addElement(s);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();
		if(source == btnAdd)
		{
			addItem();
			return;
		}
		if(source == btnAddAll)
		{
			addAllItems();
			return;
		}
		if(source == btnRemove)
		{
			removeItem();
			return;
		}
		if(source == btnRemoveAll)
		{
			removeAllItems();
			return;
		}
		if(source == btnNext)
		{
			if(listModelInvitee.isEmpty())
			{
				JOptionPane.showMessageDialog(frame, "Please select Members!");
			}
			else
			{
				 int answer = JOptionPane.showConfirmDialog(frame, "Do you want to reschedule the meeting?");
				    if (answer == JOptionPane.YES_OPTION) 
				    {
				    		goToScheduleCalender();
						return;
				    } 
				    else if (answer == JOptionPane.NO_OPTION) 
				    {
				    		AddNewEmployee();
				    		return;
				    }
			}
		}
		if(source == btnBack)
		{
			backToProfile(LoginUsername);
			return;
		}
		if(source == btnSearch)
		{
			searchMember();
			return;
		}
		if(source == btnClear)
		{
			showRestEmployee();
			return;
		}
	}

	private void addItem()
	{
		int isSelected = listEmployee.getSelectedIndex();
		if(isSelected == -1)
		{
			return;
		}
		
		String addedItem = listEmployee.getSelectedValue();
		
		// remove from left list
		listModelEmployee.remove(isSelected);
		if (pressSearch == true)
		{
			listModelTemp.removeElement(addedItem);
		}
		//displaySelectedItems(); ///////////////////////////////////////////////////////////
		
		// add to right list
		int size = listModelInvitee.getSize();
		if (size == 0)
		{
			listModelInvitee.addElement(addedItem);
			return;
		}
		
		// find a larger item to put in to right order
		for(int i = 0; i<size; i++)
		{
			String item = listModelInvitee.elementAt(i);
			int compare = addedItem.compareToIgnoreCase(item);
			if(compare < 0)
			{
				listModelInvitee.add(i, addedItem);
				return;
			}
		}
		
		// else add item to the end
		listModelInvitee.addElement(addedItem);
	}

	private void addAllItems()
	{
		listModelInvitee.clear();
		for (String s : employees)
		{
			listModelInvitee.addElement(s);
		}
		listModelEmployee.clear();
	}
	
	private void removeItem()
	{
		int iSelected = listInvitee.getSelectedIndex();
		if(iSelected == -1)
		{
			return;
		}
		
		String removedItem = listInvitee.getSelectedValue();
		
		// remove from right list
		listModelInvitee.remove(iSelected);
		
		// add to left list
		int size = listModelEmployee.getSize();
		if(size == 0)
		{
			listModelEmployee.addElement(removedItem);
			return;
		}
		
		// find a larger item to put into the right order
		for(int i = 0; i<size; i++)
		{
			String item = listModelEmployee.elementAt(i);
			int compare = removedItem.compareToIgnoreCase(item);
			if(compare < 0)
			{
				listModelEmployee.add(i, removedItem);
				return;
			}
		}
		
		// else add item to the end
		listModelEmployee.addElement(removedItem);
	}

	private void removeAllItems()
	{
		listModelEmployee.clear();
		initEmployeeModel();
		listModelInvitee.clear();
	}
	
	private void AddNewEmployee()
	{
		if(listModelInvitee.size()==0)
		{
			JOptionPane.showMessageDialog(frame, "Please Select the Employees");
		}
		else 
		{
			for(int i=0; i<listModelInvitee.size(); i++ )
			{				
				BasicDBObject match = new BasicDBObject();
		        match.put( "Name", listModelInvitee.getElementAt(i) );

		        BasicDBObject addressSpec = new BasicDBObject();
		        addressSpec.put("MeetingID", ExistMeetingID);
		        addressSpec.put("Respond", "P");
		        addressSpec.put("Update", "0");

		        BasicDBObject update = new BasicDBObject();
		        update.put( "$push", new BasicDBObject( "Meeting", addressSpec ) );
		        mongoCollection.updateMany( match, update );
		        
		        mongoCollectionMeeting. updateOne( Filters.eq( "MeetingID", ExistMeetingID),  
		                new Document( "$addToSet", new Document( "Member", listModelInvitee.getElementAt(i))))  
		                .wasAcknowledged ();
			}
			
			JOptionPane.showMessageDialog(frame, "Add new Member to Meeting!");
			MyMeeting Mymet = new MyMeeting(LoginUsername);
			dispose();
		}
	}

	private void goToScheduleCalender()
	{

		Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", ExistMeetingID )).first();
		ArrayList MemberLists = (ArrayList) myMeeting.get("Member");
		
	 	for(int i=0; i<MemberLists.size(); i++)
		{
	 		listModelInvitee.addElement(String.valueOf(MemberLists.get(i)));
		}
	 	
	 	//System.out.print(listModelInvitee);
	 	
		ScheduleCalendar timeslct = new ScheduleCalendar(listModelInvitee, LoginUsername, ExistMeeting, ExistMeetingID);
		dispose();
	}
	
	private void backToProfile(String LoginUsername)
	{
		new ProfilePage(LoginUsername);
		dispose();
	}
	
	private void changeImage()
	{
		int employeeIsSelected = listEmployee.getSelectedIndex();
		int inviteeIsSelected = listInvitee.getSelectedIndex();
		
		if(employeeIsSelected == -1 && inviteeIsSelected == -1)
		{
			lblImage.setIcon(null);
			txtPerInfo.setText(null);
			return;
		}
		else if(employeeIsSelected != -1 ){
			String imageName = listEmployee.getSelectedValue();
			ImageIcon MyImage = new ImageIcon(currentDirectory + "/image/"+ imageName + ".jpg");
			Image img = MyImage.getImage();
			Image NewImg = img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);  
			ImageIcon image = new ImageIcon(NewImg);
			lblImage.setIcon(image);
			
			txtPerInfo.setText(null);
			
			Document myMeeting = mongoCollection.find(Filters.eq("Username", imageName )).first();
			if(myMeeting != null)
			{
				txtPerInfo.append(" Uni: " + myMeeting.getString("University") + "\n" +
									" Dep: " + myMeeting.getString("Department") + "\n" +
									 " Degree: " + myMeeting.getString("Degree") + "\n" +
									"---------------------" + "\n" +
									 " Availability: " + myMeeting.getString("Availability")
									);
			}
			
			
			}
			else if(inviteeIsSelected != -1 ) {
			String imageName2 = listInvitee.getSelectedValue();
			ImageIcon MyImage = new ImageIcon(currentDirectory + "/image/"+ imageName2 + ".jpg");
			Image img = MyImage.getImage();
			Image NewImg = img.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH);  
			ImageIcon image = new ImageIcon(NewImg);
			lblImage.setIcon(image);
			
			txtPerInfo.setText(null);
			
			Document myMeeting = mongoCollection.find(Filters.eq("Username", imageName2 )).first();
			if(myMeeting != null)
			{
				txtPerInfo.append(" Uni: " + myMeeting.getString("University") + "\n" +
						" Dep: " + myMeeting.getString("Department") + "\n" +
						 " Degree: " + myMeeting.getString("Degree") + "\n" +
						"---------------------" + "\n" +
						 " Availability: " + myMeeting.getString("Availability")
						);
			}
			
			}
		
	}
	
	private void setTempModel()
	{
		int size = listModelEmployee.getSize();
		
		for(int i=0; i<size; i++)
		{
			listModelTemp.addElement(listModelEmployee.elementAt(i));
			//System.out.print(listModelTemp);
		}
	}

	private void searchMember()
	{
		setTempModel();
		pressSearch = true;
		int size = listModelEmployee.getSize();
		String findMe = searchName.getText();
		
		int findMeLength = findMe.length();
		boolean foundIt = false;
		int i = 0;
		
		while(i < size)
		{
			String searchMe = listModelEmployee.elementAt(i);
			int searchMeLength = searchMe.length();
			
			for (int j = 0; j <= (searchMeLength - findMeLength); j++) 
			{
	           if (searchMe.regionMatches(j, findMe, 0, findMeLength)) 
	           {
	              foundIt = true;
	              i++;
	              break;
	            }
		     }
			
			if (!foundIt) {
				listModelEmployee.removeElement(searchMe);
				size = listModelEmployee.getSize();
				i = 0;
			}
			foundIt = false;

		   }
	}	
	private void showRestEmployee()
	{
		if(pressSearch == false)
		{
			searchName.setText(null);
		}
		else
		{
			searchName.setText(null);
			listModelEmployee.clear();
			int size = listModelTemp.getSize();
			for(int i=0; i<size; i++)
			{
				listModelEmployee.addElement(listModelTemp.elementAt(i));
			}
			
			//listModelEmployee.removeElement(ForClearReset);
			listModelTemp.clear();
			pressSearch =false;
		}
		
	}
	
	private void setSpecificSize(JComponent component, Dimension dimension)
	{
		component.setMinimumSize(dimension);
		component.setPreferredSize(dimension);
		component.setMaximumSize(dimension);
	}
	public void valueChanged(ListSelectionEvent e)
	{
		Object source = e.getSource();
		if(source == listEmployee)
		{
			changeImage();
			return;
		}
		else if(source == listInvitee)
		{
			changeImage();
			return;
		}
	}
	

	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void iterateDatabase()
	{
		while (cursor.hasNext()) {
	        Document doc = cursor.next();
	      }
	}
}
	
	
	
	
	
	