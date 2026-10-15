package com.ondewo.sip.release;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The GitHub release body is sliced out of RELEASE.md by the Makefile's
 * {@code CURRENT_RELEASE_NOTES}: from the WHOLE heading line
 * {@code ## Release ONDEWO SIP Java Client <version>} (any heading level, version matched literally)
 * to the next {@code *****} line. A heading spelled any other way gives an empty slice, and
 * {@code gh release create -n ""} then publishes a release without notes and without an error.
 */
class ReleaseNotesTest {

    private static final String HEADING_PREFIX = "## Release ONDEWO SIP Java Client ";
    private static final Pattern SEPARATOR = Pattern.compile("^\\*{5}");

    private static List<String> releaseNotes;
    private static String makefile;

    @BeforeAll
    static void read() throws IOException {
        releaseNotes = Files.readAllLines(Paths.get("RELEASE.md"), StandardCharsets.UTF_8);
        makefile = new String(Files.readAllBytes(Paths.get("Makefile")), StandardCharsets.UTF_8);
    }

    /** Reproduces the Makefile's perl range {@code /^#+ Release ... \Q<version>\E$/../^\*{5}/}. */
    private static List<String> slice(final String version) {
        final Pattern heading =
                Pattern.compile("^#+ Release ONDEWO SIP Java Client " + Pattern.quote(version) + "$");
        final List<String> sliced = new ArrayList<>();
        boolean inside = false;
        for (final String line : releaseNotes) {
            if (!inside && heading.matcher(line).matches()) {
                inside = true;
            }
            if (inside) {
                sliced.add(line);
                if (sliced.size() > 1 && SEPARATOR.matcher(line).find()) {
                    break;
                }
            }
        }
        return sliced;
    }

    @Test
    void theMakefileSlicesTheHeadingThisTestChecks() {
        assertTrue(
                makefile.contains(
                        "perl -ne 'print if /^\\#+ Release ONDEWO SIP Java Client"
                                + " \\Q${ONDEWO_SIP_VERSION}\\E$$/../^\\*{5}/'"),
                "CURRENT_RELEASE_NOTES no longer uses the perl range this test reproduces");
    }

    /**
     * The slice is anchored: a version never opens the section of a longer one ({@code 9.1}
     * would open {@code 9.1.0}, {@code 9.1.0} would open {@code 19.1.0}).
     */
    @Test
    void aVersionNeverOpensTheSectionOfALongerVersion() {
        final Matcher version =
                Pattern.compile("^ONDEWO_SIP_VERSION=(\\S+)$", Pattern.MULTILINE)
                        .matcher(makefile);
        assertTrue(version.find());
        final String[] parts = version.group(1).split("\\.");
        assertTrue(
                slice(parts[0] + "." + parts[1]).isEmpty(),
                "the MAJOR.MINOR prefix of the version opened its section");
    }

    @Test
    void everyReleaseHeadingUsesTheSpellingTheMakefileSlices() {
        final List<String> headings =
                releaseNotes.stream()
                        .filter(line -> line.toLowerCase().contains("release ondewo"))
                        .collect(Collectors.toList());

        assertFalse(headings.isEmpty());
        for (final String heading : headings) {
            assertTrue(
                    heading.matches(Pattern.quote(HEADING_PREFIX) + "\\d+\\.\\d+\\.\\d+"),
                    "heading spelled differently: " + heading);
        }
    }

    @Test
    void everySectionEndsAtItsSeparator() {
        String open = null;
        for (final String line : releaseNotes) {
            if (line.startsWith(HEADING_PREFIX)) {
                assertEquals(
                        null, open, "no ***** separator between '" + open + "' and '" + line + "'");
                open = line;
            } else if (SEPARATOR.matcher(line).find()) {
                open = null;
            }
        }
        assertEquals(null, open, "the last section '" + open + "' is not closed by *****");
    }

    @Test
    void theCurrentVersionHasNonEmptyReleaseNotes() {
        final Matcher version =
                Pattern.compile("^ONDEWO_SIP_VERSION=(\\S+)$", Pattern.MULTILINE).matcher(makefile);
        assertTrue(version.find(), "ONDEWO_SIP_VERSION is not set in the Makefile");

        final List<String> sliced = slice(version.group(1));

        assertTrue(sliced.size() > 2, "RELEASE.md has no section for " + version.group(1));
        final long body =
                sliced.subList(1, sliced.size() - 1).stream()
                        .filter(line -> !line.trim().isEmpty())
                        .count();
        assertTrue(body > 1, "the release notes of " + version.group(1) + " are empty");
        assertTrue(SEPARATOR.matcher(sliced.get(sliced.size() - 1)).find());
    }
}
