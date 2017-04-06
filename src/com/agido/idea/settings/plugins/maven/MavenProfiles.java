package com.agido.idea.settings.plugins.maven;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.intellij.openapi.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.TreeSet;

public class MavenProfiles extends Pair.NonNull<TreeSet<String>, TreeSet<String>> {
    private MavenProfiles(@NotNull TreeSet<String> first, @NotNull TreeSet<String> second) {
        super(first, second);
    }

    public Set<String> getEnabledProfiles() {
        return first;
    }

    public Set<String> getDisabledProfiles() {
        return second;
    }

    @Override
    public String toString() {
        String enabled = Joiner.on(',').join(getEnabledProfiles());
        String disabled = Joiner.on(",-").join(getDisabledProfiles());
        if (enabled.isEmpty() && disabled.isEmpty()) {
            return "";
        } else if (enabled.isEmpty()) {
            return "-" + disabled;
        } else if (disabled.isEmpty()) {
            return enabled;
        }
        return Joiner.on(",-").join(enabled, disabled);
    }

    public static MavenProfiles of(String profiles) {
        TreeSet<String> enabled = Sets.newTreeSet();
        TreeSet<String> disabled = Sets.newTreeSet();
        for (String profile : Sets.newTreeSet(Splitter.on(",").split(profiles))) {
            profile = profile.trim();
            if (profile.isEmpty()) {
                continue;
            }
            if (profile.startsWith("-")) {
                disabled.add(profile.substring(1));
            } else {
                enabled.add(profile);
            }
        }
        return of(enabled, disabled);
    }

    public static MavenProfiles of(TreeSet<String> enabled, TreeSet<String> disabled) {
        return new MavenProfiles(enabled, disabled);
    }
}
