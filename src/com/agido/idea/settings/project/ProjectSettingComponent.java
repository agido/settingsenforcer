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
package com.agido.idea.settings.project;

import com.agido.idea.settings.Group;
import com.agido.idea.settings.Setting;
import com.agido.idea.settings.config.ConfigGroup;
import com.agido.idea.settings.config.ConfigOption;
import com.agido.idea.settings.config.SettingsEnforcerState;
import com.agido.idea.settings.config.SettingsEnforcerStateComponent;
import com.agido.idea.settings.util.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;

import java.util.Map;

public class ProjectSettingComponent extends AbstractProjectComponent {
    private final Map<String, ProjectSetting> settings = Setting.getSettings(ProjectSetting.class);
    private final Map<String, Group> groups = Group.getGroups();

    public ProjectSettingComponent(Project project) {
        super(project);
    }

    @Override
    public void projectOpened() {
        SettingsEnforcerStateComponent stateComponent = myProject.getComponent(SettingsEnforcerStateComponent.class);
        if (stateComponent != null) {
            SettingsEnforcerState state = stateComponent.getState();
            if (state != null && state.groups != null) {
                for (ConfigGroup configGroup : state.groups) {
                    load(configGroup);
                }
            }
        }
    }

    private void load(ConfigGroup config) {
        if (config.id != null && config.module != null) {
            Notification.notify(String.format("found setting with id '%s' and module '%s'!", config.id, config.module), NotificationType.ERROR);
            return;
        }
        if (config.id == null) {
            if (config.module == null) {
                Notification.notify("found setting without id/module!", NotificationType.ERROR);
            }
            return;
        }
        Group group = groups.get(config.id);
        if (group == null) {
            Notification.notify(String.format("settings group '%s' not found, plugin not installed/active?", config.id), NotificationType.WARNING);
            return;
        }
        for (ConfigOption option : config.options) {
            ProjectSetting setting = settings.get(Setting.id(config.id, option.name));
            if (setting == null) {
                Notification.notify(String.format("setting %s not found!", Setting.id(config.id, option.name)), NotificationType.ERROR);
                continue;
            }
            String message = setting.apply(option.value, myProject);
            if (message != null) {
                Notification.notify(message, NotificationType.INFORMATION);
            }
        }
    }
}
