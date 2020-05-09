package cs580;

import java.awt.Font;

import javax.swing.UIManager;

import NoUse.dbManage;

public class App {	

	private static final Font FONT_PLAIN = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
	private static final Font FOLD_BOLD = new Font(Font.SANS_SERIF, Font.BOLD, 12);
	
	public static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
	
	private static String LoginUsername = "Hank";
	private static int MeetingID = 2;
	
	
	public static void main( String[] args ) {
		
		DB_setUp ProgramStart = new DB_setUp();
		LoginPage login = new LoginPage();
		
//		test testcase = new test();

//		ClearDB Cleaner = new ClearDB();
//		dbManage member = new dbManage();
	}

	public static void setFonts() {
		UIManager.put("Button.font", FOLD_BOLD);
		UIManager.put("ComboBox.font", FOLD_BOLD);
		UIManager.put("Label.font", FOLD_BOLD);
		UIManager.put("List.font", FONT_PLAIN);
	}
}
