package entityClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Title: PostCollection Class</p>
 * 
 * <p>Description: Manages a collection of Post objects with full CRUD operations
 * and search/filter capabilities. This class supports storing all posts in the
 * system as well as creating and managing subsets based on various criteria.</p>
 * 
 * <p>Copyright: Student Discussion System © 2025</p>
 * 
 * @author Your Name
 * @version 1.00 2025-10-16 Initial implementation for HW2
 */
public class PostCollection {
    
    // The main storage for all posts
    private List<Post> posts;
    
    /**
     * Default constructor - creates an empty collection
     */
    public PostCollection() {
        this.posts = new ArrayList<>();
    }
    
    /**
     * Constructor that accepts an initial list of posts
     * This is useful for creating subsets
     * 
     * @param posts Initial list of posts
     */
    public PostCollection(List<Post> posts) {
        this.posts = new ArrayList<>(posts);
    }
    
    // ==================== CREATE ====================
    
    /**
     * Adds a new post to the collection
     * 
     * @param post The post to add
     * @return true if post was added successfully
     * @throws IllegalArgumentException if post is null or already exists
     */
    public boolean addPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException(
                "Cannot add a null post to the collection.");
        }
        
        // Check if post with same ID already exists
        if (findPostById(post.getPostId()) != null) {
            throw new IllegalArgumentException(
                "A post with ID " + post.getPostId() + " already exists.");
        }
        
        return posts.add(post);
    }
    
    /**
     * Creates and adds a new post with the given parameters
     * 
     * @param authorUsername The author's username
     * @param title The post title
     * @param content The post content
     * @param thread The thread category
     * @return The newly created Post object
     * @throws IllegalArgumentException if validation fails
     */
    public Post createPost(String authorUsername, String title, 
                          String content, String thread) {
        Post post = new Post(authorUsername, title, content, thread);
        addPost(post);
        return post;
    }
    
    // ==================== READ ====================
    
    /**
     * Retrieves all posts in the collection
     * 
     * @return A list of all posts (including deleted ones)
     */
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts);
    }
    
    /**
     * Retrieves all active (non-deleted) posts
     * 
     * @return A list of active posts
     */
    public List<Post> getActivePosts() {
        return posts.stream()
                   .filter(post -> !post.isDeleted())
                   .collect(Collectors.toList());
    }
    
    /**
     * Finds a post by its unique ID
     * 
     * @param postId The ID to search for
     * @return The Post object if found, null otherwise
     */
    public Post findPostById(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            return null;
        }
        
        return posts.stream()
                   .filter(post -> post.getPostId().equals(postId))
                   .findFirst()
                   .orElse(null);
    }
    
    /**
     * Retrieves all posts in a specific thread
     * 
     * @param thread The thread name to search for
     * @return A PostCollection containing posts in that thread
     */
    public PostCollection getPostsByThread(String thread) {
        if (thread == null || thread.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Post> threadPosts = posts.stream()
            .filter(post -> post.getThread().equalsIgnoreCase(thread.trim()))
            .collect(Collectors.toList());
        
        return new PostCollection(threadPosts);
    }
    
    /**
     * Retrieves all active posts in a specific thread
     * 
     * @param thread The thread name to search for
     * @return A PostCollection containing active posts in that thread
     */
    public PostCollection getActivePostsByThread(String thread) {
        if (thread == null || thread.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Post> threadPosts = posts.stream()
            .filter(post -> post.getThread().equalsIgnoreCase(thread.trim()))
            .filter(post -> !post.isDeleted())
            .collect(Collectors.toList());
        
        return new PostCollection(threadPosts);
    }
    
    /**
     * Retrieves all posts by a specific author
     * 
     * @param username The author's username
     * @return A PostCollection containing the author's posts
     */
    public PostCollection getPostsByAuthor(String username) {
        if (username == null || username.trim().isEmpty()) {
            return new PostCollection();
        }
        
        List<Post> authorPosts = posts.stream()
            .filter(post -> post.getAuthorUsername()
                               .equalsIgnoreCase(username.trim()))
            .collect(Collectors.toList());
        
        return new PostCollection(authorPosts);
    }
    
    /**
     * Searches for posts containing specific keywords in title or content
     * 
     * @param keyword The keyword to search for
     * @return A PostCollection containing matching posts
     * @throws IllegalArgumentException if keyword is invalid
     */
    public PostCollection searchPosts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Search keyword cannot be null or empty.");
        }
        
        if (keyword.trim().length() < 2) {
            throw new IllegalArgumentException(
                "Search keyword must be at least 2 characters long.");
        }
        
        final String searchTerm = keyword.trim().toLowerCase();
        List<Post> matchingPosts = posts.stream()
            .filter(post -> !post.isDeleted())
            .filter(post -> 
                post.getTitle().toLowerCase().contains(searchTerm) ||
                post.getContent().toLowerCase().contains(searchTerm))
            .collect(Collectors.toList());
        
        return new PostCollection(matchingPosts);
    }
    
    /**
     * Counts the number of posts in the collection
     * 
     * @return The number of posts (including deleted)
     */
    public int countAllPosts() {
        return posts.size();
    }
    
    /**
     * Counts the number of active posts
     * 
     * @return The number of active posts
     */
    public int countActivePosts() {
        return (int) posts.stream()
                         .filter(post -> !post.isDeleted())
                         .count();
    }
    
    /**
     * Counts posts in a specific thread
     * 
     * @param thread The thread name
     * @return The number of posts in that thread
     */
    public int countPostsInThread(String thread) {
        if (thread == null || thread.trim().isEmpty()) {
            return 0;
        }
        
        return (int) posts.stream()
                         .filter(post -> post.getThread()
                                            .equalsIgnoreCase(thread.trim()))
                         .count();
    }
    
    // ==================== UPDATE ====================
    
    /**
     * Updates an existing post's title
     * 
     * @param postId The ID of the post to update
     * @param newTitle The new title
     * @return true if update was successful
     * @throws IllegalArgumentException if post not found or validation fails
     */
    public boolean updatePostTitle(String postId, String newTitle) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot update a deleted post.");
        }
        
        post.setTitle(newTitle);
        return true;
    }
    
    /**
     * Updates an existing post's content
     * 
     * @param postId The ID of the post to update
     * @param newContent The new content
     * @return true if update was successful
     * @throws IllegalArgumentException if post not found or validation fails
     */
    public boolean updatePostContent(String postId, String newContent) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot update a deleted post.");
        }
        
        post.setContent(newContent);
        return true;
    }
    
    /**
     * Updates an existing post's thread
     * 
     * @param postId The ID of the post to update
     * @param newThread The new thread
     * @return true if update was successful
     * @throws IllegalArgumentException if post not found or validation fails
     */
    public boolean updatePostThread(String postId, String newThread) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot update a deleted post.");
        }
        
        post.setThread(newThread);
        return true;
    }
    
    // ==================== DELETE ====================
    
    /**
     * Soft deletes a post (marks as deleted but keeps in collection)
     * 
     * @param postId The ID of the post to delete
     * @return true if deletion was successful
     * @throws IllegalArgumentException if post not found
     */
    public boolean deletePost(String postId) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Post is already deleted.");
        }
        
        post.markAsDeleted();
        return true;
    }
    
    /**
     * Permanently removes a post from the collection
     * Use with caution - this cannot be undone
     * 
     * @param postId The ID of the post to remove
     * @return true if removal was successful
     * @throws IllegalArgumentException if post not found
     */
    public boolean permanentlyRemovePost(String postId) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        return posts.remove(post);
    }
    
    /**
     * Restores a previously deleted post
     * 
     * @param postId The ID of the post to restore
     * @return true if restoration was successful
     * @throws IllegalArgumentException if post not found
     */
    public boolean restorePost(String postId) {
        Post post = findPostById(postId);
        
        if (post == null) {
            throw new IllegalArgumentException(
                "Post with ID " + postId + " not found.");
        }
        
        if (!post.isDeleted()) {
            throw new IllegalArgumentException(
                "Post is not deleted and cannot be restored.");
        }
        
        post.restore();
        return true;
    }
    
    // ==================== UTILITY METHODS ====================
    
    /**
     * Gets the total number of posts in the collection
     * 
     * @return The total count
     */
    public int size() {
        return posts.size();
    }
    
    /**
     * Gets the number of active (non-deleted) posts
     * 
     * @return The count of active posts
     */
    public int activePostCount() {
        return (int) posts.stream()
                         .filter(post -> !post.isDeleted())
                         .count();
    }
    
    /**
     * Checks if the collection is empty
     * 
     * @return true if no posts exist
     */
    public boolean isEmpty() {
        return posts.isEmpty();
    }
    
    /**
     * Clears all posts from the collection
     */
    public void clear() {
        posts.clear();
    }
    
    /**
     * Returns a string representation of the collection
     * 
     * @return Summary string
     */
    @Override
    public String toString() {
        return String.format(
            "PostCollection[Total=%d, Active=%d, Deleted=%d]",
            size(), activePostCount(), size() - activePostCount());
    }
}