package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <p>Title: Reply Class</p>
 * 
 * <p>Description: Represents a reply to a post in the discussion system.
 * This class encapsulates all the data associated with a single reply including
 * content, author information, timestamps, and the parent post reference.</p>
 * 
 * <p>Copyright: Student Discussion System © 2025</p>
 * 
 * @author Your Name
 * @version 1.00 2025-01-15 Initial implementation
 */
public class Reply {
    
    // Constants for validation
    public static final int MIN_CONTENT_LENGTH = 5;
    public static final int MAX_CONTENT_LENGTH = 3000;
    
    // Attributes
    private String replyId;             // Unique identifier for the reply
    private String postId;              // ID of the post this replies to
    private String authorUsername;      // Username of the reply author
    private String content;             // Content/body of the reply
    private LocalDateTime createdAt;    // Timestamp when reply was created
    private LocalDateTime updatedAt;    // Timestamp of last update
    private boolean isDeleted;          // Soft delete flag
    
    /**
     * Default constructor - creates an empty reply
     */
    public Reply() {
        this.replyId = generateReplyId();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
    }
    
    /**
     * Parameterized constructor for creating a new reply
     * 
     * @param postId The ID of the post being replied to
     * @param authorUsername The username of the reply author
     * @param content The content of the reply
     * @throws IllegalArgumentException if any validation fails
     */
    public Reply(String postId, String authorUsername, String content) {
        this();
        setPostId(postId);
        setAuthorUsername(authorUsername);
        setContent(content);
    }
    
    /**
     * Generates a unique reply ID using timestamp and random component
     * 
     * @return A unique reply identifier string
     */
    private String generateReplyId() {
        return "REPLY-" + System.currentTimeMillis() + "-" + 
               (int)(Math.random() * 10000);
    }
    
    // Getters
    public String getReplyId() { return replyId; }
    public String getPostId() { return postId; }
    public String getAuthorUsername() { return authorUsername; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public boolean isDeleted() { return isDeleted; }
    
    /**
     * Sets the post ID this reply belongs to
     * 
     * @param postId The post ID to set
     * @throws IllegalArgumentException if postId is null or empty
     */
    public void setPostId(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post ID cannot be null or empty. A reply must be associated with a post.");
        }
        this.postId = postId.trim();
    }
    
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
     * Sets the reply content with validation
     * 
     * @param content The content to set
     * @throws IllegalArgumentException if content fails validation
     */
    public void setContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Reply content cannot be null or empty.");
        }
        
        String trimmedContent = content.trim();
        
        if (trimmedContent.length() < MIN_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                "Reply content must be at least " + MIN_CONTENT_LENGTH + 
                " characters long. Please provide a more detailed response.");
        }
        
        if (trimmedContent.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                "Reply content cannot exceed " + MAX_CONTENT_LENGTH + 
                " characters. Please be more concise.");
        }
        
        this.content = trimmedContent;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Marks this reply as deleted (soft delete)
     * This preserves the reply but indicates it's been removed
     */
    public void markAsDeleted() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * Restores a deleted reply
     */
    public void restore() {
        this.isDeleted = false;
        this.updatedAt = LocalDateTime.now();
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
     * Returns a string representation of the reply
     * 
     * @return String representation including key fields
     */
    @Override
    public String toString() {
        return String.format(
            "Reply[ID=%s, PostID=%s, Author=%s, Deleted=%s]",
            replyId, postId, authorUsername, isDeleted);
    }
    
    /**
     * Creates a summary view of the reply for display
     * 
     * @return A formatted summary string
     */
    public String getSummary() {
        String status = isDeleted ? "[DELETED] " : "";
        String preview = content.length() > 150 ? 
            content.substring(0, 147) + "..." : content;
        
        return String.format("%s%s\nBy: %s | %s",
            status, preview, authorUsername, getFormattedCreatedAt());
    }
}