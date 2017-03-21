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

import com.intellij.compiler.impl.javaCompiler.BackendCompiler;
import com.intellij.openapi.project.Project;

public class DefaultCompilerSetting extends AbstractJavaCompilerSetting<BackendCompiler> {
    @Override
    public Class<BackendCompilerType> getType() {
        return BackendCompilerType.class;
    }

    @Override
    public String getId() {
        return "defaultCompiler";
    }

    @Override
    protected BackendCompiler get(Project project) {
        return Compiler.getConfiguration(project).getDefaultCompiler();
    }

    @Override
    protected void set(BackendCompiler value, Project project) {
        Compiler.getConfiguration(project).setDefaultCompiler(value);
    }
}
