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
package com.agido.idea.settings.plugins.maven;

import com.agido.idea.settings.Group;
import com.agido.idea.settings.project.ProjectSetting;
import com.intellij.openapi.project.Project;
import org.jetbrains.idea.maven.project.MavenGeneralSettings;
import org.jetbrains.idea.maven.project.MavenImportingSettings;
import org.jetbrains.idea.maven.project.MavenProjectsManager;

public abstract class AbstractMavenSetting<T> extends ProjectSetting<T> {
    @Override
    public Class<? extends Group> getGroup() {
        return MavenGroup.class;
    }

    static MavenImportingSettings getImportingSettings(Project project) {
        return MavenProjectsManager.getInstance(project).getImportingSettings();
    }

    static MavenGeneralSettings getGeneralSettings(Project project) {
        return MavenProjectsManager.getInstance(project).getGeneralSettings();
    }
}
