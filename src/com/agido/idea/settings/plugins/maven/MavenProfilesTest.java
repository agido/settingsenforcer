package com.agido.idea.settings.plugins.maven;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;

public class MavenProfilesTest {
    @Test
    public void testToString() {
        for (String profiles : ImmutableList.of("", "a", "-a", "a,b", "a,-b", "a,-b", "-a,-b")) {
            MavenProfiles mavenProfiles = MavenProfiles.of(profiles);
            Assert.assertThat(profiles, is(mavenProfiles.toString()));
        }
    }

    @Test
    public void testGetEmptyProfile() {
        // GIVEN
        String profiles = "";

        // WHEN
        MavenProfiles result = MavenProfiles.of(profiles);

        // THEN
        Assert.assertTrue(result.getEnabledProfiles().isEmpty());
        Assert.assertTrue(result.getDisabledProfiles().isEmpty());
    }

    @Test
    public void testGetProfiles() {
        // GIVEN
        String profiles = "a,-b";

        // WHEN
        MavenProfiles result = MavenProfiles.of(profiles);

        // THEN
        Assert.assertThat(result.getEnabledProfiles(), hasItems("a"));
        Assert.assertThat(result.getDisabledProfiles(), hasItems("b"));
    }

    @Test
    public void testDiffEmpty() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet();
        MavenProfiles newProfiles = MavenProfiles.of("");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertTrue(result.getEnabledProfiles().isEmpty());
        Assert.assertTrue(result.getDisabledProfiles().isEmpty());
    }

    @Test
    public void testDiffOther() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet("other");
        MavenProfiles newProfiles = MavenProfiles.of("");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertTrue(result.getEnabledProfiles().isEmpty());
        Assert.assertTrue(result.getDisabledProfiles().isEmpty());
    }

    @Test
    public void testDiffEnable() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet("old");
        MavenProfiles newProfiles = MavenProfiles.of("new");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertThat(result.getEnabledProfiles(), hasItems("new"));
        Assert.assertTrue(result.getDisabledProfiles().isEmpty());
    }

    @Test
    public void testDiffDisableOther() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet("old");
        MavenProfiles newProfiles = MavenProfiles.of("-other");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertTrue(result.getEnabledProfiles().isEmpty());
        Assert.assertTrue(result.getDisabledProfiles().isEmpty());
    }

    @Test
    public void testDiffDisableOld() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet("old");
        MavenProfiles newProfiles = MavenProfiles.of("-old");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertTrue(result.getEnabledProfiles().isEmpty());
        Assert.assertThat(result.getDisabledProfiles(), hasItems("old"));
    }

    @Test
    public void testSwitch() {
        // GIVEN
        Set<String> enabledProfiles = Sets.newHashSet("old");
        MavenProfiles newProfiles = MavenProfiles.of("-old,new");

        // WHEN
        MavenProfiles result = MavenProfilesSetting.diff(enabledProfiles, newProfiles);

        // THEN
        Assert.assertThat(result.getEnabledProfiles(), hasItems("new"));
        Assert.assertThat(result.getDisabledProfiles(), hasItems("old"));
    }
}
