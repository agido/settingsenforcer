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
package com.agido.idea.settings;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.Extensions;

import java.util.HashMap;
import java.util.Map;

public interface Setting<T> {
    ExtensionPointName<Setting> EP_NAME = ExtensionPointName.create("com.agido.idea.settings.Setting");

    Class<? extends Group> getGroup();

    Class<? extends Type<T>> getType();

    String getId();

    static Group getGroupInstance(Setting<?> setting) {
        return Extensions.findExtension(Group.EP_NAME, setting.getGroup());
    }

    static String id(Setting setting) {
        return id(getGroupInstance(setting).getId(), setting.getId());
    }

    static String id(String group, String name) {
        return group + ":" + name;
    }

    @SuppressWarnings("unchecked")
    static <T extends Setting> Map<String, T> getSettings(Class<T> clazz) {
        Map<String, T> settings = new HashMap<>();

        for (Setting setting : Extensions.getExtensions(Setting.EP_NAME)) {
            if (clazz.isInstance(setting)) {
                settings.put(Setting.id(setting), (T) setting);
            }
        }

        return settings;
    }
}
