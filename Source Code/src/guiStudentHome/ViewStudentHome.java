package guiStudentHome;

import applicationMain.FoundationsMain;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewStudentHome Class. </p>
 *
 * <p> Description: Displays the Student Home GUI, providing the interface for
 * students to create posts, view posts, search, reply, and delete posts.
 * Buttons link directly to the ControllerStudentHome actions. </p>
 *
 * <p> Copyright:
 * Student Discussion System © 2025 </p>
 *
 * @version 3.00 — 2025-10-17
 */
public class ViewStudentHome {

    // =====================================================
    // ================   GUI COMPONENTS   =================
    // =====================================================

    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static User theUser;

    // Labels
    protected static Label label_PageTitle = new Label("Student Discussion System");
    protected static Label label_PostTitle = new Label("Post Title:");
    protected static Label label_PostContent = new Label("Post Content:");
    protected static Label label_PostThread = new Label("Thread:");
    protected static Label label_ReplyPostId = new Label("Post ID:");
    protected static Label label_ReplyContent = new Label("Reply Content:");

    // Text Fields / Areas
    protected static TextField text_PostTitle = new TextField();
    protected static TextField text_PostThread = new TextField("General");
    protected static TextArea text_PostContent = new TextArea();
    protected static TextField text_ReplyPostId = new TextField();
    protected static TextArea text_ReplyContent = new TextArea();

    // Buttons
    protected static Button button_CreatePost = new Button("Create Post");
    protected static Button button_ViewAllPosts = new Button("View All Posts");
    protected static Button button_ViewMyPosts = new Button("View My Posts");
    protected static Button button_SearchPosts = new Button("Search Posts");
    protected static Button button_CreateReply = new Button("Add Reply");
    protected static Button button_ViewReplies = new Button("View Replies");
    protected static Button button_DeletePost = new Button("Delete My Post");
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    // =====================================================
    // ================ DISPLAY METHOD =====================
    // =====================================================

    public static void displayStudentHome(Stage stage, User user) {
        theStage = stage;
        theUser = user;

        theRootPane = new Pane();
        Scene scene = new Scene(theRootPane, 750, 600);
        theStage.setTitle("Student Home");
        theStage.setScene(scene);

        label_PageTitle.setLayoutX(210);
        label_PageTitle.setLayoutY(20);
        label_PageTitle.setFont(new Font("Arial", 28));

        // Post creation section
        label_PostTitle.setLayoutX(40);
        label_PostTitle.setLayoutY(80);
        text_PostTitle.setLayoutX(130);
        text_PostTitle.setLayoutY(80);
        text_PostTitle.setPrefWidth(500);

        label_PostThread.setLayoutX(40);
        label_PostThread.setLayoutY(120);
        text_PostThread.setLayoutX(130);
        text_PostThread.setLayoutY(120);
        text_PostThread.setPrefWidth(200);

        label_PostContent.setLayoutX(40);
        label_PostContent.setLayoutY(160);
        text_PostContent.setLayoutX(130);
        text_PostContent.setLayoutY(160);
        text_PostContent.setPrefSize(500, 100);

        button_CreatePost.setLayoutX(130);
        button_CreatePost.setLayoutY(280);
        button_CreatePost.setOnAction(e -> ControllerStudentHome.createPost());

        // View buttons
        button_ViewAllPosts.setLayoutX(250);
        button_ViewAllPosts.setLayoutY(280);
        button_ViewAllPosts.setOnAction(e -> ControllerStudentHome.viewAllPosts());

        button_ViewMyPosts.setLayoutX(400);
        button_ViewMyPosts.setLayoutY(280);
        button_ViewMyPosts.setOnAction(e -> ControllerStudentHome.viewMyPosts());

        button_SearchPosts.setLayoutX(550);
        button_SearchPosts.setLayoutY(280);
        button_SearchPosts.setOnAction(e -> ControllerStudentHome.searchPosts());

        // Reply section
        label_ReplyPostId.setLayoutX(40);
        label_ReplyPostId.setLayoutY(340);
        text_ReplyPostId.setLayoutX(130);
        text_ReplyPostId.setLayoutY(340);

        label_ReplyContent.setLayoutX(40);
        label_ReplyContent.setLayoutY(380);
        text_ReplyContent.setLayoutX(130);
        text_ReplyContent.setLayoutY(380);
        text_ReplyContent.setPrefSize(500, 80);

        button_CreateReply.setLayoutX(130);
        button_CreateReply.setLayoutY(480);
        button_CreateReply.setOnAction(e -> ControllerStudentHome.createReply());

        button_ViewReplies.setLayoutX(260);
        button_ViewReplies.setLayoutY(480);
        button_ViewReplies.setOnAction(e -> ControllerStudentHome.viewRepliesForPost());

        button_DeletePost.setLayoutX(400);
        button_DeletePost.setLayoutY(480);
        button_DeletePost.setOnAction(e -> ControllerStudentHome.deletePost());

        button_Logout.setLayoutX(540);
        button_Logout.setLayoutY(540);
        button_Logout.setOnAction(e -> ControllerStudentHome.performLogout());

        button_Quit.setLayoutX(630);
        button_Quit.setLayoutY(540);
        button_Quit.setOnAction(e -> ControllerStudentHome.performQuit());

        theRootPane.getChildren().addAll(
                label_PageTitle, label_PostTitle, text_PostTitle,
                label_PostThread, text_PostThread, label_PostContent, text_PostContent,
                button_CreatePost, button_ViewAllPosts, button_ViewMyPosts, button_SearchPosts,
                label_ReplyPostId, text_ReplyPostId, label_ReplyContent, text_ReplyContent,
                button_CreateReply, button_ViewReplies, button_DeletePost,
                button_Logout, button_Quit
        );

        try {
            ControllerStudentHome.allPosts = new entityClasses.PostCollection(
                    FoundationsMain.database.getAllPosts());
        } catch (Exception e) {
            System.err.println("Could not load saved posts: " + e.getMessage());
        }

        theStage.show();
    }
}
