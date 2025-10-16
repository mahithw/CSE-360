package guiStudentHome;

import database.Database;
import entityClasses.Post;
import entityClasses.PostCollection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: ControllerStudentHome Class. </p>
 * 
 * <p> Description: Controller for Student Home page actions.</p>
 * 
 * <p> Copyright: Student Discussion System © 2025 </p>
 * 
 * @author Your Name
 * 
 * @version 1.00		2025-10-16 Initial version for HW2
 *  
 */

public class ControllerStudentHome {
	
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	// Store posts in memory for HW2 demonstration
	// In a real application, this would be in the database
	protected static PostCollection allPosts = new PostCollection();
	
	/**********
	 * <p> Method: createPost() </p>
	 * 
	 * <p> Description: Creates a new post with validation.</p>
	 */
	protected static void createPost() {
		String title = ViewStudentHome.text_PostTitle.getText();
		String content = ViewStudentHome.text_PostContent.getText();
		String thread = ViewStudentHome.text_PostThread.getText();
		String username = ViewStudentHome.theUser.getUserName();
		
		// Input validation
		try {
			// Create the post (validation happens in Post constructor)
			Post newPost = new Post(username, title, content, thread);
			
			// Add to collection
			allPosts.addPost(newPost);
			
			// Show success message
			Alert success = new Alert(AlertType.INFORMATION);
			success.setTitle("Post Created");
			success.setHeaderText("Success");
			success.setContentText("Your post has been created successfully!\n" +
					"Post ID: " + newPost.getPostId());
			success.showAndWait();
			
			// Clear input fields
			ViewStudentHome.text_PostTitle.setText("");
			ViewStudentHome.text_PostContent.setText("");
			ViewStudentHome.text_PostThread.setText("General");
			
			System.out.println("Post created: " + newPost.getPostId());
			
		} catch (IllegalArgumentException e) {
			// Show validation error
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Invalid Input");
			error.setHeaderText("Cannot Create Post");
			error.setContentText(e.getMessage());
			error.showAndWait();
		}
	}
	
	/**********
	 * <p> Method: viewMyPosts() </p>
	 * 
	 * <p> Description: Displays posts created by current user.</p>
	 */
	protected static void viewMyPosts() {
		String username = ViewStudentHome.theUser.getUserName();
		PostCollection myPosts = allPosts.getPostsByAuthor(username);
		
		if (myPosts.isEmpty()) {
			Alert info = new Alert(AlertType.INFORMATION);
			info.setTitle("My Posts");
			info.setHeaderText("No Posts Found");
			info.setContentText("You haven't created any posts yet.");
			info.showAndWait();
			return;
		}
		
		// Show posts in a dialog (simplified for HW2)
		StringBuilder postsText = new StringBuilder();
		postsText.append("Your Posts (" + myPosts.activePostCount() + " active):\n\n");
		
		for (Post post : myPosts.getActivePosts()) {
			postsText.append("=".repeat(60) + "\n");
			postsText.append("ID: ").append(post.getPostId()).append("\n");
			postsText.append("Title: ").append(post.getTitle()).append("\n");
			postsText.append("Thread: ").append(post.getThread()).append("\n");
			postsText.append("Created: ").append(post.getFormattedCreatedAt()).append("\n");
			postsText.append("Replies: ").append(post.getReplyCount()).append("\n");
			postsText.append("-".repeat(60) + "\n");
			String preview = post.getContent().length() > 100 ? 
					post.getContent().substring(0, 97) + "..." : post.getContent();
			postsText.append(preview).append("\n\n");
		}
		
		Alert info = new Alert(AlertType.INFORMATION);
		info.setTitle("My Posts");
		info.setHeaderText("Your Posts");
		info.setContentText(postsText.toString());
		info.showAndWait();
	}
	
	/**********
	 * <p> Method: searchPosts() </p>
	 * 
	 * <p> Description: Searches posts by keyword.</p>
	 */
	protected static void searchPosts() {
		// Show input dialog for search keyword
		javafx.scene.control.TextInputDialog dialog = 
				new javafx.scene.control.TextInputDialog();
		dialog.setTitle("Search Posts");
		dialog.setHeaderText("Search for Posts");
		dialog.setContentText("Enter search keyword:");
		
		java.util.Optional<String> result = dialog.showAndWait();
		if (!result.isPresent() || result.get().trim().isEmpty()) {
			return;
		}
		
		String keyword = result.get();
		
		try {
			PostCollection searchResults = allPosts.searchPosts(keyword);
			
			if (searchResults.isEmpty()) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setTitle("Search Results");
				info.setHeaderText("No Results");
				info.setContentText("No posts found matching: " + keyword);
				info.showAndWait();
				return;
			}
			
			// Display search results
			StringBuilder resultsText = new StringBuilder();
			resultsText.append("Search Results for '").append(keyword)
					   .append("' (").append(searchResults.activePostCount())
					   .append(" found):\n\n");
			
			for (Post post : searchResults.getActivePosts()) {
				resultsText.append("=".repeat(60) + "\n");
				resultsText.append("Title: ").append(post.getTitle()).append("\n");
				resultsText.append("Author: ").append(post.getAuthorUsername()).append("\n");
				resultsText.append("Thread: ").append(post.getThread()).append("\n");
				resultsText.append("Created: ").append(post.getFormattedCreatedAt()).append("\n\n");
			}
			
			Alert info = new Alert(AlertType.INFORMATION);
			info.setTitle("Search Results");
			info.setHeaderText("Posts Found");
			info.setContentText(resultsText.toString());
			info.showAndWait();
			
		} catch (IllegalArgumentException e) {
			Alert error = new Alert(AlertType.ERROR);
			error.setTitle("Search Error");
			error.setHeaderText("Invalid Search");
			error.setContentText(e.getMessage());
			error.showAndWait();
		}
	}
	
	/**********
	 * <p> Method: viewAllPosts() </p>
	 * 
	 * <p> Description: Displays all active posts.</p>
	 */
	protected static void viewAllPosts() {
		if (allPosts.isEmpty()) {
			Alert info = new Alert(AlertType.INFORMATION);
			info.setTitle("All Posts");
			info.setHeaderText("No Posts");
			info.setContentText("No posts have been created yet.");
			info.showAndWait();
			return;
		}
		
		StringBuilder postsText = new StringBuilder();
		postsText.append("All Posts (" + allPosts.activePostCount() + " active):\n\n");
		
		for (Post post : allPosts.getActivePosts()) {
			postsText.append("=".repeat(60) + "\n");
			postsText.append("Title: ").append(post.getTitle()).append("\n");
			postsText.append("Author: ").append(post.getAuthorUsername()).append("\n");
			postsText.append("Thread: ").append(post.getThread()).append("\n");
			postsText.append("Created: ").append(post.getFormattedCreatedAt()).append("\n");
			postsText.append("Replies: ").append(post.getReplyCount()).append("\n\n");
		}
		
		Alert info = new Alert(AlertType.INFORMATION);
		info.setTitle("All Posts");
		info.setHeaderText("Discussion Posts");
		info.setContentText(postsText.toString());
		info.showAndWait();
	}
	
	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: Logs out current user.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewStudentHome.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: Exits the application.</p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}