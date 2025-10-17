package guiStudentHome;

import database.Database;
import entityClasses.Post;
import entityClasses.PostCollection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.UUID;

/*******
 * <p> Title: ControllerStudentHome Class. </p>
 *
 * <p> Description: Controller for Student Home page actions with H2 database persistence. </p>
 *
 * <p> Copyright:
 * Student Discussion System © 2025 </p>
 *
 * @version 3.00 — 2025-10-17
 */
public class ControllerStudentHome {

    protected static Database theDatabase = applicationMain.FoundationsMain.database;
    protected static PostCollection allPosts = new PostCollection();

    // =====================================================
    // ================ CREATE POST ========================
    // =====================================================

    protected static void createPost() {
        String title = ViewStudentHome.text_PostTitle.getText().trim();
        String content = ViewStudentHome.text_PostContent.getText().trim();
        String thread = ViewStudentHome.text_PostThread.getText().trim();
        String username = ViewStudentHome.theUser.getUserName();

        try {
            Post newPost = new Post(username, title, content, thread);
            allPosts.addPost(newPost);
            try { theDatabase.savePost(newPost); } catch (Exception ex) { ex.printStackTrace(); }

            new Alert(AlertType.INFORMATION, "Post created successfully!\nPost ID: " + newPost.getPostId()).showAndWait();

            ViewStudentHome.text_PostTitle.clear();
            ViewStudentHome.text_PostContent.clear();
            ViewStudentHome.text_PostThread.setText("General");

        } catch (IllegalArgumentException e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    // =====================================================
    // ================ VIEW ALL POSTS =====================
    // =====================================================

    protected static void viewAllPosts() {
        try {
            allPosts = new PostCollection(theDatabase.getAllPosts());
        } catch (Exception ex) {
            new Alert(AlertType.ERROR, "Error loading posts from database.").showAndWait();
            return;
        }

        if (allPosts.isEmpty()) {
            new Alert(AlertType.INFORMATION, "No posts have been created yet.").showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder("All Posts (" + allPosts.activePostCount() + " active):\n\n");
        for (Post p : allPosts.getActivePosts()) {
            sb.append("=".repeat(60)).append("\n");
            sb.append("ID: ").append(p.getPostId()).append("\n");
            sb.append("Title: ").append(p.getTitle()).append("\n");
            sb.append("Author: ").append(p.getAuthorUsername()).append("\n");
            sb.append("Thread: ").append(p.getThread()).append("\n");
            sb.append("Replies: ").append(p.getReplyCount()).append("\n\n");
        }

        new Alert(AlertType.INFORMATION, sb.toString()).showAndWait();
    }

    // =====================================================
    // ================ VIEW MY POSTS ======================
    // =====================================================

    protected static void viewMyPosts() {
        try {
            allPosts = new PostCollection(theDatabase.getAllPosts());
        } catch (Exception ex) {
            new Alert(AlertType.ERROR, "Error loading posts from database.").showAndWait();
            return;
        }

        String username = ViewStudentHome.theUser.getUserName();
        PostCollection myPosts = allPosts.getPostsByAuthor(username);

        if (myPosts.isEmpty()) {
            new Alert(AlertType.INFORMATION, "You haven't created any posts yet.").showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder("Your Posts (" + myPosts.activePostCount() + " active):\n\n");
        for (Post p : myPosts.getActivePosts()) {
            sb.append("=".repeat(60)).append("\n");
            sb.append("ID: ").append(p.getPostId()).append("\n");
            sb.append("Title: ").append(p.getTitle()).append("\n");
            sb.append("Thread: ").append(p.getThread()).append("\n");
            sb.append("Replies: ").append(p.getReplyCount()).append("\n\n");
        }

        new Alert(AlertType.INFORMATION, sb.toString()).showAndWait();
    }

    // =====================================================
    // ================ SEARCH POSTS =======================
    // =====================================================

    protected static void searchPosts() {
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("Search Posts");
        dialog.setHeaderText("Search for Posts");
        dialog.setContentText("Enter keyword:");
        Optional<String> result = dialog.showAndWait();

        if (!result.isPresent() || result.get().trim().isEmpty()) return;

        String keyword = result.get().trim();

        try {
            allPosts = new PostCollection(theDatabase.getAllPosts());
            PostCollection found = allPosts.searchPosts(keyword);

            if (found.isEmpty()) {
                new Alert(AlertType.INFORMATION, "No posts found matching \"" + keyword + "\".").showAndWait();
                return;
            }

            StringBuilder sb = new StringBuilder("Search Results for \"" + keyword + "\":\n\n");
            for (Post p : found.getActivePosts()) {
                sb.append("=".repeat(60)).append("\n");
                sb.append("ID: ").append(p.getPostId()).append("\n");
                sb.append("Title: ").append(p.getTitle()).append("\n");
                sb.append("Author: ").append(p.getAuthorUsername()).append("\n");
                sb.append("Thread: ").append(p.getThread()).append("\n");
                sb.append("Replies: ").append(p.getReplyCount()).append("\n\n");
            }

            new Alert(AlertType.INFORMATION, sb.toString()).showAndWait();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error searching posts: " + e.getMessage()).showAndWait();
        }
    }

    // =====================================================
    // ================ DELETE POST ========================
    // =====================================================

    protected static void deletePost() {
        String postId = ViewStudentHome.text_ReplyPostId.getText().trim();
        if (postId.isEmpty()) {
            new Alert(AlertType.ERROR, "Please enter a Post ID to delete.").showAndWait();
            return;
        }

        try {
            allPosts = new PostCollection(theDatabase.getAllPosts());
            Post post = allPosts.findPostById(postId);
            if (post == null) throw new IllegalArgumentException("No post found with ID " + postId);
            if (!post.getAuthorUsername().equals(ViewStudentHome.theUser.getUserName()))
                throw new IllegalArgumentException("You can only delete your own posts.");

            Alert confirm = new Alert(AlertType.CONFIRMATION, "Delete post titled: " + post.getTitle() + " ?", ButtonType.OK, ButtonType.CANCEL);
            Optional<ButtonType> r = confirm.showAndWait();

            if (r.isPresent() && r.get() == ButtonType.OK) {
                post.markAsDeleted();
                theDatabase.markPostDeleted(post.getPostId());
                new Alert(AlertType.INFORMATION, "Post deleted successfully.").showAndWait();
            }

        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    // =====================================================
    // ================ CREATE REPLY =======================
    // =====================================================

    protected static void createReply() {
        String postId = ViewStudentHome.text_ReplyPostId.getText().trim();
        String content = ViewStudentHome.text_ReplyContent.getText().trim();
        String username = ViewStudentHome.theUser.getUserName();

        if (postId.isEmpty() || content.isEmpty()) {
            new Alert(AlertType.ERROR, "Please enter Post ID and Reply Content.").showAndWait();
            return;
        }

        try {
            allPosts = new PostCollection(theDatabase.getAllPosts());
            Post post = allPosts.findPostById(postId);
            if (post == null) throw new IllegalArgumentException("No post found with ID " + postId);

            post.incrementReplyCount();
            String replyId = "R-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            theDatabase.saveReply(replyId, postId, username, content);
            theDatabase.updateReplyCount(postId, post.getReplyCount());

            new Alert(AlertType.INFORMATION, "Reply added successfully!").showAndWait();

            ViewStudentHome.text_ReplyPostId.clear();
            ViewStudentHome.text_ReplyContent.clear();

        } catch (Exception e) {
            new Alert(AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    // =====================================================
    // ================ VIEW REPLIES =======================
    // =====================================================

    protected static void viewRepliesForPost() {
        String postId = ViewStudentHome.text_ReplyPostId.getText().trim();
        if (postId.isEmpty()) {
            new Alert(AlertType.ERROR, "Please enter a Post ID.").showAndWait();
            return;
        }

        try {
            java.util.List<String> replies = theDatabase.getRepliesForPost(postId);
            if (replies.isEmpty()) {
                new Alert(AlertType.INFORMATION, "No replies found for this post.").showAndWait();
                return;
            }

            StringBuilder sb = new StringBuilder();
            for (String r : replies) sb.append(r).append("\n\n");

            new Alert(AlertType.INFORMATION, sb.toString()).showAndWait();
        } catch (Exception e) {
            new Alert(AlertType.ERROR, "Error viewing replies: " + e.getMessage()).showAndWait();
        }
    }

    // =====================================================
    // ================ LOGOUT / QUIT ======================
    // =====================================================

    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewStudentHome.theStage);
    }

    protected static void performQuit() {
        System.exit(0);
    }
}
