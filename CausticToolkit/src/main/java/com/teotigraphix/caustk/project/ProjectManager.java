////////////////////////////////////////////////////////////////////////////////
// Copyright 2013 Michael Schmalle - Teoti Graphix, LLC
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
// http://www.apache.org/licenses/LICENSE-2.0 
// 
// Unless required by applicable law or agreed to in writing, software 
// distributed under the License is distributed on an "AS IS" BASIS, 
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and 
// limitations under the License
// 
// Author: Michael Schmalle, Principal Architect
// mschmalle at teotigraphix dot com
////////////////////////////////////////////////////////////////////////////////

package com.teotigraphix.caustk.project;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.teotigraphix.caustk.controller.ICaustkController;
import com.teotigraphix.caustk.core.CtkDebug;

/**
 * The project manager manages the single project loaded for an application.
 * <p>
 * The manager will have a root directory passed to it when it is created. All
 * project related files are stored within this directory.
 */
public class ProjectManager implements IProjectManager {

    private ICaustkController controller;

    private File sessionPreferencesFile;

    //----------------------------------
    // sessionPreferences
    //----------------------------------

    private SessionPreferences sessionPreferences;

    @Override
    public SessionPreferences getSessionPreferences() {
        return sessionPreferences;
    }

    //----------------------------------
    // applicationRoot
    //----------------------------------

    /**
     * The root application directory, all {@link Project}s are stored in the
     * <code>applicationRoot/projects</code> directory.
     */
    @Override
    public File getApplicationRoot() {
        return controller.getApplicationRoot();
    }

    public File getProjectDirectory() {
        return new File(getApplicationRoot(), "projects");
    }

    @Override
    public File getDirectory(String path) {
        File directory = new File(getProjectDirectory(), path);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directory;
    }

    protected File getAbsoluteProjectDirectory() {
        return getDirectory("projects");
    }

    //----------------------------------
    // project
    //----------------------------------

    private Project project;

    @Override
    public Project getProject() {
        return project;
    }

    //--------------------------------------------------------------------------
    // Constructor
    //--------------------------------------------------------------------------

    public ProjectManager(ICaustkController controller) {
        this.controller = controller;
    }

    //-------------------------------------------------------------------------
    // IProjectManager API
    //--------------------------------------------------------------------------

    @Override
    public void initialize() {
        CtkDebug.log("ProjectManager", "Initialize, setup/load .settings");

        File applicationRoot = controller.getApplicationRoot();

        sessionPreferencesFile = new File(applicationRoot, ".settings");

        if (!sessionPreferencesFile.exists()) {
            try {
                FileUtils.writeStringToFile(sessionPreferencesFile, "");
                sessionPreferences = new SessionPreferences();
                CtkDebug.log("ProjectManager", "Created new .settings file: "
                        + sessionPreferencesFile.getAbsolutePath());
                saveProjectPreferences();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (sessionPreferencesFile.exists()) {
                sessionPreferences = controller.getSerializeService().fromFile(
                        sessionPreferencesFile, SessionPreferences.class);
                CtkDebug.log("ProjectManager",
                        "Loaded .settings file: " + sessionPreferencesFile.getAbsolutePath());
            }
        }
    }

    @Override
    public boolean isProject(File file) {
        if (file.isAbsolute())
            return file.exists();
        return toProjectFile(file).exists();
    }

    @Override
    public void exit() throws IOException {
        save();
        Project oldProject = project;
        project.close();
        project = null;
        controller.trigger(new OnProjectManagerChange(oldProject, ProjectManagerChangeKind.Exit));
    }

    @Override
    public void save() throws IOException {
        // XXX project manager project.getFile absolute path doubled up
        CtkDebug.log("ProjectManager", "Save - " + project.getStateFile());

        sessionPreferences.put("lastProject", project.getDirectory().getPath());
        // set modified
        project.getInfo().setModified(new Date());

        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Save));

        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.SaveComplete));

        finalizeSaveComplete();
    }

    protected void finalizeSaveComplete() throws IOException {
        //System.out.println(">> SAVE_COMPLETE flushProjectFile()");
        CtkDebug.log("ProjectManager", "Save Complete, now saving project json file");

        String data = controller.getSerializeService().toPrettyString(project);
        FileUtils.writeStringToFile(project.getStateFile(), data);

        saveProjectPreferences();
    }

    private void saveProjectPreferences() throws IOException {
        String data = controller.getSerializeService().toPrettyString(sessionPreferences);
        FileUtils.writeStringToFile(sessionPreferencesFile, data);
    }

    @Override
    public Project load(File directory) throws IOException {
        if (directory.getName().contains("."))
            throw new IOException("Project is not a directory");

        if (directory.isAbsolute())
            throw new IOException("Project direcotry must not be absolute");

        File absoluteDir = getDirectory(directory.getPath());

        CtkDebug.log("ProjectManager", "Load - " + absoluteDir);

        project = controller.getSerializeService().fromFile(new File(absoluteDir, ".project"),
                Project.class);

        project.open();

        // all state objects are created here
        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Load));

        // all clients can now act on the deserialized state objects (IControllerComponent)
        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.LoadComplete));

        return project;
    }

    @Override
    public Project createProject(String relativePath) throws IOException {
        return createProject(new File(relativePath));
    }

    @Override
    public Project createProject(File file) throws IOException {
        if (file.getName().contains("."))
            throw new IOException("Project is not a directory");

        project = new Project();
        project.wakeup(controller);
        project.setInitializing(true);
        // set the project sub directory in the /projects directory
        project.setDirectory(new File(file.getPath()));
        project.setInfo(createInfo());
        project.open();
        CtkDebug.log("ProjectManager", "Create() - " + project.getAbsolutDirectory());
        controller.trigger(new OnProjectManagerChange(project, ProjectManagerChangeKind.Create));

        FileUtils.forceMkdir(project.getAbsolutDirectory());

        // save the new Project
        finalizeSaveComplete();

        // adding a LOAD event here to keep consistent with all startup.
        // whether a project is created or loaded on start, mediators will always
        // get a load event at start.
        //        controller.getDispatcher().trigger(
        //                new OnProjectManagerChange(project, ProjectManagerChangeKind.LOAD));

        project.setInitializing(false);

        return project;
    }

    @Override
    public void clear() {
        project.close();
        controller.trigger(new OnProjectManagerChange(project,
                ProjectManagerChangeKind.CloseComplete));
        project = null;
    }

    //--------------------------------------------------------------------------
    // 
    //--------------------------------------------------------------------------

    private ProjectInfo createInfo() {
        ProjectInfo info = new ProjectInfo();
        info.setName("Untitled Project");
        info.setAuthor("Untitled Author");
        info.setCreated(new Date());
        info.setModified(new Date());
        info.setDescription("A new project");
        return info;
    }

    private File toProjectFile(File file) {
        if (file.isAbsolute())
            return file;
        return new File(getAbsoluteProjectDirectory(), file.getPath());
    }

}
