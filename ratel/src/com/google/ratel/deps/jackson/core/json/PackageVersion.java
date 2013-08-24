package com.google.ratel.deps.jackson.core.json;

import com.google.ratel.deps.jackson.core.Version;
import com.google.ratel.deps.jackson.core.Versioned;
import com.google.ratel.deps.jackson.core.util.VersionUtil;

/**
 * Automatically generated from PackageVersion.java.in during packageVersion-generate execution of maven-replacer-plugin in pom.xml.
 */
public final class PackageVersion implements Versioned {

    public final static Version VERSION = VersionUtil.parseVersion(
        "2.2.2", "com.fasterxml.jackson.core", "jackson-core");

    @Override
    public Version version() {
        return VERSION;
    }
}