/*
 * Copyright 2017 agido GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agido.idea.settings.ignore;

import com.intellij.ProjectTopics;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootAdapter;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.util.messages.MessageBusConnection;

public class IgnoreSettingComponent extends AbstractProjectComponent {
    private final static ProjectUpdater myTimer = new ProjectUpdater();

    public IgnoreSettingComponent(Project project) {
        super(project);
    }

    @Override
    public void projectOpened() {
        final MessageBusConnection myBusConnection = myProject.getMessageBus().connect();
        myBusConnection.subscribe(ProjectTopics.PROJECT_ROOTS, new MyRootChangesListener());
    }

    private class MyRootChangesListener extends ModuleRootAdapter {
        @Override
        public void rootsChanged(ModuleRootEvent event) {
            myTimer.update(myProject);
        }
    }
}
