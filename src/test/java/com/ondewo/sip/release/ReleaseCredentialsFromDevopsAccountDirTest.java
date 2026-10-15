package com.ondewo.sip.release;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * ondewo-sip-api's release driver runs {@code make release_preflight} and
 * {@code make release_verify} STANDALONE, with nothing but an absolute {@code DEVOPS_ACCOUNT_DIR}
 * in the environment. The steps that need a credential read it from there - into that step's
 * environment only, never onto a command line or into the output - and a value that is already in
 * the environment wins.
 */
class ReleaseCredentialsFromDevopsAccountDirTest {

    private static final String[] NAMES = {
        "GITHUB_GH_TOKEN",
        "MAVEN_CENTRAL_USERNAME",
        "MAVEN_CENTRAL_PASSWORD",
        "MAVEN_GPG_KEY_B64",
        "MAVEN_GPG_PASSPHRASE"
    };

    private static boolean onPath(final String tool) {
        return Arrays.stream(System.getenv().getOrDefault("PATH", "").split(File.pathSeparator))
                .anyMatch(dir -> Files.isExecutable(Paths.get(dir, tool)));
    }

    private static List<String> recipe(final String target) throws IOException {
        final List<String> makefile = Files.readAllLines(Paths.get("Makefile"), StandardCharsets.UTF_8);
        int i = 0;
        while (!makefile.get(i).startsWith(target + ":")) {
            i++;
        }
        final List<String> lines = new java.util.ArrayList<>();
        for (i++; i < makefile.size() && !makefile.get(i).isEmpty(); i++) {
            if (makefile.get(i).startsWith("\t")) {
                lines.add(makefile.get(i).trim());
            }
        }
        return lines;
    }

    @Test
    void theStandaloneStepsThatNeedACredentialLoadItSilently() throws IOException {
        for (final String[] step :
                new String[][] {
                    {"release_preflight", "make check_release_credentials"},
                    {"release_preflight", "make validate_release_credentials_via_docker_image"},
                    {"release_verify", "make verify_github_release_via_docker_image"}
                }) {
            assertTrue(
                    recipe(step[0]).contains("@$(WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS) " + step[1]),
                    step[0] + " does not load the credentials of " + step[1] + " silently");
        }
    }

    /** Runs a probe target behind the prefix; returns make's combined output. */
    private static String probe(final Path root, final Path accounts, final Map<String, String> preset)
            throws Exception {
        final Path probeMakefile = root.resolve("probe.mk");
        Files.write(
                probeMakefile,
                List.of(
                        "probe:",
                        "\t@$(WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS) sh -c '"
                                + "for n in " + String.join(" ", NAMES) + "; do "
                                + "eval \"v=\\$$$$n\"; echo \"$$n=$$v\"; done > \"$$REPORT\"'"),
                StandardCharsets.UTF_8);
        final ProcessBuilder builder =
                new ProcessBuilder(
                                "make", "--no-print-directory", "-f", "Makefile", "-f",
                                probeMakefile.toString(), "probe")
                        .redirectErrorStream(true);
        final Map<String, String> environment = builder.environment();
        for (final String name : NAMES) {
            environment.remove(name);
        }
        environment.remove("MAKEFLAGS");
        environment.remove("MFLAGS");
        environment.remove("MAKELEVEL");
        environment.putAll(preset);
        environment.put("DEVOPS_ACCOUNT_DIR", accounts.toString());
        environment.put("REPORT", root.resolve("report.txt").toString());
        final Process process = builder.start();
        final String output = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertTrue(process.waitFor(60, TimeUnit.SECONDS), "make timed out");
        assertEquals(0, process.exitValue(), output);
        return output;
    }

    private static Path accounts(final Path root) throws IOException {
        final Path dir = root.resolve("accounts");
        Files.createDirectories(dir);
        Files.write(
                dir.resolve("account_github.env"),
                List.of("# GITHUB_GH_TOKEN=from-a-comment", "GITHUB_GH_TOKEN=gh-from-dir"),
                StandardCharsets.UTF_8);
        Files.write(
                dir.resolve("account_maven_central.env"),
                List.of(
                        "# MAVEN_GPG_KEY_B64 is base64",
                        "MAVEN_CENTRAL_USERNAME=user-from-dir",
                        "MAVEN_CENTRAL_PASSWORD=password-from-dir",
                        "MAVEN_GPG_KEY_B64=a2V5LWZyb20tZGly",
                        "MAVEN_GPG_PASSPHRASE=passphrase-from-dir"),
                StandardCharsets.UTF_8);
        return dir;
    }

    @Test
    void unsetCredentialsAreReadFromTheDirectoryAndNeverPrinted(@TempDir final Path root) throws Exception {
        assumeTrue(onPath("make"), "needs make");

        final String output = probe(root, accounts(root), Map.of());

        assertEquals(
                List.of(
                        "GITHUB_GH_TOKEN=gh-from-dir",
                        "MAVEN_CENTRAL_USERNAME=user-from-dir",
                        "MAVEN_CENTRAL_PASSWORD=password-from-dir",
                        "MAVEN_GPG_KEY_B64=a2V5LWZyb20tZGly",
                        "MAVEN_GPG_PASSPHRASE=passphrase-from-dir"),
                Files.readAllLines(root.resolve("report.txt"), StandardCharsets.UTF_8));
        assertFalse(output.contains("from-dir"), "a credential reached make's output: " + output);
    }

    @Test
    void aCredentialAlreadyInTheEnvironmentWins(@TempDir final Path root) throws Exception {
        assumeTrue(onPath("make"), "needs make");

        probe(root, accounts(root), Map.of("GITHUB_GH_TOKEN", "gh-from-env"));

        final List<String> report = Files.readAllLines(root.resolve("report.txt"), StandardCharsets.UTF_8);
        assertEquals("GITHUB_GH_TOKEN=gh-from-env", report.get(0));
        assertEquals("MAVEN_CENTRAL_PASSWORD=password-from-dir", report.get(2));
    }
}
