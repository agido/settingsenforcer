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
package com.agido.idea.settings.plugins.java.compiler;

import com.agido.idea.settings.types.StringType;
import com.intellij.openapi.project.Project;

public class BuildProcessVMOptionsSetting extends AbstractJavaCompilerSetting<String> {
    @Override
    public Class<StringType> getType() {
        return StringType.class;
    }

    @Override
    public String getId() {
        return "buildProcessVMOptions";
    }

    @Override
    protected String get(Project project) {
        return Compiler.getConfiguration(project).getBuildProcessVMOptions();
    }

    @Override
    protected void set(String value, Project project) {
        Compiler.getConfiguration(project).setBuildProcessVMOptions(value);
    }
}
