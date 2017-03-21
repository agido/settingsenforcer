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

public interface Group {
    ExtensionPointName<Group> EP_NAME = ExtensionPointName.create("com.agido.idea.settings.Group");

    String getId();

    static Map<String, Group> getGroups() {
        Map<String, Group> groups = new HashMap<>();

        for (Group group : Extensions.getExtensions(Group.EP_NAME)) {
            groups.put(group.getId(), group);
        }

        return groups;
    }
}
