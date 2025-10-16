package guiStudentHome;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

/*******
 * <p> Title: ViewStudentHome Class. </p>
 * 
 * <p> Description: The Java/FX-based Student Home Page for the Discussion System.
 * This page allows students to create posts, view posts, and manage replies.</p>
 * 
 * <p> Copyright: Student Discussion System © 2025 </p>
 * 
 * @author Your Name
 * 
 * @version 1.00		2025-10-16 Initial version for HW2
 *  
 */

public class ViewStudentHome {
	
	/*-*******************************************************************************************
	Attributes
	*/
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Area 1: Page title and user info
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI Area 2: Create New Post section
	protected static Label label_CreatePost = new Label("Create New Post");
	protected static Label label_PostTitle = new Label("Post Title:");
	protected static TextField text_PostTitle = new TextField();
	protected static Label label_PostContent = new Label("Post Content:");
	protected static TextArea text_PostContent = new TextArea();
	protected static Label label_PostThread = new Label("Thread:");
	protected static TextField text_PostThread = new TextField();
	protected static Button button_CreatePost = new Button("Create Post");
	
	protected static Line line_Separator2 = new Line(20, 360, width-20, 360);
	
	// GUI Area 3: View/Manage Posts section
	protected static Button button_ViewMyPosts = new Button("View My Posts");
	protected static Button button_SearchPosts = new Button("Search Posts");
	protected static Button button_ViewAllPosts = new Button("View All Posts");
	
	protected static Line line_Separator3 = new Line(20, 525, width-20, 525);
	
	// GUI Area 4: Navigation buttons
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// Singleton and shared attributes
	private static ViewStudentHome theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;
	
	private static Scene theStudentHomeScene;
	protected static final int theRole = 4; // Student role

	/*-*******************************************************************************************
	Constructors
	*/

	/**********
	 * <p> Method: displayStudentHome(Stage ps, User user) </p>
	 * 
	 * <p> Description: Entry point to display the Student Home page.</p>
	 * 
	 * @param ps specifies the JavaFX Stage
	 * @param user specifies the current User
	 */
	public static void displayStudentHome(Stage ps, User user) {
		theStage = ps;
		theUser = user;
		
		if (theView == null) theView = new ViewStudentHome();
		
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		
		// Clear input fields
		text_PostTitle.setText("");
		text_PostContent.setText("");
		text_PostThread.setText("General"); // Default thread
		
		theStage.setTitle("CSE 360 Foundations: Student Home Page");
		theStage.setScene(theStudentHomeScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: ViewStudentHome() </p>
	 * 
	 * <p> Description: Constructor that initializes all GUI elements.</p>
	 */
	private ViewStudentHome() {
		theRootPane = new Pane();
		theStudentHomeScene = new Scene(theRootPane, width, height);
		
		// GUI Area 1: Title and user info
		label_PageTitle.setText("Student Discussion Home");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((event) -> 
			{guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, theUser);});
		
		// GUI Area 2: Create Post section
		setupLabelUI(label_CreatePost, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 105);
		
		setupLabelUI(label_PostTitle, "Arial", 16, 100, Pos.BASELINE_RIGHT, 20, 140);
		setupTextUI(text_PostTitle, "Arial", 16, 550, Pos.BASELINE_LEFT, 130, 135, true);
		text_PostTitle.setPromptText("Enter post title (3-200 characters)");
		
		setupLabelUI(label_PostContent, "Arial", 16, 100, Pos.BASELINE_LEFT, 20, 170);
		text_PostContent.setLayoutX(130);
		text_PostContent.setLayoutY(170);
		text_PostContent.setPrefWidth(550);
		text_PostContent.setPrefHeight(100);
		text_PostContent.setWrapText(true);
		text_PostContent.setPromptText("Enter post content (10-5000 characters)");
		
		setupLabelUI(label_PostThread, "Arial", 16, 100, Pos.BASELINE_RIGHT, 20, 285);
		setupTextUI(text_PostThread, "Arial", 16, 200, Pos.BASELINE_LEFT, 130, 280, true);
		text_PostThread.setPromptText("Default: General");
		text_PostThread.setText("General");
		
		setupButtonUI(button_CreatePost, "Dialog", 16, 150, Pos.CENTER, 350, 315);
		button_CreatePost.setOnAction((event) -> 
			{ControllerStudentHome.createPost();});
		
		// GUI Area 3: View/Manage Posts
		setupButtonUI(button_ViewMyPosts, "Dialog", 16, 200, Pos.CENTER, 50, 380);
		button_ViewMyPosts.setOnAction((event) -> 
			{ControllerStudentHome.viewMyPosts();});
		
		setupButtonUI(button_SearchPosts, "Dialog", 16, 200, Pos.CENTER, 300, 380);
		button_SearchPosts.setOnAction((event) -> 
			{ControllerStudentHome.searchPosts();});
		
		setupButtonUI(button_ViewAllPosts, "Dialog", 16, 200, Pos.CENTER, 550, 380);
		button_ViewAllPosts.setOnAction((event) -> 
			{ControllerStudentHome.viewAllPosts();});
		
		// GUI Area 4: Navigation
		setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Logout.setOnAction((event) -> 
			{ControllerStudentHome.performLogout();});
		
		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
		button_Quit.setOnAction((event) -> 
			{ControllerStudentHome.performQuit();});
		
		// Add all elements to pane
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
			label_CreatePost, label_PostTitle, text_PostTitle,
			label_PostContent, text_PostContent,
			label_PostThread, text_PostThread, button_CreatePost,
			line_Separator2,
			button_ViewMyPosts, button_SearchPosts, button_ViewAllPosts,
			line_Separator3,
			button_Logout, button_Quit
		);
	}
	
	/*-*******************************************************************************************
	Helper methods
	*/
	
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, 
			double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}
	
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, 
			double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}

	private static void setupTextUI(TextField t, String ff, double f, double w, Pos p, 
			double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);
		t.setEditable(e);
	}
}