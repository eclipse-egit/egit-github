package org.eclipse.mylyn.github.ui.internal;

import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.mylyn.github.internal.GitHub;
import org.eclipse.mylyn.github.internal.GitHubCredentials;
import org.eclipse.mylyn.github.internal.GitHubGist;
import org.eclipse.mylyn.github.internal.GitHubGists;
import org.eclipse.mylyn.github.internal.GitHubService;
import org.eclipse.mylyn.github.internal.GitHubServiceException;
import org.eclipse.mylyn.tasks.core.TaskRepository;
import org.eclipse.mylyn.tasks.ui.TasksUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;


public class GistsView extends ViewPart implements ITreeContentProvider,
		ILabelProvider {

	private TreeViewer viewer = null;

	@Override
	public void createPartControl(Composite parent) {
		PatternFilter patternFilter = new PatternFilter();
		FilteredTree filter = new FilteredTree(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL, patternFilter, true);
		viewer = filter.getViewer();
		viewer.setContentProvider(this);
		viewer.setLabelProvider(this);
		try {
			GitHubService service = new GitHubService();
			Set<TaskRepository> repositories = TasksUi.getRepositoryManager().getRepositories(GitHub.CONNECTOR_KIND);
			TaskRepository repository = repositories.iterator().next();
			GitHubCredentials credentials = GitHubCredentials.create(repository);
			viewer.setInput(service.getPublicGists(credentials));
		} catch (GitHubServiceException exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void setFocus() {
		if (viewer != null)
			viewer.getTree().setFocus();
	}



	/* CONTENT PROVIDER */

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof GitHubGists) {
			GitHubGists gists = (GitHubGists) inputElement;
			return gists.getGists();
		}
		return null;
	}

	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof GitHubGist) {
			GitHubGist gist = (GitHubGist) parentElement;
			return gist.getFiles();
		}
		return null;
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof GitHubGist) {
			GitHubGist gist = (GitHubGist) element;
			return gist.getFiles().length > 0;
		}
		return false;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
	}



	/* LABEL PROVIDER */

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	public boolean isLabelProperty(Object element, String property) {
		return true;
	}

	public Image getImage(Object element) {
		if (element instanceof GitHubGist) {
			// TODO return gist image
		}
		return null;
	}

	public String getText(Object element) {
		return element == null ? "" : element.toString();
	}
}
