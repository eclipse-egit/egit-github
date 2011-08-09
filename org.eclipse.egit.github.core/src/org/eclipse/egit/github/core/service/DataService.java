/******************************************************************************
 *  Copyright (c) 2011 GitHub Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *    Kevin Sawicki (GitHub Inc.) - initial API and implementation
 *****************************************************************************/
package org.eclipse.egit.github.core.service;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Blob;
import org.eclipse.egit.github.core.Commit;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.Reference;
import org.eclipse.egit.github.core.ShaResource;
import org.eclipse.egit.github.core.Tag;
import org.eclipse.egit.github.core.Tree;
import org.eclipse.egit.github.core.TreeEntry;
import org.eclipse.egit.github.core.TypedResource;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.IGitHubConstants;
import org.eclipse.egit.github.core.client.PagedRequest;

/**
 * Date service class for low-level access to Git repository data.
 */
public class DataService extends GitHubService {

	/**
	 * @param client
	 */
	public DataService(GitHubClient client) {
		super(client);
	}

	/**
	 * Get blob for given SHA-1
	 * 
	 * @param repository
	 * @param sha
	 * @return blob
	 * @throws IOException
	 */
	public Blob getBlob(IRepositoryIdProvider repository, String sha)
			throws IOException {
		final String id = getId(repository);
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_BLOBS);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setType(Blob.class);
		request.setUri(uri);
		return (Blob) client.get(request).getBody();
	}

	/**
	 * Create blob with given content
	 * 
	 * @param repository
	 * @param blob
	 * @return SHA-1 of created blob
	 * @throws IOException
	 */
	public String createBlob(IRepositoryIdProvider repository, Blob blob)
			throws IOException {
		final String id = getId(repository);
		if (blob == null)
			throw new IllegalArgumentException("Blob cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_BLOBS);
		ShaResource created = client.post(uri.toString(), blob,
				ShaResource.class);
		return created.getSha();
	}

	/**
	 * Get tree with given SHA-1
	 * 
	 * @param repository
	 * @param sha
	 * @return tree
	 * @throws IOException
	 */
	public Tree getTree(IRepositoryIdProvider repository, String sha)
			throws IOException {
		return getTree(repository, sha, false);
	}

	/**
	 * Get tree with given SHA-1
	 * 
	 * @param repository
	 * @param sha
	 * @param recursive
	 * @return tree
	 * @throws IOException
	 */
	public Tree getTree(IRepositoryIdProvider repository, String sha,
			boolean recursive) throws IOException {
		final String id = getId(repository);
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_TREES);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setType(Tree.class);
		request.setUri(uri);
		if (recursive)
			request.setParams(Collections.singletonMap("recursive", "1"));
		return (Tree) client.get(request).getBody();
	}

	/**
	 * Create tree
	 * 
	 * @param repository
	 * @param entries
	 * @return created tree
	 * @throws IOException
	 */
	public Tree createTree(IRepositoryIdProvider repository,
			Collection<TreeEntry> entries) throws IOException {
		return createTree(repository, entries, null);
	}

	/**
	 * Create tree
	 * 
	 * @param repository
	 * @param entries
	 * @param baseTree
	 * @return created tree
	 * @throws IOException
	 */
	public Tree createTree(IRepositoryIdProvider repository,
			Collection<TreeEntry> entries, String baseTree) throws IOException {
		final String id = getId(repository);
		if (entries == null)
			throw new IllegalArgumentException("Tree cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_TREES);
		GitHubRequest request = createRequest();
		request.setType(Tree.class);
		request.setUri(uri);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tree", entries.toArray());
		if (baseTree != null)
			params.put("base_tree", baseTree);
		return client.post(uri.toString(), params, Tree.class);
	}

	/**
	 * Get reference with given name
	 * 
	 * @param repository
	 * @param name
	 * @return reference
	 * @throws IOException
	 */
	public Reference getReference(IRepositoryIdProvider repository, String name)
			throws IOException {
		final String id = getId(repository);
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null"); //$NON-NLS-1$
		if (name.length() == 0)
			throw new IllegalArgumentException("Name cannot be empty"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		if (name.startsWith("refs/")) //$NON-NLS-1$
			uri.append('/');
		else
			uri.append(IGitHubConstants.SEGMENT_REFS);
		uri.append(name);
		GitHubRequest request = createRequest();
		request.setType(Reference.class);
		request.setUri(uri);
		return (Reference) client.get(request).getBody();
	}

	/**
	 * Get references for given repository
	 * 
	 * @param repository
	 * @return non-null but possibly empty list of references
	 * @throws IOException
	 */
	public List<Reference> getReferences(IRepositoryIdProvider repository)
			throws IOException {
		final String id = getId(repository);
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_REFS);
		PagedRequest<Reference> request = createPagedRequest();
		request.setType(new TypeToken<List<Reference>>() {
		}.getType());
		request.setUri(uri);
		return getAll(request);
	}

	/**
	 * Create reference
	 * 
	 * @param repository
	 * @param reference
	 * @throws IOException
	 */
	public void createReference(IRepositoryIdProvider repository,
			Reference reference) throws IOException {
		final String id = getId(repository);
		if (reference == null)
			throw new IllegalArgumentException("Reference cannot be null"); //$NON-NLS-1$
		TypedResource object = reference.getObject();
		if (object == null)
			throw new IllegalArgumentException("Object cannot be null"); //$NON-NLS-1$
		String sha = object.getSha();
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		String ref = reference.getRef();
		if (ref == null)
			throw new IllegalArgumentException("Ref cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_REFS);
		Map<String, String> params = new HashMap<String, String>();
		params.put("ref", ref); //$NON-NLS-1$
		params.put("sha", sha); //$NON-NLS-1$
		client.post(uri.toString(), params, null);
	}

	/**
	 * Edit reference
	 * 
	 * @param repository
	 * @param reference
	 * @param force
	 * @return updated reference
	 * @throws IOException
	 */
	public Reference editReference(IRepositoryIdProvider repository,
			Reference reference, boolean force) throws IOException {
		final String id = getId(repository);
		if (reference == null)
			throw new IllegalArgumentException("Reference cannot be null"); //$NON-NLS-1$
		TypedResource object = reference.getObject();
		if (object == null)
			throw new IllegalArgumentException("Object cannot be null"); //$NON-NLS-1$
		String ref = reference.getRef();
		if (ref == null)
			throw new IllegalArgumentException("Ref cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		if (ref.startsWith("refs/")) //$NON-NLS-1$
			uri.append('/');
		else
			uri.append(IGitHubConstants.SEGMENT_REFS);
		uri.append(ref);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sha", object.getSha()); //$NON-NLS-1$
		if (force)
			params.put("force", true); //$NON-NLS-1$
		return client.post(uri.toString(), params, Reference.class);
	}

	/**
	 * Get commit for given SHA-1
	 * 
	 * @param repository
	 * @param sha
	 * @return commit
	 * @throws IOException
	 */
	public Commit getCommit(IRepositoryIdProvider repository, String sha)
			throws IOException {
		final String id = getId(repository);
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		if (sha.length() == 0)
			throw new IllegalArgumentException("SHA-1 cannot be empty"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_COMMITS);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setType(Commit.class);
		request.setUri(uri);
		return (Commit) client.get(request).getBody();
	}

	/**
	 * Create commit in given repository
	 * 
	 * @param repository
	 * @param commit
	 * @return created commit
	 * @throws IOException
	 */
	public Commit createCommit(IRepositoryIdProvider repository, Commit commit)
			throws IOException {
		final String id = getId(repository);
		if (commit == null)
			throw new IllegalArgumentException("Commit cannot be null"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_COMMITS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("author", commit.getAuthor()); //$NON-NLS-1$
		params.put("committer", commit.getCommitter()); //$NON-NLS-1$
		params.put("message", commit.getMessage()); //$NON-NLS-1$
		List<String> parentIds = new ArrayList<String>();
		List<Commit> parents = commit.getParents();
		if (parents != null)
			for (Commit parent : parents)
				parentIds.add(parent.getSha());
		params.put("parents", parents); //$NON-NLS-1$
		Tree tree = commit.getTree();
		if (tree != null)
			params.put("tree", tree.getSha()); //$NON-NLS-1$
		return client.post(uri.toString(), params, Commit.class);
	}

	/**
	 * Get tag for given SHA-1
	 * 
	 * @param repository
	 * @param sha
	 * @return tag
	 * @throws IOException
	 */
	public Tag getTag(IRepositoryIdProvider repository, String sha)
			throws IOException {
		final String id = getId(repository);
		if (sha == null)
			throw new IllegalArgumentException("SHA-1 cannot be null"); //$NON-NLS-1$
		if (sha.length() == 0)
			throw new IllegalArgumentException("SHA-1 cannot be empty"); //$NON-NLS-1$
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_TAGS);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setType(Tag.class);
		request.setUri(uri);
		return (Tag) client.get(request).getBody();
	}

	/**
	 * Create tag object in given repository
	 * 
	 * @param repository
	 * @param tag
	 * @return created tag
	 * @throws IOException
	 */
	public Tag createTag(IRepositoryIdProvider repository, Tag tag)
			throws IOException {
		final String id = getId(repository);
		if (tag == null)
			throw new IllegalArgumentException("Tag cannot be null");
		StringBuilder uri = new StringBuilder();
		uri.append(IGitHubConstants.SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(IGitHubConstants.SEGMENT_GIT);
		uri.append(IGitHubConstants.SEGMENT_TAGS);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tag", tag.getTag());
		params.put("message", tag.getMessage());
		TypedResource object = tag.getObject();
		if (object != null) {
			params.put("object", object.getSha());
			params.put("type", object.getType());
		}
		params.put("tagger", tag.getTagger());
		return client.post(uri.toString(), params, Tag.class);
	}
}
