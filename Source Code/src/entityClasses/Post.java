package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>Title: Post Class</p>
 * 
 * <p>Description: Represents a student post/question in the discussion system.
 * This class encapsulates all the data associated with a single post including
 * content, author information, timestamps, and thread categorization.</p>
 * 
 * <p>Copyright: Student Discussion System © 2025</p>
 * 
 * @author Your Name
 * @version 1.00 2025-01-15 Initial implementation
 */
public class Post {
    
    // Constants for validation
    public static final int MIN_TITLE_LENGTH = 3;
    public static final int MAX_TITLE_LENGTH = 200;
    public static final int MIN_CONTENT_LENGTH = 10;
    public static final int MAX_CONTENT_LENGTH = 5000;
    public static final String DEFAULT_THREAD = "General";
    
    // Attributes
    private String postId;              // Unique identifier for the post
    private String authorUsername;      // Username of the post creator
    private String title;               // Title/subject of the post
    private String content;             // Main content/body of the post
    private String thread;              // Discussion thread category
    private LocalDateTime createdAt;    // Timestamp when post was created
    private LocalDateTime updatedAt;    // Timestamp of last update
    private boolean isDeleted;          // Soft delete flag
    private int replyCount;             // Number of replies to this post
    
    /**
     * Default constructor - creates an empty post
     */
    public Post() {
        this.postId = generatePostId();
        this.thread = DEFAULT_THREAD;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.replyCount = 0;
    }
    
    /**
     * Parameterized constructor for creating a new post
     * 
     * @param authorUsername The username of the post author
     * @param title The title of the post
     * @param content The main content of the post
     * @param thread The discussion thread (category)
     * @throws IllegalArgumentException if any validation fails
     */
    public Post(String authorUsername, String title, String content, String thread) {
        this();
        setAuthorUsername(authorUsername);
        setTitle(title);
        setContent(content);
        setThread(thread);
    }
    
    /**
     * Generates a unique post ID using timestamp and random component
     * 
     * @return A unique post identifier string
     */
    private String generatePostId() {
        return "POST-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 10000);
    }
    
    // Getters
    public String getPostId() { return postId; }
    public String getAuthorUsername() { return authorUsername; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getThread() { return thread; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isDeleted() { return isDeleted; }
    public int getReplyCount() { return replyCount; }
    
    /**
     * Sets the author username with validation
     * 
     * @param authorUsername The username to set
     * @throws IllegalArgumentException if username is null or empty
     */
    public void setAuthorUsername(String authorUsername) {
        if (authorUsername == null || authorUsername.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Author username cannot be null or empty.");
        }
        this.authorUsername = authorUsername.trim();
    }
    
    /**
     * Sets the post title with validation
     * 
     * @param title The title to set
     * @throws IllegalArgumentException if title fails validation
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post title cannot be null or empty.");
        }
        
        String trimmedTitle = title.trim();
        
        if (trimmedTitle.length() < MIN_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                "Post title must be at least " + MIN_TITLE_LENGTH + 
                " characters long.");
        }
        
        if (trimmedTitle.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                "Post title cannot exceed " + MAX_TITLE_LENGTH + 
                " characters.");
        }
        
        this.title = trimmedTitle;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Sets the post content with validation
     * 
     * @param content The content to set
     * @throws IllegalArgumentException if content fails validation
     */
    public void setContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post content cannot be null or empty.");
        }
        
        String trimmedContent = content.trim();
        
        if (trimmedContent.length() < MIN_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                "Post content must be at least " + MIN_CONTENT_LENGTH + 
                " characters long. Please provide more details.");
        }
        
        if (trimmedContent.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                "Post content cannot exceed " + MAX_CONTENT_LENGTH + 
                " characters. Please be more concise.");
        }
        
        this.content = trimmedContent;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Sets the thread/category with validation
     * 
     * @param thread The thread name to set
     */
    public void setThread(String thread) {
        if (thread == null || thread.trim().isEmpty()) {
            this.thread = DEFAULT_THREAD;
        } else {
            this.thread = thread.trim();
        }
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Marks this post as deleted (soft delete)
     * This preserves the post for replies but indicates it's been removed
     */
    public void markAsDeleted() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Restores a deleted post
     */
    public void restore() {
        this.isDeleted = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Increments the reply count for this post
     */
    public void incrementReplyCount() {
        this.replyCount++;
    }
    
    /**
     * Decrements the reply count for this post
     */
    public void decrementReplyCount() {
        if (this.replyCount > 0) {
            this.replyCount--;
        }
    }
    
    /**
     * Gets a formatted timestamp string
     * 
     * @return Formatted date/time string
     */
    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }
    
    /**
     * Gets a formatted timestamp string for last update
     * 
     * @return Formatted date/time string
     */
    public String getFormattedUpdatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");
        return updatedAt.format(formatter);
    }
    
    /**
     * Returns a string representation of the post
     * 
     * @return String representation including key fields
     */
    @Override
    public String toString() {
        return String.format(
            "Post[ID=%s, Author=%s, Title=%s, Thread=%s, Replies=%d, Deleted=%s]",
            postId, authorUsername, title, thread, replyCount, isDeleted);
    }
    
    /**
     * Creates a summary view of the post for display in lists
     * 
     * @return A formatted summary string
     */
    public String getSummary() {
        String status = isDeleted ? "[DELETED] " : "";
        String preview = content.length() > 100 ? 
            content.substring(0, 97) + "..." : content;
        
        return String.format("%s%s\nBy: %s | Thread: %s | Replies: %d | %s",
            status, title, authorUsername, thread, replyCount, 
            getFormattedCreatedAt());
    }
}