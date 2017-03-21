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

import com.agido.idea.settings.config.ConfigGroup;
import com.agido.idea.settings.config.ConfigOption;
import com.agido.idea.settings.config.SettingsEnforcerState;
import com.agido.idea.settings.config.SettingsEnforcerStateComponent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ExcludeFolder;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;

class IgnoreSettingsUpdater {
    static void update(Project project) {
        SettingsEnforcerStateComponent stateComponent = project.getComponent(SettingsEnforcerStateComponent.class);
        if (stateComponent != null) {
            SettingsEnforcerState state = stateComponent.getState();
            if (state != null && state.groups != null) {
                for (Module module : ModuleManager.getInstance(project).getModules()) {
                    update(module, state);
                }
            }
        }
    }

    private static void update(Module module, SettingsEnforcerState state) {
        ModifiableRootModel root = ModuleRootManager.getInstance(module).getModifiableModel();
        boolean commit = false;
        for (ConfigGroup configGroup : state.groups) {
            if (update(root, configGroup)) {
                commit = true;
            }
        }
        if (commit) {
            root.commit();
        } else {
            root.dispose();
        }
    }

    private static boolean update(ModifiableRootModel root, ConfigGroup config) {
        boolean commit = false;
        for (ContentEntry entry : root.getContentEntries()) {
            VirtualFile file = entry.getFile();
            if (file != null && file.getName().equals(config.module)) {
                if (update(config, entry, file)) {
                    commit = true;
                }
            }
        }
        return commit;
    }

    private static boolean update(ConfigGroup config, ContentEntry entry, VirtualFile file) {
        boolean commit = false;
        String url = file.getUrl();
        for (ConfigOption option : config.options) {
            if ("ignore".equals(option.name)) {
                if (addExcludeFolder(entry, url + "/" + option.value)) {
                    commit = true;
                }
            }
        }
        return commit;
    }

    private static boolean addExcludeFolder(ContentEntry entry, String url) {
        for (ExcludeFolder exclude : entry.getExcludeFolders()) {
            if (url.equals(exclude.getUrl())) {
                return false;
            }
        }
        entry.addExcludeFolder(url);
        //Notification.notify(String.format("added exclude folder '%s'", url), NotificationType.INFORMATION);
        return true;
    }
}
