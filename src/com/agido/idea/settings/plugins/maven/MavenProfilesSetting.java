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

import com.agido.idea.settings.types.StringType;
import com.google.common.collect.Sets;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.idea.maven.model.MavenExplicitProfiles;
import org.jetbrains.idea.maven.project.MavenProjectsManager;
import org.jetbrains.idea.maven.project.MavenProjectsTree;

import java.util.Collection;
import java.util.TreeSet;

public class MavenProfilesSetting extends AbstractMavenSetting<String> {
    @Override
    public Class<StringType> getType() {
        return StringType.class;
    }

    @Override
    public String getId() {
        return "profiles";
    }

    static MavenProfiles diff(Collection<String> activeProfiles, MavenProfiles newProfiles) {
        TreeSet<String> enabled = Sets.newTreeSet();
        TreeSet<String> disabled = Sets.newTreeSet();
        for (String profile : newProfiles.getEnabledProfiles()) {
            if (!activeProfiles.contains(profile)) {
                enabled.add(profile);
            }
        }
        for (String profile : newProfiles.getDisabledProfiles()) {
            if (activeProfiles.contains(profile)) {
                disabled.add(profile);
            }
        }
        return MavenProfiles.of(enabled, disabled);
    }

    @Override
    protected String get(Project project, String newValue) {
        Collection<String> enabledProfiles = getExplicitProfiles(project).getEnabledProfiles();
        MavenProfiles newProfiles = MavenProfiles.of(newValue);

        MavenProfiles diff = diff(enabledProfiles, newProfiles);

        if (diff.getEnabledProfiles().isEmpty() && diff.getDisabledProfiles().isEmpty()) {
            return newValue;
        }

        return diff.toString();
    }

    @Override
    protected void set(String value, Project project) {
        MavenExplicitProfiles profiles = getExplicitProfiles(project);
        Collection<String> enabledProfiles = Sets.newTreeSet(profiles.getEnabledProfiles());
        Collection<String> disabledProfiles = Sets.newTreeSet(profiles.getDisabledProfiles());

        MavenProfiles newProfiles = MavenProfiles.of(value);

        enabledProfiles.removeAll(newProfiles.getDisabledProfiles());
        enabledProfiles.addAll(newProfiles.getEnabledProfiles());

        disabledProfiles.removeAll(newProfiles.getEnabledProfiles());
        disabledProfiles.addAll(newProfiles.getDisabledProfiles());

        setExplicitProfiles(project, new MavenExplicitProfiles(enabledProfiles, disabledProfiles));
    }

    private MavenExplicitProfiles getExplicitProfiles(Project project) {
        return getMavenProjectsManager(project).getExplicitProfiles();
    }

    private static void ensureExplicitProfilesAreSet(MavenProjectsManager manager, MavenExplicitProfiles profiles) {
        @NotNull MavenExplicitProfiles explicitProfiles = manager.getExplicitProfiles();
        if (!explicitProfiles.getEnabledProfiles().containsAll(profiles.getEnabledProfiles())
                || !explicitProfiles.getDisabledProfiles().containsAll(profiles.getDisabledProfiles())
        ) {
            explicitProfiles.getEnabledProfiles().addAll(profiles.getEnabledProfiles());
            explicitProfiles.getDisabledProfiles().addAll(profiles.getDisabledProfiles());
            manager.setExplicitProfiles(explicitProfiles);
        }
    }

    private void setExplicitProfiles(Project project, MavenExplicitProfiles profiles) {
        MavenProjectsManager manager = getMavenProjectsManager(project);
        class Activity implements Runnable, DumbAware {
            @Override
            public void run() {
                ensureExplicitProfilesAreSet(manager, profiles);
                manager.addProjectsTreeListener(new MavenProjectsTree.Listener() {
                    @Override
                    public void profilesChanged() {
                        ensureExplicitProfilesAreSet(manager, profiles);
                    }
                });
            }
        }
        StartupManager.getInstance(project).registerPostStartupActivity(new Activity());
    }
}
