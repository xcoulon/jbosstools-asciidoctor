/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.asciidoc.texteditor.internal.util;

import static org.junit.Assert.fail;

import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.junit.Assert;
import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xcoulon
 *
 */
public class TestProject extends ExternalResource {

	/** The logger (what else?). */
	public static final Logger LOGGER = LoggerFactory.getLogger(TestProject.class);

	/** Name of the project to synchronize. */
	private final String projectName;

	/** The Project Synchronizator itself. */
	private ProjectSynchronizator synchronizor;
	
	/** The sample project using during the tests.*/
	private IProject project;

	/**
	 * Constructor
	 * 
	 * @param projectName
	 *            the name of the project to synchronize between the developer's
	 *            workspace and the junit runtime workspace.
	 */
	public TestProject(final String projectName) {
		this.projectName = projectName;
	}

	/**
	 * 
	 * @see org.junit.rules.ExternalResource#before()
	 */
	@Override
	protected void before() throws Throwable {
		long startTime = new Date().getTime();
		try {
			this.project = WorkbenchUtils.syncProject(projectName);
			Assert.assertNotNull("Project not found", project.exists());
			Assert.assertTrue("Project is not open", project.isOpen());
			synchronizor = new ProjectSynchronizator(project);
			ResourcesPlugin.getWorkspace().addResourceChangeListener(synchronizor);
			LOGGER.debug("Starting test with project {}", project.getName());
		} finally {
			long endTime = new Date().getTime();
			LOGGER.info("Test Workspace setup in " + (endTime - startTime) + "ms.");
		}
	}

	/**
	 * @see org.junit.rules.ExternalResource#after()
	 */
	@Override
	protected void after() {
		long startTime = new Date().getTime();
		try {
			LOGGER.info("Synchronizing the workspace back to its initial state...");
			// remove listener before sync' to avoid desync...
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.removeResourceChangeListener(synchronizor);
			synchronizor.resync();
		} catch (Exception e) {
			LOGGER.error("Failed to synchronize project " + projectName, e);
			fail("Failed to synchronize project " + projectName);
		} finally {
			final long endTime = new Date().getTime();
			LOGGER.info("Test Workspace sync'd back to original state in " + (endTime - startTime) + "ms.");
		}
	}

	/**
	 * Finds and returns the member resource identified by the given path in
	 * the underlying sample project, or <code>null</code> if no such resource exists.
	 * The supplied path may be absolute or relative; in either case, it is
	 * interpreted as relative to this resource. Trailing separators and the path's
	 * device are ignored. If the path is empty the sample project is returned. Parent
	 * references in the supplied path are discarded if they go above the workspace
	 * root.

	 * @param path the relative path to the member resource, must be a valid path or path segment
	 * @return the member resource, or <code>null</code> if no such
	 * 		resource exists
	 * @see #members()
	 * @see IPath#isValidPath(String)
	 * @see IPath#isValidSegment(String)
	 */
	public IResource findMember(String path) {
		return project.findMember(path);
	}
}
