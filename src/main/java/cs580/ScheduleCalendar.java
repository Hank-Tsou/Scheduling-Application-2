package cs580;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import customComponents.ResizableButton;

import javax.swing.ListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class ScheduleCalendar {

	private final Font ARIAL_FONT = new Font("Arial", Font.PLAIN, 11);
	
	private JFrame frame;
	private JTable table;
	
	private int selectMonth = LocalDate.now().getMonthValue();
	private int selectYear = LocalDate.now().getYear();
	
	private final int currentMonth = LocalDate.now().getMonthValue();
	private final int currentYear = LocalDate.now().getYear();
	private final int currentDay = LocalDate.now().getDayOfMonth();
	private int MeetingMonth;
	private int MeetingDay;
	
	private final String DAYS_OF_WEEK[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	private final String MONTHS[] = {"January", "February", "March", "April", "May", "June", "July",
			"August", "September", "October", "November", "December"};
	
	private String RunningDay;	
	
	private DefaultListModel<String> Invitee;
	private JLabel lblNewLabel;
	private String LoginUsername;
	private Boolean ExistMeeting;
	private int ExistMeetingID;
	
	private Object selectedValue;
	
	private ArrayList<Integer> MeetingDateList;
	
	public ScheduleCalendar(DefaultListModel<String> listModelInvitee, String Username, Boolean existmeeting, int existmeetingID) {
		Invitee = listModelInvitee;
		LoginUsername = Username;
		ExistMeeting = existmeeting;
		ExistMeetingID = existmeetingID;
		initialize();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}


	private void initialize() {
		MeetingDateList = new ArrayList<Integer>();
		
		frame = new JFrame();
		frame.setTitle("My Calendar");
		frame.setBounds(100, 100, 460, 430);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		CustomRenderer customRenderer = new CustomRenderer();
		customRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		table = new JTable();
		table.setGridColor(Color.LIGHT_GRAY);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				int row = table.rowAtPoint(arg0.getPoint()); // get row of current mouse cursor
				int col = table.columnAtPoint(arg0.getPoint()); // get column of current mouse cursor
				
				if (row >= 0 && col >= 0) {
					selectedValue = table.getValueAt(row, col); // get the value at the cursor
				}
				
				// Making sure the selected date is not null
				if (selectedValue != null) {
					int IntSelectValue = (Integer) selectedValue;
					
					boolean isSelectionValid = checkForValidDateSelection(selectYear, selectMonth, IntSelectValue);
					
					if (!isSelectionValid) {
						JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
				}
			}
		});
		
		DefaultTableModel model = new DefaultTableModel(generateDaysInMonth(selectYear, selectMonth),
			DAYS_OF_WEEK) {
				private static final long serialVersionUID = 1L;
	
				public boolean isCellEditable(int row, int column) {
					return false;
				}
		};
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(34, 68, 386, 272);
		frame.getContentPane().add(scrollPane);
		
		table.setModel(model);
		table.setDefaultRenderer(Object.class, customRenderer);
		table.setRowHeight(50);
		table.setFillsViewportHeight(true);
		table.setBounds(65, 54, 324, 168);
		scrollPane.setViewportView(table);
		
		ResizableButton btnCancel = new ResizableButton("Cancel");
		btnCancel.setFont(new Font("Arial", Font.BOLD, 11));
		btnCancel.setBounds(244, 352, 96, 36);
		frame.getContentPane().add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ProfilePage(LoginUsername);
				frame.dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.setBounds(85, 21, 288, 36);
		frame.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(10, 0));
		
		ResizableButton button_1 = new ResizableButton("<");
		panel.add(button_1, BorderLayout.WEST);
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectMonth--;
				if (selectMonth <= 0) {
					selectMonth = MONTHS.length;
					selectYear--;
				}
				
				lblNewLabel.setText(MONTHS[selectMonth - 1] + " " + selectYear);
				Object[][] tempData = generateDaysInMonth(selectYear, selectMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
		button_1.setFont(ARIAL_FONT);
		
		ResizableButton button = new ResizableButton(">");
		panel.add(button, BorderLayout.EAST);
		button.setFont(ARIAL_FONT);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblNewLabel, BorderLayout.CENTER);
		lblNewLabel.setFont(new Font("Arial", Font.BOLD, 14));
		lblNewLabel.setText(MONTHS[selectMonth - 1] + " " + selectYear);
		
		ResizableButton selectBtn = new ResizableButton("Select");
		
		selectBtn.setText("Select");
		selectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (selectedValue != null) {
					int IntSelectValue = (Integer) selectedValue;
					boolean isSelectionValid = checkForValidDateSelection(selectYear, selectMonth, IntSelectValue);
					
					if (isSelectionValid) {
						new TimeRoomSelection(Invitee, LoginUsername, selectYear, selectMonth, selectedValue, ExistMeeting, ExistMeetingID) ;
						frame.dispose();
					}
					else {
						JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
					}
				}
				else {
					JOptionPane.showMessageDialog(frame, "You selected an invalid date. Please select another date.");
				}
			}
		});
		selectBtn.setFont(new Font("Arial", Font.BOLD, 11));
		selectBtn.setBounds(117, 352, 96, 36);
		frame.getContentPane().add(selectBtn);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(App.CURRENT_DIRECTORY + "/image/calendarB2.jpg"));
		lblNewLabel_1.setBounds(0, 0, 460, 408);
		frame.getContentPane().add(lblNewLabel_1);
		
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel tempModel = (DefaultTableModel) table.getModel();
				tempModel.setRowCount(0);
				
				selectMonth++;
				if (selectMonth > MONTHS.length) {
					selectMonth = 1;
					selectYear++;
				}
				
				lblNewLabel.setText(MONTHS[selectMonth - 1] + " " + selectYear);
				Object[][] tempData = generateDaysInMonth(selectYear, selectMonth);
				table.setModel(new DefaultTableModel(tempData, DAYS_OF_WEEK));
				((AbstractTableModel)table.getModel()).fireTableDataChanged();
			}
		});
	}
	
	
	private Object[][] generateDaysInMonth(int year, int month) {
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
	
	/**
	 * Check if the select year is valid for a meeting.
	 * @author Tho Nguyen
	 * @param selectYear1 - The selected year
	 * @param selectMonth1 - The selected month
	 * @param selectDay1 - The selected day
	 * @return True if the date is valid, False otherwise.
	 */
	private boolean checkForValidDateSelection(int selectYear1, int selectMonth1, int selectDay1) {
		if (selectYear1 > currentYear) {
			return true;
		}
		else if(selectYear1 == currentYear) {
			if(selectMonth1 > currentMonth) {	
				return true;
			}
			
			else if(selectMonth1 == currentMonth && selectDay1 >= currentDay) {
				return true;
			}
		}
		
		return false;
	}
	
	//*****************************
	// Getter and setters
	//*****************************
	public int getCurrentSelectedMonth() {return selectMonth;}
	
	
	// Set color
	private class CustomRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value,
				boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if(table.getValueAt(row, column) != null) {
				int lengthOfMonth = YearMonth.of(selectYear, selectMonth).lengthOfMonth();
				int selectDay = (Integer) table.getValueAt(row, column);
								
				for(int i=0; i < lengthOfMonth; i++) {
					if (selectYear < currentYear || 
						(selectYear == currentYear && selectMonth < currentMonth) ||
						(selectYear == currentYear && selectMonth == currentMonth && selectDay < currentDay)) {
						c.setBackground(Color.LIGHT_GRAY);
					}
					else {
						c.setBackground(table.getBackground());
					}
				}
			}
			
			return c;
		}
	}
}