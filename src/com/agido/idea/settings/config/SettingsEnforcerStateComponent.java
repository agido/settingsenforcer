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
package com.agido.idea.settings.config;

import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;

@State(name = "SettingsEnforcer", storages = @Storage("settings_enforcer.xml"))
public class SettingsEnforcerStateComponent extends AbstractProjectComponent implements PersistentStateComponent<SettingsEnforcerState> {
    private SettingsEnforcerState myState = new SettingsEnforcerState();

    public SettingsEnforcerStateComponent(Project project) {
        super(project);
    }

    @Override
    public SettingsEnforcerState getState() {
        return myState;
    }

    @Override
    public void loadState(SettingsEnforcerState state) {
        XmlSerializerUtil.copyBean(state, myState);
    }
}
