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
package com.agido.idea.settings.plugins.java.eclipse;

import com.agido.idea.settings.types.BooleanType;
import com.intellij.openapi.project.Project;

public class DeprecationSetting extends AbstractEclipseCompilerSetting<Boolean> {
    @Override
    public Class<BooleanType> getType() {
        return BooleanType.class;
    }

    @Override
    public String getId() {
        return "DEPRECATION";
    }

    @Override
    protected Boolean get(Project project, Boolean newValue) {
        return getOptions(project).DEPRECATION;
    }

    @Override
    protected void set(Boolean value, Project project) {
        getOptions(project).DEPRECATION = value;
    }
}
