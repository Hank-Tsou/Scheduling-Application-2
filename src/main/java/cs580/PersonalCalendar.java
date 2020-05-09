package cs580;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import customComponents.ResizableButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.channels.SelectableChannel;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class PersonalCalendar {
	
	private JFrame frmPersonalCalendar;
	private JTable table;
	
	private int currentMonth = LocalDate.now().getMonthValue();
	private int currentYear = LocalDate.now().getYear();
	
	private final String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private final String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	private ArrayList<String> myMeetingList = new ArrayList<String>();
	private ArrayList<String> myMeetingListRC = new ArrayList<String>();
	private JList<String> Meetinglist;
	private DefaultListModel<String> MeetinglistModel;
	private ArrayList<Integer> MeetingDateList=new ArrayList<Integer>();  
	
	private JLabel lblNewLabel;
	private Object selectedValue;
	private Boolean NewNotification = false;
	private JTextArea MeetingDetail;
	
	private String Date;
	private String StringDate;
	private String LoginUsername;
	private int MeetingMonth;
	private int MeetingDay;
	
	private String RunningDay;
	
	private DefaultTableCellRenderer tcr;
	private DefaultTableModel tablemodel;
	
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
	
	public PersonalCalendar(String username) {
		LoginUsername = username;
		SeperateDate();
		initialize();
		//setUpMeetingList();
		frmPersonalCalendar.setLocationRelativeTo(null);
		frmPersonalCalendar.setVisible(true);
	}


	private void initialize() {
		String currentDirectory = System.getProperty("user.dir");
		
		frmPersonalCalendar = new JFrame();
		frmPersonalCalendar.setTitle("Personal Calendar");
		frmPersonalCalendar.setBounds(100, 100, 610, 428);
		frmPersonalCalendar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPersonalCalendar.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 82, 314, 260);
		frmPersonalCalendar.getContentPane().add(scrollPane);
		
///++++++++++++++++++++++++++++++++++++++++		
		tablemodel = new DefaultTableModel(generateDaysInMonth(currentYear, currentMonth), DAYS_OF_WEEK) 
		{
				private static final long serialVersionUID = 1L;
				public boolean isCellEditable(int row, int column) 
				{
					return false;
				}
		};
///++++++++++++++++++++++++++++++++++++++++
		CustomRenderer customRenderer = new CustomRenderer();
		customRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		table = new JTable(tablemodel);
		table.setDefaultRenderer(Object.class, customRenderer);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		table.setRowHeight(40);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			
				selectedValue = null;
				int row = table.rowAtPoint(arg0.getPoint()); // get row of current mouse cursor
				int col = table.columnAtPoint(arg0.getPoint()); // get column of current mouse cursor
				
				if (row >= 0 && col >= 0) {
					selectedValue = table.getValueAt(row, col); // get the value at the cursor
				}			
				// Making sure the selected date is not null
				if (selectedValue != null) {
					CalculateDate();
					MeetinglistModel.clear();
					MeetingDetail.setText(null);
					listMeetingSchedule();
				}
				else {
					JOptionPane.showMessageDialog(frmPersonalCalendar, "You selected an invalid date. Please select another date.");
				}
			}
		});
		
/////////////////////////////////////////////////////////////////////////////////////////  <<  month  >>
/////////////////////////////////////////////////////////////////////////////////////////
		JPanel panel = new JPanel();
		panel.setBounds(20, 43, 314, 39);
		//panel.setBackground(new Color(0,0,0,0));
		frmPersonalCalendar.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(10, 0));
		
// Button << ///////////////////		
		ResizableButton button_1 = new ResizableButton("<");
		panel.add(button_1, BorderLayout.WEST);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				currentMonth--;
				if (currentMonth <= 0) {
					currentMonth = MONTHS.length;
					currentYear--;
				}
///++++++++++++++++++++++++++++++++++++++++				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				SeperateDate();
///++++++++++++++++++++++++++++++++++++++++		
				MeetinglistModel.clear();
				MeetingDetail.setText(null);
			}
		});		
		
// Button >> ///////////////////		
		ResizableButton button = new ResizableButton(">");
		panel.add(button, BorderLayout.EAST);
		
/////////////////////
		lblNewLabel = new JLabel();
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel, BorderLayout.CENTER);
		lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tempModel = (DefaultTableModel) table.getModel();
				tempModel.setRowCount(0);
				
				currentMonth++;
				if (currentMonth > MONTHS.length) {
					currentMonth = 1;
					currentYear++;
				}
///+++++++++++++++++++++++++++++++++++++++				
				lblNewLabel.setText(MONTHS[currentMonth - 1] + " " + currentYear);
				Object[][] tempData = generateDaysInMonth(currentYear, currentMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
///+++++++++++++++++++++++++++++++++++++++
				MeetinglistModel.clear();
				MeetingDetail.setText(null);
			}
		});
		
///////////////////////////////////////////////////////////////////////////////////////// Left side detail information
/////////////////////////////////////////////////////////////////////////////////////////
		MeetingDetail = new JTextArea();
		MeetingDetail.setBounds(353, 205, 231, 125);
		frmPersonalCalendar.getContentPane().add(MeetingDetail);
		
		MeetinglistModel = new DefaultListModel();
		Meetinglist = new JList(MeetinglistModel);
		Meetinglist.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				String UserSelectedMeeting = Meetinglist.getSelectedValue();
				String getID = UserSelectedMeeting.substring(0, UserSelectedMeeting.indexOf(' ')); 
				int IntID = Integer.parseInt(getID);
				MeetingDetail.setText(null);
				
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntID )).first();
				if(myMeeting != null)
				{
					Document myStatus = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
					List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
					
					MeetingDetail.append(" Host: " + myMeeting.getString("Host") + "\n" +
										 " Room: " + myMeeting.getString("Room") + "\n\n" +
										 " Start Time: " + myMeeting.getString("StartTime") + ":00"  + "\n" +
										 " End Time: " + myMeeting.getString("EndTime") + ":00" +"\n" +
										 "-----------------------------"+"\n" +
										 " Respond: " +meetingRes.get(0).getString("Respond")
										);
				}
			}
		});
		Meetinglist.setBounds(353, 56, 231, 111);
		frmPersonalCalendar.getContentPane().add(Meetinglist);
		
		JPanel NCbtnPanel = new JPanel();
		NCbtnPanel.setBounds(338, 358, 266, 39);
		NCbtnPanel.setBackground(new Color(0,0,0,0));
		frmPersonalCalendar.getContentPane().add(NCbtnPanel);
		NCbtnPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
						
						JButton btnNotificationCenter = new JButton("Notification Center");
						NCbtnPanel.add(btnNotificationCenter);
						
				btnNotificationCenter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Document myStatus = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
						List<Document> meetingRes = (List<Document>) myStatus.get("Meeting");
						
						for(int i=0; i<meetingRes.size(); i++)
						{
							Document MeetingElement = meetingRes.get(i);					
							String StringRespond = MeetingElement.getString("Respond");
							if(StringRespond.equals("P")){
								NewNotification = true;
							}
							else if(StringRespond.equals("X")){
								NewNotification = true;
							}
						}
						
						if(NewNotification == true){
							Notification Notice = new Notification(LoginUsername);
							frmPersonalCalendar.dispose();
						}
						
						else{
							JOptionPane.showMessageDialog(frmPersonalCalendar, "No New Norification!");
						
						}
					}
				});
				
				///////////////////////////////////////////////////////////////////////////////////////// cancel button
				/////////////////////////////////////////////////////////////////////////////////////////
						ResizableButton btnCancel = new ResizableButton("Cancel");
						NCbtnPanel.add(btnCancel);
						
						JLabel lblMeeting = new JLabel("Meeting");
						lblMeeting.setBounds(439, 27, 77, 25);
						lblMeeting.setForeground(Color.WHITE);
						lblMeeting.setFont(new Font("Dialog", Font.BOLD, 16));
						frmPersonalCalendar.getContentPane().add(lblMeeting);
						
						JLabel lblDetail = new JLabel("Meeting Information");
						lblDetail.setBounds(387, 181, 176, 25);
						lblDetail.setForeground(Color.WHITE);
						lblDetail.setFont(new Font("Dialog", Font.BOLD, 16));
						frmPersonalCalendar.getContentPane().add(lblDetail);
						
						JLabel lblNewLabel_1 = new JLabel("");
						lblNewLabel_1.setIcon(new ImageIcon(currentDirectory+"/image/calendarB2.jpg"));
						lblNewLabel_1.setBounds(0, 0, 610, 406);
						frmPersonalCalendar.getContentPane().add(lblNewLabel_1);
						btnCancel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								ProfilePage profile = new ProfilePage(LoginUsername);
								frmPersonalCalendar.dispose();
							}
						});	
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void CalculateDate()
	{
		int intSelectedValue = (Integer) selectedValue;
		String stringSelectedValue = Integer.toString(intSelectedValue);
		String stringCurrentMonth = Integer.toString(currentMonth);
		
		if (currentMonth/10 == 0){
			stringCurrentMonth = "0" + stringCurrentMonth;
		}
		
		if (intSelectedValue/10 == 0){
			stringSelectedValue = "0" + stringSelectedValue;
		}
		Date = stringCurrentMonth + stringSelectedValue;
	}
	
	private void SeperateDate()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Username", LoginUsername )).first(); //get member
		List<Document> MeetingLists = (List<Document>) myDoc.get("Meeting"); 						//get meeting list
		int MeetingListSize = MeetingLists.size(); 												//get meeting list size
		MeetingDateList.clear();
		// Count meeting list
		for(int j=0; j<MeetingListSize; j++)
		{
			Document MeetingElement = MeetingLists.get(j);
			//System.out.println(MeetingElement); // get meeting ID to find the meeting date, start time and end time
			if(!MeetingElement.getString("Respond").equals("X"))
			{
				String StringMeetingID = String.valueOf(MeetingElement.get("MeetingID"));
				int IntMeetingID = Integer.parseInt(StringMeetingID);
				// use meeting ID to get meeting detail
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
				
				//String convertedToString = myMeeting.get("Date").toString();
				int MeetingDate = Integer.valueOf((String) myMeeting.get("Date"));
				
				MeetingMonth = MeetingDate/100;
				MeetingDay = MeetingDate%100;
				
				MeetingDateList.add(MeetingMonth);
				MeetingDateList.add(MeetingDay);	
			}
		}
	}
	
	private void listMeetingSchedule()
	{
		Document myDoc = mongoCollection.find(Filters.eq("Username", LoginUsername )).first();
		List<Document> meetinglsit = (List<Document>) myDoc.get("Meeting");
		
		int size = meetinglsit.size();
		
		for (int i=0; i<size; i++)
		{
			Document meeting = meetinglsit.get(i);
			if(!meeting.getString("Respond").equals("X"))
			{
				String StringMeetingID = String.valueOf(meeting.get("MeetingID"));
				int IntMeetingID = Integer.parseInt(StringMeetingID);
				
				Document myMeeting = mongoCollectionMeeting.find(Filters.eq("MeetingID", IntMeetingID )).first();
				
				if(myMeeting.getString("Date").equals(Date)){
					String outputInList = StringMeetingID + " " + myMeeting.getString("Host") + " " + myMeeting.getString("StartTime") + ":00 - " +myMeeting.getString("EndTime") + ":00";
					MeetinglistModel.addElement(outputInList);
				}
			}
		}		
	}
	
	private Object[][] generateDaysInMonth(int year, int month) 
	{
		int colCounter = 7;
		int rowCounter;
				
		int lengthOfMonth = YearMonth.of(year, month).lengthOfMonth();
		LocalDate tempLocalDate = LocalDate.of(year, month, 1);
		int dayOfFirstDayOfMonth = tempLocalDate.getDayOfWeek().getValue() % 7;
		
		// If the month is February and the first day of month is Monday
		if (dayOfFirstDayOfMonth == 0 && YearMonth.of(year, month).getMonthValue() == 2) {
			rowCounter = 4;
		}
		// If first day of month is a Friday and the month's length is 31
		else if (dayOfFirstDayOfMonth == 5 && lengthOfMonth == 31) {
			rowCounter = 6;
		} 
		// If first day of month is a Saturday
		else if (dayOfFirstDayOfMonth == 6) {
			rowCounter = 6;
		}
		// Else, there will be 5 weeks
		else {
			rowCounter = 5;
		}
		
		Object[][] days = new Object[rowCounter][colCounter];
		
		boolean isFirstWeek = true;
		int curRow = 0;
		int curCol = 0;
		
		for (int i = 1; i <= lengthOfMonth; i++) {
			if (isFirstWeek)
				curCol = dayOfFirstDayOfMonth;
					
			days[curRow][curCol] = i;
			curCol++;
			
			if (curCol >= 7 || i == lengthOfMonth) {			
				curCol = 0;
				curRow++;
			}
			
			// Toggle isFirstWeek off
			if (isFirstWeek)
				isFirstWeek = false;
		}
		
		return days;
	}
	
	//*****************************
	// Getter and setters
	//*****************************
	public int getCurrentMonth() {return currentMonth;}
	
	public void print(Object o) {
		System.out.println(o);
	}
	
	// Set color
	private class CustomRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if(table.getValueAt(row, column) != null)
			{
				RunningDay = table.getValueAt(row, column).toString();
				Integer IntRunningDay = Integer.valueOf(RunningDay);
				
				for(int i=0; i<MeetingDateList.size();)
				{
					MeetingMonth = MeetingDateList.get(i);
					MeetingDay =  MeetingDateList.get(i+1);

					if (currentMonth == MeetingMonth && IntRunningDay == MeetingDay) {
						c.setBackground(Color.orange);
						break;
					}
					
					else{
						c.setBackground(table.getBackground());
					}
					i=i+2;
				}
			}

			else {
				c.setBackground(table.getBackground());
			}
			
			return c;
		}
	}
}
