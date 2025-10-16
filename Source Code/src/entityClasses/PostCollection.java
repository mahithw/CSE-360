package entityClasses;

import entityClasses.Reply;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title: ReplyCollection Class</p>
 * 
 * <p>Description: Manages a collection of Reply objects with full CRUD operations
 * and search/filter capabilities. This class supports storing all replies in the
 * system as well as creating and managing subsets based on various criteria.</p>
 * 
 * <p>Copyright: Student Discussion System © 2025</p>
 * 
 * @author Your Name
 * @version 1.00 2025-01-15 Initial implementation
 */
public class PostCollection {
    
    // The main storage for all replies
    private List<Reply> replies;
    
    /**
     * Default constructor - creates an empty collection
     */
    public PostCollection() {
        this.replies = new ArrayList<>();
    }
    
    /**
     * Constructor that accepts an initial list of replies
     * This is useful for creating subsets
     * 
     * @param replies Initial list of replies
     */
    public PostCollection(List<Reply> replies) {
        this.replies = new ArrayList<>(replies);
    }
    
    // ==================== CREATE ====================
    
    /**
     * Adds a new reply to the collection
     * 
     * @param reply The reply to add
     * @return true if reply was added successfully
     * @throws IllegalArgumentException if reply is null or already exists
     */
    public boolean addReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException(
                "Cannot add a null reply to the collection.");
        }
        
        // Check if reply with same ID already exists
        if (findReplyById(reply.getReplyId()) != null) {
            throw new IllegalArgumentException(
                "A reply with ID " + reply.getReplyId() + " already exists.");
        }
        
        return replies.add(reply);
    }
    
    /**
     * Creates and adds a new reply with the given parameters
     * 
     * @param postId The ID of the post being replied to
     * @param authorUsername The author's username
     * @param content The reply content
     * @return The newly created Reply object
     * @throws IllegalArgumentException if validation fails
     */
    public Reply createReply(String postId, String authorUsername, String content) {
        Reply reply = new Reply(postId, authorUsername, content);
        addReply(reply);
        return reply;
    }
    
    // ==================== READ ====================
    
    /**
     * Retrieves all replies in the collection
     * 
     * @return A list of all replies (including deleted ones)
     */
    public List<Reply> getAllReplies() {
        return new ArrayList<>(replies);
    }
    
    /**
     * Retrieves all active (non-deleted) replies
     * 
     * @return A list of active replies
     */
    public List<Reply> getActiveReplies() {
        return replies.stream()
                     .filter(reply -> !reply.isDeleted())
                     .collect(Collectors.toList());
    }
    
    /**
     * Finds a reply by its unique ID
     * 
     * @param replyId The ID to search for
     * @return The Reply object if found, null otherwise
     */
    public Reply findReplyById(String replyId) {
        if (replyId == null || replyId.trim().isEmpty()) {
            return null;
        }
        
        return replies.stream()
                     .filter(reply -> reply.getReplyId().equals(replyId))
                     .findFirst()
                     .orElse(null);
    }
    
    /**
     * Retrieves all replies to a specific post
     * 
     * @param postId The post ID to search for
     * @return A ReplyCollection containing replies to that post
     */
    public PostCollection getRepliesByPostId(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Reply> postReplies = replies.stream()
            .filter(reply -> reply.getPostId().equals(postId.trim()))
            .collect(Collectors.toList());
        
        return new PostCollection(postReplies);
    }
    
    /**
     * Retrieves all active replies to a specific post
     * 
     * @param postId The post ID to search for
     * @return A ReplyCollection containing active replies to that post
     */
    public PostCollection getActiveRepliesByPostId(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Reply> postReplies = replies.stream()
            .filter(reply -> reply.getPostId().equals(postId.trim()))
            .filter(reply -> !reply.isDeleted())
            .collect(Collectors.toList());
        
        return new PostCollection(postReplies);
    }
    
    /**
     * Retrieves all replies by a specific author
     * 
     * @param username The author's username
     * @return A ReplyCollection containing the author's replies
     */
    public PostCollection getRepliesByAuthor(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Reply> authorReplies = replies.stream()
            .filter(reply -> reply.getAuthorUsername()
                                 .equalsIgnoreCase(username.trim()))
            .collect(Collectors.toList());
        
        return new PostCollection(authorReplies);
    }
    
    /**
     * Searches for replies containing specific keywords
     * 
     * @param keyword The keyword to search for
     * @return A ReplyCollection containing matching replies
     * @throws IllegalArgumentException if keyword is invalid
     */
    public PostCollection searchReplies(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Search keyword cannot be null or empty.");
        }
        
        if (keyword.trim().length() < 2) {
            throw new IllegalArgumentException(
                "Search keyword must be at least 2 characters long.");
        }
        
        final String searchTerm = keyword.trim().toLowerCase();
        List<Reply> matchingReplies = replies.stream()
            .filter(reply -> !reply.isDeleted())
            .filter(reply -> 
                reply.getContent().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
        
        return new PostCollection(matchingReplies);
    }
    
    /**
     * Counts the number of replies to a specific post
     * 
     * @param postId The post ID
     * @return The number of replies (including deleted)
     */
    public int countRepliesForPost(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return 0;
        }
        
        return (int) replies.stream()
                           .filter(reply -> reply.getPostId().equals(postId))
                           .count();
    }
    
    /**
     * Counts the number of active replies to a specific post
     * 
     * @param postId The post ID
     * @return The number of active replies
     */
    public int countActiveRepliesForPost(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return 0;
        }
        
        return (int) replies.stream()
                           .filter(reply -> reply.getPostId().equals(postId))
                           .filter(reply -> !reply.isDeleted())
                           .count();
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Updates an existing reply's content
     * 
     * @param replyId The ID of the reply to update
     * @param newContent The new content
     * @return true if update was successful
     * @throws IllegalArgumentException if reply not found or validation fails
     */
    public boolean updateReplyContent(String replyId, String newContent) {
        Reply reply = findReplyById(replyId);
        
        if (reply == null) {
            throw new IllegalArgumentException(
                "Reply with ID " + replyId + " not found.");
        }
        
        if (reply.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot update a deleted reply.");
        }
        
        reply.setContent(newContent);
        return true;
    }
    
    // ==================== DELETE ====================
    
    /**
     * Soft deletes a reply (marks as deleted but keeps in collection)
     * 
     * @param replyId The ID of the reply to delete
     * @return true if deletion was successful
     * @throws IllegalArgumentException if reply not found
     */
    public boolean deleteReply(String replyId) {
        Reply reply = findReplyById(replyId);
        
        if (reply == null) {
            throw new IllegalArgumentException(
                "Reply with ID " + replyId + " not found.");
        }
        
        if (reply.isDeleted()) {
            throw new IllegalArgumentException(
                "Reply is already deleted.");
        }
        
        reply.markAsDeleted();
        return true;
    }
    
    /**
     * Permanently removes a reply from the collection
     * Use with caution - this cannot be undone
     * 
     * @param replyId The ID of the reply to remove
     * @return true if removal was successful
     * @throws IllegalArgumentException if reply not found
     */
    public boolean permanentlyRemoveReply(String replyId) {
        Reply reply = findReplyById(replyId);
        
        if (reply == null) {
            throw new IllegalArgumentException(
                "Reply with ID " + replyId + " not found.");
        }
        
        return replies.remove(reply);
    }
    
    /**
     * Deletes all replies associated with a specific post
     * Useful when a post is deleted
     * 
     * @param postId The post ID
     * @return The number of replies deleted
     */
    public int deleteRepliesForPost(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return 0;
        }
        
        List<Reply> postReplies = replies.stream()
            .filter(reply -> reply.getPostId().equals(postId))
            .filter(reply -> !reply.isDeleted())
            .collect(Collectors.toList());
        
        postReplies.forEach(Reply::markAsDeleted);
        return postReplies.size();
    }
    
    /**
     * Restores a previously deleted reply
     * 
     * @param replyId The ID of the reply to restore
     * @return true if restoration was successful
     * @throws IllegalArgumentException if reply not found
     */
    public boolean restoreReply(String replyId) {
        Reply reply = findReplyById(replyId);
        
        if (reply == null) {
            throw new IllegalArgumentException(
                "Reply with ID " + replyId + " not found.");
        }
        
        if (!reply.isDeleted()) {
            throw new IllegalArgumentException(
                "Reply is not deleted and cannot be restored.");
        }
        
        reply.restore();
        return true;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Gets the total number of replies in the collection
     * 
     * @return The total count
     */
    public int size() {
        return replies.size();
    }
    
    /**
     * Gets the number of active (non-deleted) replies
     * 
     * @return The count of active replies
     */
    public int activeReplyCount() {
        return (int) replies.stream()
                           .filter(reply -> !reply.isDeleted())
                           .count();
    }
    
    /**
     * Checks if the collection is empty
     * 
     * @return true if no replies exist
     */
    public boolean isEmpty() {
        return replies.isEmpty();
    }
    
    /**
     * Clears all replies from the collection
     */
    public void clear() {
        replies.clear();
    }
    
    /**
     * Returns a string representation of the collection
     * 
     * @return Summary string
     */
    @Override
    public String toString() {
        return String.format(
            "ReplyCollection[Total=%d, Active=%d, Deleted=%d]",
            size(), activeReplyCount(), size() - activeReplyCount());
    }
}