/*
 *        Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 *         Evan DeGraff
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of
 *         this software and associated documentation files (the "Software"), to deal in
 *         the Software without restriction, including without limitation the rights to
 *         use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 *         the Software, and to permit persons to whom the Software is furnished to do so,
 *         subject to the following conditions:
 *
 *         The above copyright notice and this permission notice shall be included in all
 *         copies or substantial portions of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *         IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *         FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *         COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *         IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *         CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.model;

import io.searchbox.Action;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;

import java.util.Collection;
import java.util.UUID;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * Class for interacting with the ES service
 * The latest error string can be retrieved via getErrorString
 */
public class WebStorage {
	
	private JestClient client;
	private String errorMessage;
	
	/**
	 * Construct a basic WebStorage object
	 */
	public WebStorage() {
		client = ES.Client.getClient();
	}

	/**
	 * Gets a story from ES
	 * @param storyId ID of the story to retrieve
	 * @return The Story object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public Story getStory(UUID storyId) throws Exception {
		Get get = new Get.Builder("stories", storyId.toString()).build();
		JestResult result = execute(get);
		return result.getSourceAsObject(Story.class);
	}
	
	/**
	 * Gets all stories from ES
	 * @return Collection of stories
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public Collection<Story> getAllStories() throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchAllQuery());
		Search search = new Search.Builder(ssb.toString())
			.addIndex("stories")
			.addType("story")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(Story.class);
	}
	
	/**
	 * Gets a fragment from ES
	 * All fragments are UUID so story reference is not needed
	 * @param fragmentId ID of the fragment to retrieve
	 * @return The StoryFragment object
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public StoryFragment getFragment(UUID fragmentId) throws Exception {
		Get get = new Get.Builder("fragments", fragmentId.toString()).build();
		JestResult result = execute(get);
		return result.getSourceAsObject(StoryFragment.class);
	}
	
	/**
	 * Gets all fragments for a given story
	 * @param storyId ID of the story to retrieve all fragments for
	 * @return Collection of StoryFragments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public Collection<StoryFragment> getAllFragmentsForStory(UUID storyId) throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchQuery("storyId", storyId.toString()));
		Search search = new Search.Builder(ssb.toString())
			.addIndex("fragments")
			.addType("fragment")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(StoryFragment.class);
	}
	
	/**
	 * Gets a comment for the targetId. May be a StoryId or FragmentId.
	 * @param targetId. The Story or StoryFragment to retrieve comments for.
	 * @return A collection of comments
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public Collection<Comment> getComments(UUID targetId) throws Exception {
		SearchSourceBuilder ssb = new SearchSourceBuilder();
		ssb.query(QueryBuilders.matchQuery("targetId", targetId.toString()));
		Search search = new Search.Builder(ssb.toString())
			.addIndex("comments")
			.addType("comment")
			.build();
		
		JestResult result = execute(search);
		return result.getSourceAsObjectList(Comment.class);
	}

	/**
	 * Puts a comment to ES
	 * @param comment the Comment to save to ES
	 * @return True if succeeded, false otherwise.
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean putComment(Comment comment) throws Exception {
		Index index = new Index.Builder(comment).index("comments").type("comment").build();
		JestResult result = execute(index);
		return result.isSucceeded();
	}
	
	/**
	 * Deletes a comment from ES
	 * @param commentId ID of the comment to delete
	 * @return True if succeeded, false otherwise
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean deleteComment(UUID commentId) throws Exception {
		JestResult result = execute(new Delete.Builder("comments", "comment", commentId.toString()).build());
		return result.isSucceeded();
	}
	
	/**
	 * Publishes a Story to ES. Overwrites if already exists.
	 * Note: this does NOT check if the StoryFragments actually belong to the Story
	 * @param story the Story object to publish
	 * @param fragments Collection of StoryFragments to publish
	 * @return True if succeeded, false otherwise
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	public boolean publishStory(Story story, Collection<StoryFragment> fragments) throws Exception {
		// TODO: make this more clear on what part failed if it does fail
		
		// I am not cleaning up old fragments because I am assuming we do not support
		// deleting fragments. If we do support that, then I will have to clean them up.
		Index index = new Index.Builder(story).index("stories").type("story").build();
		JestResult resultStory = execute(index);
		
		Bulk.Builder bulkBuilder = new Bulk.Builder()
			.defaultIndex("fragments")
			.defaultType("fragment");
		
		for (StoryFragment f : fragments) {
			bulkBuilder.addAction(new Index.Builder(f).build());
		}
		
		JestResult resultFragments = execute(bulkBuilder.build());
		return resultStory.isSucceeded() && resultFragments.isSucceeded();
	}

	/**
	 * Returns the latest ErrorMessage, or null if none exist.
	 * ...I think. Not sure how JestClient sets error message
	 * when it succeeds.
	 * @return The latest error message, if any
	 */
	public String getErrorMessagge() {
		return this.errorMessage;
	}
	
	/**
	 * Execute a client action and set this WebRequester's error message
	 * @param clientRequest the Action to execute
	 * @return The JestResult
	 * @throws Exception, connection errors, etc. See JestClient
	 */
	private JestResult execute(Action clientRequest) throws Exception {
		JestResult result = client.execute(clientRequest);
		this.errorMessage = result.getErrorMessage();
		return result;
	}
}
