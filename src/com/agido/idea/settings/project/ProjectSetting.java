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

import com.agido.idea.settings.Setting;
import com.agido.idea.settings.Type;
import com.google.common.base.Objects;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.project.Project;

public abstract class ProjectSetting<T> implements Setting<T> {
    private T convert(String value, Project project) {
        Type<T> type = Extensions.findExtension(Type.EP_NAME, getType());
        return type.convert(value, project);
    }

    String apply(String value, Project project) {
        T oldValue = get(project);
        T newValue = convert(value, project);
        if (newValue == null) {
            return String.format("Invalid value '%s' for %s", value, Setting.id(this));
        } else if (!Objects.equal(oldValue, newValue)) {
            set(newValue, project);
            return String.format("Changed %s from '%s' to '%s'", Setting.id(this), oldValue, newValue);
        }
        return null;
    }

    protected abstract T get(Project project);

    protected abstract void set(T value, Project project);
}
