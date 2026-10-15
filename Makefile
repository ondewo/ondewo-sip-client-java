export

# =====================================================================================
# ondewo-sip-client-java - Makefile
#
# The ONDEWO SIP (SIP Telephony) gRPC client for java.
#
# This repository holds NO hand-written protocol code: the generated stubs under
# src/main/java are produced by the ondewo-java-proto-compiler docker image from the
# protos of the ondewo-sip-api submodule. The maven build descriptor (pom.xml) is
# hand-maintained - a generation keeps it - and the jar in target/ is packaged from it
# in the ondewo-sip-client-utils-java image (Dockerfile.utils).
#
# Quick start:
#   make help                    # list every documented target
#   make makefile_chapters       # list the section headers below
#   make build                   # submodules -> compiler image -> stubs -> jar (all in docker)
#   make test                    # check_build + the maven test suite (local JDK + maven)
#   make test_via_docker         # the same inside the utils image - needs only docker
#
# Versioning: ONDEWO_SIP_VERSION (below) is the single source of truth. It is
# passed to the compiler image as the maven <version> of the generated library, and
# `make update_pom_version` (run by every generation and by `make ondewo_release`) writes
# it into pom.xml - never edit the pom's version by hand.
# =====================================================================================

# ---------------- BEFORE RELEASE ----------------
# 1 - Update Version Number (ONDEWO_SIP_VERSION and the submodule pins)
# 2 - Update RELEASE.md
# -------------- Release Process Steps (`make ondewo_release`, everything runs locally) --------------
# CI builds and publishes nothing, and the credentials live ONLY in ondewo-devops-accounts.
# 1 - Get Credentials from devops-accounts repo, then DELETE that clone before anything else runs
# 2 - release_preflight (read-only): host tools, every credential set AND accepted (GitHub, Maven
#     Central, GPG), version absent from origin and Maven Central, RELEASE.md entry, branch up to date
# 3 - Build, run the full test suite and rehearse the signed Maven Central deployment
# 4 - Commit locally
# 5 - Maven Central: upload, publish automatically, wait until Central reports it PUBLISHED and
#     repo1.maven.org serves it
# 6 - Push, create and push the Release Branch and the Release Tag
# 7 - GitHub Release with the release notes, then release_verify (read-only)

########################################################
# 		Variables
########################################################

# MUST BE THE SAME AS THE API in Major and Minor Version Number
# example: API 2.9.0 --> Client 2.9.X
ONDEWO_SIP_VERSION=5.5.0

# Submodule pins. Both are checked out by `make checkout_defined_submodule_versions`, which
# every `make build` runs first, so a build can never silently use whatever the submodule
# happened to be left at.
ONDEWO_SIP_API_GIT_BRANCH=tags/5.5.0
ONDEWO_PROTO_COMPILER_GIT_BRANCH=tags/5.15.5

# From ondewo-devops-accounts/account_github.env at release time (see run_release_with_devops). It
# must be allowed to push to this repository, which check_gh_token_valid proves before the first push.
GITHUB_GH_TOKEN?=ENTER_YOUR_TOKEN_HERE

# --- Maven Central (Sonatype Central Portal) credentials
# All four come from ondewo-devops-accounts/account_maven_central.env at release time (see
# run_release_with_devops) and are never written into a file in this repository. `export` at
# the top of this Makefile puts them into the recipe environment, which is how they reach the
# publishing container as bare `docker run -e NAME` forwards - so no secret is ever spelled
# out on a command line.
#
# The two halves of a Central Portal USER TOKEN (https://central.sonatype.com/account ->
# "Generate User Token"), NOT the portal login.
MAVEN_CENTRAL_USERNAME?=ENTER_HERE_YOUR_MAVEN_CENTRAL_USERNAME
MAVEN_CENTRAL_PASSWORD?=ENTER_HERE_YOUR_MAVEN_CENTRAL_PASSWORD
# The PGP secret key Central verifies every artifact against, base64 of
# `gpg --armor --export-secret-keys <fingerprint>`. Base64 because an account_*.env file is
# strictly one line per variable (run_release_with_devops reads it line by line), and an armored
# key is not. Its PUBLIC half has to be on a keyserver - see README.md.
MAVEN_GPG_KEY_B64?=ENTER_HERE_YOUR_MAVEN_GPG_KEY_B64
MAVEN_GPG_PASSPHRASE?=ENTER_HERE_YOUR_MAVEN_GPG_PASSPHRASE

# --- Directories
ONDEWO_API_DIR=ondewo-sip-api
ONDEWO_PROTO_COMPILER_DIR=ondewo-proto-compiler
# The protos to compile. Every ONDEWO api repository keeps them below a single top-level
# `ondewo/` tree, which is what TARGET_PROTOS_SUBDIR scopes the compilation to (an unscoped
# run over a root that spans several top-level trees is refused by the image, on purpose).
TARGET_PROTOS_SUBDIR=ondewo
ONDEWO_PROTOS_DIR=${ONDEWO_API_DIR}/${TARGET_PROTOS_SUBDIR}
# The vendored googleapis tree. The java image gives protoc a SINGLE -I root (the api
# directory), so `import "google/api/annotations.proto"` only resolves when that tree sits at
# <api>/google. Products whose api repository keeps it elsewhere (ondewo-survey-api vendors it
# as googleapis/google) override this variable; the tree is then bind-mounted into the input
# volume at the path protoc expects. Empty for an api that only imports well-known types.
GOOGLE_APIS_DIR=${ONDEWO_API_DIR}/google
# Generated java source root (standard maven layout) - also where hand-written sources live.
STUBS_DIR=src/main/java

# --- The compiler image and the maven coordinates of the generated library
# The image TAG is the only contract with ondewo-proto-compiler: `make build_compiler`
# rebuilds it from the submodule, but any locally built ondewo-java-proto-compiler:latest works.
PROTO_COMPILER_IMAGE=ondewo-java-proto-compiler
MAVEN_GROUP_ID=com.ondewo
MAVEN_ARTIFACT_ID=ondewo-sip-client-java

# --- The utils image: maven, the JDK, gpg and gh (Dockerfile.utils)
# Every step that needs one of them runs in this image through a *_via_docker target, so neither a
# build nor a release depends on whatever the host happens to have installed.
IMAGE_UTILS_NAME=ondewo-sip-client-utils-java:${ONDEWO_SIP_VERSION}
# The run prefix of every *_via_docker target; `-e NAME` credential forwards and the image name
# follow it. The repository is MOUNTED, so target/ and pom.xml land in the working tree, and the
# container runs as the invoking user, so nothing it writes is root-owned. HOME and MAVEN_CONFIG
# point at a writable path, and the maven local repository is the git-ignored .m2/ of the
# checkout, so the downloads survive from one container to the next.
UTILS_DOCKER_RUN=docker run --rm --user $$(id -u):$$(id -g) \
	-v ${shell pwd}:/workspace -w /workspace \
	-e HOME=/tmp/home -e MAVEN_CONFIG=/tmp/home/.m2 \
	-e MAVEN_ARGS=-Dmaven.repo.local=/workspace/.m2/repository

# --- Publishing
# A settings.xml that holds no credential: it interpolates ${env.MAVEN_CENTRAL_*} at run time.
MAVEN_CENTRAL_SETTINGS=maven-central-settings.xml

# Terminate on the ***** separator that delimits release entries, NOT on /\*\*/ - that matches
# the first markdown **bold** span inside the entry and silently truncates the notes there,
# with no error from `gh release create`. The heading is matched as a WHOLE line with the version
# quoted (\Q..\E): an unanchored `9.1.0` would also open the slice at a `19.1.0` or `9.1.0x` heading.
CURRENT_RELEASE_NOTES=`cat RELEASE.md \
	| perl -ne 'print if /^\#+ Release ONDEWO SIP Java Client \Q${ONDEWO_SIP_VERSION}\E$$/../^\*{5}/'`

GH_REPO="https://github.com/ondewo/ondewo-sip-client-java"
# <owner>/<repo>, the form the GitHub REST API addresses the repository by (check_gh_token_valid).
GH_REPO_PATH=ondewo/ondewo-sip-client-java
# The Central Portal's read-only "is this version published?" endpoint (check_maven_central_token_valid).
MAVEN_CENTRAL_PUBLISHED_API=https://central.sonatype.com/api/v1/publisher/published?namespace=${MAVEN_GROUP_ID}&name=${MAVEN_ARTIFACT_ID}&version=${ONDEWO_SIP_VERSION}
# What Maven Central SERVES - the CDN every consumer resolves from, as opposed to the Portal's
# state above. release_preflight requires the pom to be absent there, release_verify present.
MAVEN_REPO1_VERSION_URL=https://repo1.maven.org/maven2/$(subst .,/,${MAVEN_GROUP_ID})/${MAVEN_ARTIFACT_ID}/${ONDEWO_SIP_VERSION}
MAVEN_REPO1_POM_URL=${MAVEN_REPO1_VERSION_URL}/${MAVEN_ARTIFACT_ID}-${ONDEWO_SIP_VERSION}.pom
MAVEN_REPO1_JAR_URL=${MAVEN_REPO1_VERSION_URL}/${MAVEN_ARTIFACT_ID}-${ONDEWO_SIP_VERSION}.jar
# release_verify polls repo1.maven.org this often, this far apart (20 x 30 s = 10 minutes): Central
# reports PUBLISHED before every CDN node serves the files.
# VERIFY_ATTEMPTS / VERIFY_INTERVAL_S are accepted as short aliases.
RELEASE_VERIFY_ATTEMPTS?=$(or ${VERIFY_ATTEMPTS},20)
RELEASE_VERIFY_INTERVAL_S?=$(or ${VERIFY_INTERVAL_S},30)
# The only tools the release needs on the host; everything else runs in the utils image.
RELEASE_HOST_TOOLS=docker git perl curl

# --- ondewo-devops-accounts
# DEVOPS_ACCOUNT_DIR is deliberately NOT defined here: when the caller's environment points it at an
# existing directory (the single temp clone of ondewo-sip-api's release_all_clients - give an
# ABSOLUTE path, `make -C` changes the directory), run_release_with_devops reads the credentials
# from there and neither clones nor deletes it. Otherwise it clones DEVOPS_ACCOUNT_GIT_URL into
# DEVOPS_ACCOUNT_CLONE_DIR and deletes that clone again before `make release` starts.
DEVOPS_ACCOUNT_GIT=ondewo-devops-accounts
DEVOPS_ACCOUNT_GIT_URL?=git@bitbucket.org:ondewo/${DEVOPS_ACCOUNT_GIT}.git
DEVOPS_ACCOUNT_CLONE_DIR?=./${DEVOPS_ACCOUNT_GIT}
# release_preflight and release_verify are also run STANDALONE by ondewo-sip-api's release driver,
# with nothing but an absolute DEVOPS_ACCOUNT_DIR in the environment. This shell prefix fills every
# credential that is unset or still a placeholder from $$DEVOPS_ACCOUNT_DIR/account_*.env (anchored
# `^NAME=`, the last such line) into the environment of the ONE recipe line it prefixes - so only the
# step that uses them gets them, nothing persists, and no value is ever on a command line or echoed
# (every line it prefixes is @-silent). A value already in the environment always wins. Pinned by
# ReleaseCredentialsFromDevopsAccountDirTest.
WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS=if [ -n "$$DEVOPS_ACCOUNT_DIR" ] && [ -d "$$DEVOPS_ACCOUNT_DIR" ]; then \
	for spec in account_github.env:GITHUB_GH_TOKEN \
		account_maven_central.env:MAVEN_CENTRAL_USERNAME account_maven_central.env:MAVEN_CENTRAL_PASSWORD \
		account_maven_central.env:MAVEN_GPG_KEY_B64 account_maven_central.env:MAVEN_GPG_PASSPHRASE; do \
		file=$${spec%%:*}; name=$${spec\#*:}; \
		eval "value=\"\$$$$name\""; \
		case "$$value" in \
			""|ENTER_*) line=$$(grep -hE "^$$name=" "$$DEVOPS_ACCOUNT_DIR/$$file" 2>/dev/null | tail -n 1); \
				if [ -n "$$line" ]; then set -a; eval "$$line"; set +a; fi;; \
		esac; \
	done; \
fi;

# Non-empty under `make -n` (see run_release_with_devops).
MAKE_DRY_RUN=$(findstring n,$(firstword -$(MAKEFLAGS)))

# Console colors for [INFO] / [SUCCESS] / [ERROR] messages
BLUE   := \033[1;34m
GREEN  := \033[0;32m
RED    := \033[0;31m
NC     := \033[0m

.DEFAULT_GOAL := help

########################################################
#       ONDEWO Standard Make Targets
########################################################

setup_developer_environment_locally: update_submodules install_precommit_hooks ## Sets up local development environment

install_precommit_hooks: ## Installs pre-commit hooks and sets them up for the ondewo-sip-client-java repo
	pip install pre-commit
	pre-commit install
	pre-commit install --hook-type commit-msg

precommit_hooks_run_all_files: ## Runs all pre-commit hooks on all files and not just the changed ones
	pre-commit run --all-files

help: ## Print usage info about help targets
	# (first comment after target starting with double hashes ##)
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' Makefile | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-40s\033[0m %s\n", $$1, $$2}'

makefile_chapters: ## Shows all sections of Makefile
	@echo `cat Makefile| grep "########################################################" -A 1 | grep -v "########################################################"`

TEST: ## Prints some important variables
	@echo "Release Notes: \n \n$(CURRENT_RELEASE_NOTES)"
	@echo "GH Token: \t $(if $(filter-out ENTER_YOUR_TOKEN_HERE,$(GITHUB_GH_TOKEN)),<set>,<unset>)"
	@echo "Central Token: \t $(if $(filter-out ENTER_HERE_YOUR_MAVEN_CENTRAL_USERNAME,$(MAVEN_CENTRAL_USERNAME)),<set>,<unset>) / $(if $(filter-out ENTER_HERE_YOUR_MAVEN_CENTRAL_PASSWORD,$(MAVEN_CENTRAL_PASSWORD)),<set>,<unset>)"
	@echo "Signing Key: \t $(if $(filter-out ENTER_HERE_YOUR_MAVEN_GPG_KEY_B64,$(MAVEN_GPG_KEY_B64)),<set>,<unset>) / $(if $(filter-out ENTER_HERE_YOUR_MAVEN_GPG_PASSPHRASE,$(MAVEN_GPG_PASSPHRASE)),<set>,<unset>)"
	@echo "Maven Artifact: \t ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION}"
	@echo "Api Submodule: \t ${ONDEWO_API_DIR} @ ${ONDEWO_SIP_API_GIT_BRANCH}"
	@echo "Proto Compiler: \t ${ONDEWO_PROTO_COMPILER_DIR} @ ${ONDEWO_PROTO_COMPILER_GIT_BRANCH}"

check_build: ## Checks if all built proto-code is there
# protoc derives the outer class name from the proto FILE name (snake_case -> CamelCase) and
# appends "OuterClass" when a message shares that name, so the generated file is matched by
# the <CamelCase>*.java glob in either case - and also when the proto sets its own
# java_outer_classname (e.g. entity_type.proto -> EntityTypeProto.java).
# A hyphen separates words like an underscore does (speech-to-text.proto -> SpeechToText.java).
	@test -d ${ONDEWO_PROTOS_DIR} || { echo "$(RED)[ERROR]$(NC) '${ONDEWO_PROTOS_DIR}' is missing - run 'make update_submodules' first"; exit 1; }
	@test -d ${STUBS_DIR} || { echo "$(RED)[ERROR]$(NC) '${STUBS_DIR}' is missing - run 'make generate_ondewo_protos' first"; exit 1; }
	@find ${ONDEWO_PROTOS_DIR} -type f -name "*.proto" ! -path "*/google/*" -exec basename {} .proto \; \
		| sort -u \
		| while IFS= read -r proto; do \
			outer=$$(echo "$$proto" | awk -F"[_-]" '{ o=""; for (i=1;i<=NF;i++) o = o toupper(substr($$i,1,1)) substr($$i,2); print o }'); \
			if [ -z "$$(find ${STUBS_DIR} -type f -name "$$outer*.java" | head -n 1)" ]; then \
				echo "$(RED)[ERROR]$(NC) No java code for $$proto.proto (expected $$outer*.java below ${STUBS_DIR})"; \
				exit 1; \
			fi; \
		done
	@echo "$(GREEN)[SUCCESS]$(NC) every .proto has generated java code"

########################################################
#       Repo Specific Make Targets
########################################################
#		Build

build: update_submodules checkout_defined_submodule_versions build_compiler generate_ondewo_protos check_build package_via_docker ## Build the client: submodules -> compiler image -> stubs -> jar

build_compiler: ## Build the ondewo-java-proto-compiler docker image from the submodule
# generate_ondewo_protos runs this image as the invoking user, but COPY keeps the file modes of the
# checkout: under a restrictive umask (e.g. `umask 077` around a release log) the image's root-owned
# scripts are 0600 and the generation dies with "Permission denied". git tracks only the executable
# bit, so making the build context world-readable leaves the submodule clean.
	@echo "$(BLUE)[INFO]$(NC) Building the ${PROTO_COMPILER_IMAGE} image from ${ONDEWO_PROTO_COMPILER_DIR} ..."
	chmod -R a+rX ${ONDEWO_PROTO_COMPILER_DIR}/java
	cd ${ONDEWO_PROTO_COMPILER_DIR}/java && sh build.sh
	@echo "$(GREEN)[SUCCESS]$(NC) Built ${PROTO_COMPILER_IMAGE}:latest"

# The docker run below is the java image's documented contract, taken from
# ondewo-proto-compiler/java/example/run-compile.sh:
#
#   docker run -v <in>:/input-volume -v <out>:/output-volume ondewo-java-proto-compiler \
#              <relative_protos_dir> [<target_subdir>] [<group_id>] [<artifact_id>] [<version>]
#
# * NO `-it`: it breaks every non-interactive caller with "cannot attach stdin to a TTY-enabled
#   container because stdin is not a terminal". `-it` belongs only on the --entrypoint /bin/bash
#   debug form (see README.md).
# * Input volume: ONLY the api submodule (plus this repo's LICENSE, so the packaged library
#   ships it instead of the image's default). Mounting the whole repository would copy
#   pom.xml into the container, where it WINS over the rendered template - and the library
#   version would then silently freeze at whatever that file already said.
# * Output volume: the repository root, which is exactly the maven project the image emits
#   (pom.xml + src/main/java + target/*.jar). The image removes only the leaf packages it
#   regenerates, so hand-written sources in their own package survive - see README.md.
# * pom.xml is hand-maintained, but the image OVERWRITES it with its rendered template, which
#   has none of the HAND-WRITTEN blocks (tests, coverage, javadoc, Maven Central metadata,
#   signing). So it is saved before the run and restored after it, and update_pom_version then
#   writes ONDEWO_SIP_VERSION into it. The one thing taken from the rendered template is a
#   check: the stubs are generated for its toolchain pins (POM_TOOLCHAIN_PINS), and protobuf
#   gencode needs a runtime at least as new, so a compiler that moves a pin fails the run
#   until the new value is copied into pom.xml by hand.
# * --user: the container runs as the invoking user, so nothing it writes is root-owned.
#   TEMP_SRC_DIRECTORY moves the image's scratch copy of the input from the root-owned
#   /image-data to /tmp; the pre-warmed maven repository below /image-data is world-readable
#   and only read (the image's build of the stubs runs offline).
POM_TOOLCHAIN_PINS=<(maven\.compiler\.release|grpc\.version|protobuf\.version|google\.common\.protos\.version)>

generate_ondewo_protos: ## Generate the java gRPC stubs from the proto files (keeps the hand-maintained pom.xml)
	@test -d ${ONDEWO_PROTOS_DIR} || { echo "$(RED)[ERROR]$(NC) '${ONDEWO_PROTOS_DIR}' is missing - run 'make update_submodules' first"; exit 1; }
	@test -f pom.xml || { echo "$(RED)[ERROR]$(NC) pom.xml is missing - it is hand-maintained, restore it with 'git checkout pom.xml'"; exit 1; }
	@echo "$(BLUE)[INFO]$(NC) Generating ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION} from ${ONDEWO_PROTOS_DIR} ..."
	pom_backup=$$(mktemp) && cp pom.xml "$$pom_backup" || exit 1; \
	docker run --rm --user $$(id -u):$$(id -g) \
		-e HOME=/tmp -e TEMP_SRC_DIRECTORY=/tmp/src \
		-v ${shell pwd}/${ONDEWO_API_DIR}:/input-volume/${ONDEWO_API_DIR} \
		$(if $(wildcard ${GOOGLE_APIS_DIR}),-v ${shell pwd}/${GOOGLE_APIS_DIR}:/input-volume/${ONDEWO_API_DIR}/google,) \
		-v ${shell pwd}/LICENSE:/input-volume/LICENSE \
		-v ${shell pwd}:/output-volume \
		${PROTO_COMPILER_IMAGE} \
		${ONDEWO_API_DIR} \
		${TARGET_PROTOS_SUBDIR} \
		${MAVEN_GROUP_ID} \
		${MAVEN_ARTIFACT_ID} \
		${ONDEWO_SIP_VERSION}; \
	status=$$?; \
	rendered_pins=$$(grep -E '${POM_TOOLCHAIN_PINS}' pom.xml); \
	kept_pins=$$(grep -E '${POM_TOOLCHAIN_PINS}' "$$pom_backup"); \
	cp "$$pom_backup" pom.xml && rm -f "$$pom_backup" || exit 1; \
	[ $$status -eq 0 ] || exit $$status; \
	if [ "$$rendered_pins" != "$$kept_pins" ]; then \
		echo "$(RED)[ERROR]$(NC) ${PROTO_COMPILER_IMAGE} generated the stubs for other toolchain pins than pom.xml declares"; \
		echo "        copy the rendered values into the <properties> of pom.xml:"; \
		echo "--- rendered by the compiler image:"; echo "$$rendered_pins"; \
		echo "--- pom.xml:"; echo "$$kept_pins"; \
		exit 1; \
	fi
	make update_pom_version
	@echo "$(GREEN)[SUCCESS]$(NC) Generated the client into ${STUBS_DIR}; pom.xml kept at ${ONDEWO_SIP_VERSION}"

update_pom_version: ## Write ONDEWO_SIP_VERSION into the project <version> of pom.xml
# The project <version> is the only one at two-space indentation (a direct child of <project>,
# the same anchoring check_central_pom relies on). ondewo-sip-api's release_client rewrites only
# this Makefile, which is why `make ondewo_release` runs this before spc compares the versions.
	@perl -i -pe 's|^  <version>[^<]*</version>|  <version>${ONDEWO_SIP_VERSION}</version>|' pom.xml
	@grep -q "^  <version>${ONDEWO_SIP_VERSION}</version>" pom.xml || { echo "$(RED)[ERROR]$(NC) pom.xml has no project <version> line to update"; exit 1; }
	@echo "$(GREEN)[SUCCESS]$(NC) pom.xml is at ${ONDEWO_SIP_VERSION}"

package: ## Compile the stubs and package the jar with the local JDK (>= 11) and maven
	@test -f pom.xml || { echo "$(RED)[ERROR]$(NC) no pom.xml - restore it with 'git checkout pom.xml'"; exit 1; }
	mvn -B --no-transfer-progress -DskipTests package

package_via_docker: build_utils_docker_image ## Run `make package` in the utils image - needs only docker
	${UTILS_DOCKER_RUN} ${IMAGE_UTILS_NAME} make package

# `verify`, not `test`: the jacoco coverage gate on the hand-written sources is bound to the
# verify phase, so `mvn test` alone would run the suite but skip the gate - and local and CI
# would disagree about what "the tests pass" means.
test: check_build ## Run the java test suite + the coverage gate, and verify every proto produced java code
	@test -f pom.xml || { echo "$(RED)[ERROR]$(NC) no pom.xml - restore it with 'git checkout pom.xml'"; exit 1; }
	mvn -B --no-transfer-progress verify

test_via_docker: build_utils_docker_image ## Run `make test` in the utils image - needs only docker
	${UTILS_DOCKER_RUN} ${IMAGE_UTILS_NAME} make test

build_utils_docker_image: ## Build the utils image (maven, JDK, gpg, gh) the *_via_docker targets run in
# The Dockerfile arrives on stdin and there is NO build context: Dockerfile.utils copies nothing, and
# a context of `.` would ship the whole checkout - an ondewo-devops-accounts clone included - to the
# docker daemon. .dockerignore excludes that clone as well, for any other build that uses `.`.
	docker build -t ${IMAGE_UTILS_NAME} - < Dockerfile.utils

clean: ## Remove the local maven build output (never touches the generated sources)
	rm -rf target

########################################################
#		Submodules

update_submodules: ## Initialize and update all submodules
	@echo "START initializing submodules ..."
	git submodule update --init --recursive
	@echo "DONE initializing submodules"

checkout_defined_submodule_versions: ## Check out the submodule versions pinned above
	@echo "START checking out submodules ..."
	git -C ${ONDEWO_API_DIR} fetch --all
	git -C ${ONDEWO_API_DIR} checkout ${ONDEWO_SIP_API_GIT_BRANCH}
	git -C ${ONDEWO_PROTO_COMPILER_DIR} fetch --all
	git -C ${ONDEWO_PROTO_COMPILER_DIR} checkout ${ONDEWO_PROTO_COMPILER_GIT_BRANCH}
	@echo "DONE checking out submodules"

########################################################
#		Release

release: ## Automate the entire release process - every step runs locally, CI publishes nothing
# The ORDER is pinned by src/test/java/com/ondewo/sip/release/ReleaseOrderTest.java.
	@echo "Start Release"
# FIRST, before anything is built, committed or pushed: every credential is proven set AND accepted,
# the version is proven absent from origin and from Maven Central, and RELEASE.md is proven to carry
# its notes. All of it is read-only, so a failure here leaves nothing behind.
	make release_preflight
	make build
	-make precommit_hooks_run_all_files
	make check_build
# The full suite + its coverage gate, then the Maven Central deployment minus the upload: the release
# profile, the suite again, javadoc, and signing (with a throwaway key), online and offline.
	make test_via_docker
	make check_central_pom
	make dry_run_maven_central_via_docker
	git status
# src/ carries both the generated stubs and any hand-written sources beside them; pom.xml is
# hand-maintained, carries the version above (update_pom_version), and is staged so the tag
# always contains the descriptor the jar was built from.
	git add src
	git add pom.xml
	git add Makefile
	git add README.md
	git add RELEASE.md
	git add LICENSE
	git add ${ONDEWO_PROTO_COMPILER_DIR}
	git add ${ONDEWO_API_DIR}
	git status
# Commit only when something is staged, but never swallow a REAL commit failure (e.g. no git
# identity): the tag below would then silently name the previous commit.
	git diff --cached --quiet || git commit --no-verify -m "Preparing for Release ${ONDEWO_SIP_VERSION}"
# The ONLY upload to Maven Central, BEFORE anything reaches origin: a failed or rejected deployment
# then leaves no branch, no tag and no GitHub release behind, and the release can simply be re-run.
# The release profile sets autoPublish=true and waitUntil=published, so this returns once the Portal
# reports the version PUBLISHED; verify_maven_central_serves_version then waits until
# repo1.maven.org - what every consumer resolves from - serves the pom and the jar.
	make push_to_maven_central_via_docker
	make verify_maven_central_serves_version
# Only now does anything reach origin. release_preflight proved the branch is not behind origin, so
# this push is a fast-forward.
	git push
	make create_release_branch
	make create_release_tag
# LAST, so that a GitHub release exists only for a version that is complete everywhere. It carries
# the release notes; the jars are distributed by Maven Central.
	make release_to_github_via_docker_image
	make release_verify
	@echo "Release Finished"

release_preflight: ## Read-only: fail before anything is built, committed or pushed (tools, credentials, version, notes, branch)
# Changes nothing anywhere - every probe is a GET or a local check; the only side effect is building
# the local utils image the credential checks run in. Standalone, it reads the credentials from
# DEVOPS_ACCOUNT_DIR (see WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS).
	make check_release_host_tools
	make check_no_devops_clone_in_workspace
	@$(WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS) make check_release_credentials
	@$(WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS) make validate_release_credentials_via_docker_image
	make check_release_notes
	make check_version_unreleased
	make check_branch_up_to_date
	@echo "$(GREEN)[SUCCESS]$(NC) release preflight passed for ${ONDEWO_SIP_VERSION}"

release_verify: ## Read-only: fail unless the tag is on origin, the GitHub release has notes and repo1.maven.org serves the version
	make check_tag_on_origin
	make verify_maven_central_serves_version
	@$(WITH_DEVOPS_ACCOUNT_DIR_CREDENTIALS) make verify_github_release_via_docker_image
	@echo "$(GREEN)[SUCCESS]$(NC) ${ONDEWO_SIP_VERSION} is tagged, released on GitHub with notes, and served by Maven Central"

check_release_host_tools: ## Assert the host has the only tools a release runs outside the utils image
	@missing=""; \
	for tool in ${RELEASE_HOST_TOOLS}; do command -v "$$tool" >/dev/null 2>&1 || missing="$$missing $$tool"; done; \
	if [ -n "$$missing" ]; then echo "$(RED)[ERROR]$(NC) the release needs on the host:$$missing"; exit 1; fi
	@docker info >/dev/null 2>&1 || { echo "$(RED)[ERROR]$(NC) the docker daemon is not reachable"; exit 1; }
	@echo "$(GREEN)[SUCCESS]$(NC) host tools present: ${RELEASE_HOST_TOOLS} (docker daemon reachable)"

check_no_devops_clone_in_workspace: ## Assert no ondewo-devops-accounts clone sits in the checkout the containers mount
# Every *_via_docker target mounts this checkout into a container, so a credential clone inside it
# would be readable there. run_release_with_devops deletes its clone before `make release` starts.
	@if [ -e "${DEVOPS_ACCOUNT_CLONE_DIR}" ]; then \
		echo "$(RED)[ERROR]$(NC) ${DEVOPS_ACCOUNT_CLONE_DIR} exists - the release mounts this checkout into containers; delete it first"; \
		exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) no ${DEVOPS_ACCOUNT_GIT} clone in the workspace"

check_version_unreleased: ## Assert neither origin nor Maven Central has ${ONDEWO_SIP_VERSION} yet (read-only)
# `git ls-remote --exit-code` exits 2 when no ref matches, 0 when one does, and 128 when origin cannot
# be asked - which must fail too. A pattern matches a WHOLE trailing path, so refs/tags/9.1.0 never
# matches refs/tags/19.1.0. 404 from repo1.maven.org is the only answer that lets the release go on.
	@git ls-remote --exit-code origin "refs/tags/${ONDEWO_SIP_VERSION}" "refs/heads/release/${ONDEWO_SIP_VERSION}" >/dev/null; \
	status=$$?; \
	case $$status in \
		0) echo "$(RED)[ERROR]$(NC) origin already has the tag ${ONDEWO_SIP_VERSION} or the branch release/${ONDEWO_SIP_VERSION}"; exit 1;; \
		2) echo "$(GREEN)[SUCCESS]$(NC) origin has neither the tag ${ONDEWO_SIP_VERSION} nor the branch release/${ONDEWO_SIP_VERSION}";; \
		*) echo "$(RED)[ERROR]$(NC) cannot ask origin for its refs (git ls-remote exit $$status)"; exit 1;; \
	esac
	@code=$$(curl -sS --connect-timeout 20 --max-time 60 -o /dev/null -w '%{http_code}' "${MAVEN_REPO1_POM_URL}") || code="no answer"; \
	case "$$code" in \
		404) echo "$(GREEN)[SUCCESS]$(NC) repo1.maven.org does not serve ${ONDEWO_SIP_VERSION} yet";; \
		200) echo "$(RED)[ERROR]$(NC) repo1.maven.org already serves ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION} - a published version can never be uploaded again"; exit 1;; \
		*) echo "$(RED)[ERROR]$(NC) unexpected answer from repo1.maven.org (HTTP $$code) for ${MAVEN_REPO1_POM_URL}"; exit 1;; \
	esac

check_branch_up_to_date: ## Assert the current branch tracks origin and is not behind it, so the release push is a fast-forward
# The release publishes to Maven Central BEFORE it pushes, so a push that origin rejects would leave a
# published version without its tag. The tip origin reports must already be in the local history.
	@branch=$$(git symbolic-ref -q --short HEAD) || { echo "$(RED)[ERROR]$(NC) HEAD is detached - release from a branch"; exit 1; }; \
	git rev-parse -q --verify "$$branch@{upstream}" >/dev/null || { echo "$(RED)[ERROR]$(NC) $$branch has no upstream - 'git push' would refuse"; exit 1; }; \
	remote_tip=$$(git ls-remote --exit-code origin "refs/heads/$$branch" | cut -f1) || { echo "$(RED)[ERROR]$(NC) origin has no branch $$branch"; exit 1; }; \
	git merge-base --is-ancestor "$$remote_tip" HEAD 2>/dev/null || { echo "$(RED)[ERROR]$(NC) $$branch is behind origin ($$remote_tip is not in HEAD's history) - pull first"; exit 1; }; \
	echo "$(GREEN)[SUCCESS]$(NC) $$branch contains origin's tip $$remote_tip"

check_tag_on_origin: ## Assert origin has the tag ${ONDEWO_SIP_VERSION} (read-only)
	@git ls-remote --exit-code origin "refs/tags/${ONDEWO_SIP_VERSION}" >/dev/null \
		|| { echo "$(RED)[ERROR]$(NC) origin has no tag ${ONDEWO_SIP_VERSION}"; exit 1; }
	@echo "$(GREEN)[SUCCESS]$(NC) origin has the tag ${ONDEWO_SIP_VERSION}"

verify_maven_central_serves_version: ## Poll repo1.maven.org until it serves the pom and the jar of ${ONDEWO_SIP_VERSION} (read-only)
# The Portal's PUBLISHED (waitUntil=published) is the Portal's state; consumers resolve from
# repo1.maven.org, which lags it. RELEASE_VERIFY_ATTEMPTS x RELEASE_VERIFY_INTERVAL_S bound the wait.
	@attempt=1; \
	while :; do \
		pom=$$(curl -sS --connect-timeout 20 --max-time 60 -o /dev/null -w '%{http_code}' "${MAVEN_REPO1_POM_URL}") || pom="no answer"; \
		jar=$$(curl -sS --connect-timeout 20 --max-time 60 -o /dev/null -w '%{http_code}' -I "${MAVEN_REPO1_JAR_URL}") || jar="no answer"; \
		if [ "$$pom" = 200 ] && [ "$$jar" = 200 ]; then break; fi; \
		if [ $$attempt -ge ${RELEASE_VERIFY_ATTEMPTS} ]; then \
			echo "$(RED)[ERROR]$(NC) repo1.maven.org does not serve ${ONDEWO_SIP_VERSION} after ${RELEASE_VERIFY_ATTEMPTS} attempts (pom HTTP $$pom, jar HTTP $$jar)"; \
			exit 1; \
		fi; \
		echo "$(BLUE)[INFO]$(NC) repo1.maven.org: pom HTTP $$pom, jar HTTP $$jar - attempt $$attempt/${RELEASE_VERIFY_ATTEMPTS}, retrying in ${RELEASE_VERIFY_INTERVAL_S} s"; \
		attempt=$$((attempt + 1)); \
		sleep ${RELEASE_VERIFY_INTERVAL_S}; \
	done
	@echo "$(GREEN)[SUCCESS]$(NC) repo1.maven.org serves ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION}"

verify_github_release: check_gh_token ## Assert the GitHub release ${ONDEWO_SIP_VERSION} exists and has a non-empty body (read-only, needs gh)
	@body=$$(GH_TOKEN="$$GITHUB_GH_TOKEN" gh release view "${ONDEWO_SIP_VERSION}" --repo "${GH_REPO_PATH}" --json body --jq .body) \
		|| { echo "$(RED)[ERROR]$(NC) gh cannot read the GitHub release ${ONDEWO_SIP_VERSION}: GitHub refused GITHUB_GH_TOKEN (401) or the release does not exist (see the gh error above)"; exit 1; }; \
	if [ -z "$$(printf '%s' "$$body" | tr -d '[:space:]')" ]; then \
		echo "$(RED)[ERROR]$(NC) the GitHub release ${ONDEWO_SIP_VERSION} has an EMPTY body"; exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) the GitHub release ${ONDEWO_SIP_VERSION} carries release notes"

verify_github_release_via_docker_image: build_utils_docker_image check_gh_token ## Run `make verify_github_release` in the utils image - needs only docker
	@${UTILS_DOCKER_RUN} -e GITHUB_GH_TOKEN ${IMAGE_UTILS_NAME} make verify_github_release

create_release_branch: ## Create Release Branch and push it to origin
	git checkout -b "release/${ONDEWO_SIP_VERSION}"
	git push -u origin "release/${ONDEWO_SIP_VERSION}"

create_release_tag: ## Create Release Tag and push it to origin
	git tag -a ${ONDEWO_SIP_VERSION} -m "release/${ONDEWO_SIP_VERSION}"
	git push origin ${ONDEWO_SIP_VERSION}

check_gh_token: ## Fail loudly when GITHUB_GH_TOKEN is unset or still the placeholder
# Without this, an unset token reaches gh as an empty GH_TOKEN and the failure surfaces only in
# push_to_gh - after create_release_tag has already pushed the tag.
# `$$GITHUB_GH_TOKEN`, not `${GITHUB_GH_TOKEN}`: make expands the latter BEFORE the shell runs, which
# puts the token on the argv of `sh -c`, and /proc/<pid>/cmdline is readable by every user on the host.
	@if [ -z "$$GITHUB_GH_TOKEN" ] || [ "$$GITHUB_GH_TOKEN" = "ENTER_YOUR_TOKEN_HERE" ]; then \
		echo "$(RED)[ERROR]$(NC) GITHUB_GH_TOKEN is not set - use 'make ondewo_release', which reads ${DEVOPS_ACCOUNT_GIT}/account_github.env"; \
		exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) GITHUB_GH_TOKEN is set"

check_gh_token_valid: check_gh_token ## Assert GitHub accepts GITHUB_GH_TOKEN and lets it push to this repository (read-only, needs gh)
# GET /repos/<owner>/<repo> changes nothing. `.permissions` is only present for an authenticated
# caller, and `push` is exactly what the release branch, the tag and the GitHub release need. Every gh
# call gets the token as GH_TOKEN for that one command: nothing runs `gh auth login`, so the token is
# never persisted in a gh config file, and a GH_TOKEN/GITHUB_TOKEN of the caller is never used instead.
	@push=$$(GH_TOKEN="$$GITHUB_GH_TOKEN" gh api "repos/${GH_REPO_PATH}" --jq .permissions.push) \
		|| { echo "$(RED)[ERROR]$(NC) GitHub refused GITHUB_GH_TOKEN (see the gh error above)"; exit 1; }; \
	if [ "$$push" != "true" ]; then \
		echo "$(RED)[ERROR]$(NC) GITHUB_GH_TOKEN is valid but may not push to ${GH_REPO_PATH} (permissions.push=$$push)"; \
		exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) GitHub accepts GITHUB_GH_TOKEN and it may push to ${GH_REPO_PATH}"

check_release_notes: ## Assert RELEASE.md carries a non-empty entry for ONDEWO_SIP_VERSION
# `gh release create -n ""` succeeds and publishes an EMPTY release, so an entry that was
# forgotten - or a heading whose wording drifted away from what the CURRENT_RELEASE_NOTES
# flip-flop greps for - is otherwise only noticed by whoever reads the release page afterwards.
# A heading followed directly by its ***** separator is as empty as no heading at all, so the body
# is what is counted: the slice minus its heading and separator lines, minus blank lines.
	@notes="$(CURRENT_RELEASE_NOTES)"; \
	body=$$(printf '%s\n' "$$notes" | grep -vE '^(#+ Release ONDEWO SIP Java Client |\*{5})' | grep -c '[^[:space:]]'); \
	if [ "$$body" -eq 0 ]; then \
		echo "$(RED)[ERROR]$(NC) RELEASE.md has no non-empty '## Release ONDEWO SIP Java Client ${ONDEWO_SIP_VERSION}' entry"; \
		echo "        The GitHub release would be created with empty notes - add the entry first."; \
		exit 1; \
	fi; \
	echo "$(GREEN)[SUCCESS]$(NC) RELEASE.md has release notes for ${ONDEWO_SIP_VERSION} ($$body non-blank lines)"

build_gh_release: check_gh_token check_release_notes ## Create the GitHub release with the release notes
	GH_TOKEN="$$GITHUB_GH_TOKEN" gh release create --repo $(GH_REPO) "$(ONDEWO_SIP_VERSION)" \
		-n "$(CURRENT_RELEASE_NOTES)" \
		-t "Release ${ONDEWO_SIP_VERSION}"

push_to_gh: build_gh_release ## Create the GitHub release (the token reaches gh as GH_TOKEN, never through `gh auth login`)
	@echo 'Released to Github'

release_to_github_via_docker_image: build_utils_docker_image check_gh_token ## Run `make push_to_gh` in the utils image - needs only docker
	@${UTILS_DOCKER_RUN} -e GITHUB_GH_TOKEN ${IMAGE_UTILS_NAME} make push_to_gh

########################################################
#		MAVEN CENTRAL

# The publish command. `clean deploy`, never -DskipTests: this builds the exact artifact users
# will download, so it runs the suite and the coverage gate on the way.
MAVEN_CENTRAL_DEPLOY_CMD=mvn -B --no-transfer-progress -s ${MAVEN_CENTRAL_SETTINGS} -Prelease clean deploy
# The armored secret key is reconstructed into the environment right before maven starts and
# is never written to disk. MAVEN_GPG_KEY / MAVEN_GPG_PASSPHRASE are the env var names the
# maven-gpg-plugin's `bc` signer reads by default.
# Unquoted %s on purpose: this whole definition is substituted inside a single-quoted
# `sh -c '...'`, where a nested single quote would end the string.
DECODE_MAVEN_GPG_KEY=MAVEN_GPG_KEY=$$(printf %s "$$MAVEN_GPG_KEY_B64" | base64 -d); export MAVEN_GPG_KEY; unset MAVEN_GPG_KEY_B64

check_central_pom: ## Assert pom.xml carries every field and plugin the Maven Central Portal requires
# The Portal validates the pom only AFTER the whole bundle has been uploaded, so everything
# that can be checked locally is checked locally. The top-level elements are anchored at their
# indentation (two spaces = a direct child of <project>), because <name> and <url> also occur
# inside organization, licenses, developers and scm - an unanchored grep would happily pass a
# pom with no project name at all. The scm children are looked for inside the scm block only,
# for the same reason.
	@test -f pom.xml || { echo "$(RED)[ERROR]$(NC) no pom.xml - restore it with 'git checkout pom.xml'"; exit 1; }
	@missing=""; \
	for element in groupId artifactId version name description url licenses developers scm; do \
		grep -q "^  <$$element>" pom.xml || missing="$$missing <$$element>"; \
	done; \
	scm_block=$$(sed -n '/^  <scm>/,/^  <\/scm>/p' pom.xml); \
	for element in connection developerConnection url; do \
		echo "$$scm_block" | grep -q "<$$element>" || missing="$$missing <scm><$$element>"; \
	done; \
	for plugin in maven-source-plugin maven-javadoc-plugin maven-gpg-plugin central-publishing-maven-plugin; do \
		grep -q "<artifactId>$$plugin</artifactId>" pom.xml || missing="$$missing $$plugin"; \
	done; \
	if [ -n "$$missing" ]; then \
		echo "$(RED)[ERROR]$(NC) pom.xml would be rejected by Maven Central - missing:$$missing"; \
		echo "$(RED)[ERROR]$(NC) restore the HAND-WRITTEN blocks listed in the header comment of pom.xml"; \
		exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) pom.xml carries every mandatory Maven Central field and publishing plugin"

check_maven_central_credentials: ## Fail loudly when a publishing credential is still a placeholder
# Iterates over the NAMES; each value is looked up through the environment (this Makefile
# exports everything) and is never printed, not even partially.
	@missing=""; \
	for name in MAVEN_CENTRAL_USERNAME MAVEN_CENTRAL_PASSWORD MAVEN_GPG_KEY_B64 MAVEN_GPG_PASSPHRASE; do \
		eval "value=\"\$$$$name\""; \
		case "$$value" in "" | ENTER_HERE_YOUR_*) missing="$$missing $$name";; esac; \
	done; \
	if [ -n "$$missing" ]; then \
		echo "$(RED)[ERROR]$(NC) refusing to publish - no credential for:$$missing"; \
		echo "$(RED)[ERROR]$(NC) use 'make ondewo_release', which reads ${DEVOPS_ACCOUNT_GIT}/account_maven_central.env"; \
		exit 1; \
	fi
	@echo "$(GREEN)[SUCCESS]$(NC) every Maven Central credential is set"

check_release_credentials: check_gh_token check_maven_central_credentials ## Fail loudly when a release credential (GitHub, Maven Central, GPG) is unset or still a placeholder

check_maven_central_token_valid: check_maven_central_credentials ## Assert the Central Portal accepts the user token and this version is not published yet (read-only, needs curl)
# GET /api/v1/publisher/published changes nothing - it is the call central-publishing-maven-plugin
# itself makes for ignorePublishedComponents - and it authenticates like every Portal API call, with
# `Authorization: Bearer base64(<token username>:<token password>)`
# (https://central.sonatype.org/publish/publish-portal-api/). The header reaches curl on stdin
# (`-K -`), never on its command line. Only 200 + published=false lets the release go on: 401/403 is
# a refused token, and published=true means this version already is on Maven Central, where it can
# never be uploaded again.
	@body=$$(mktemp); trap 'rm -f "$$body"' EXIT; \
	auth=$$(printf '%s:%s' "$$MAVEN_CENTRAL_USERNAME" "$$MAVEN_CENTRAL_PASSWORD" | base64 -w0); \
	code=$$(printf 'header = "Authorization: Bearer %s"\n' "$$auth" \
		| curl -sS --connect-timeout 20 --max-time 60 -K - -o "$$body" -w '%{http_code}' "${MAVEN_CENTRAL_PUBLISHED_API}") \
		|| code="no answer"; \
	case "$$code" in \
		200) ;; \
		401|403) echo "$(RED)[ERROR]$(NC) the Central Portal refused MAVEN_CENTRAL_USERNAME/MAVEN_CENTRAL_PASSWORD (HTTP $$code: $$(cat "$$body")) - they must be a Portal USER TOKEN"; exit 1;; \
		*) echo "$(RED)[ERROR]$(NC) unexpected answer from the Central Portal (HTTP $$code): $$(cat "$$body")"; exit 1;; \
	esac; \
	if grep -q '"published"[[:space:]]*:[[:space:]]*false' "$$body"; then \
		echo "$(GREEN)[SUCCESS]$(NC) the Central Portal accepts the user token; ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION} is not published yet"; \
	elif grep -q '"published"[[:space:]]*:[[:space:]]*true' "$$body"; then \
		echo "$(RED)[ERROR]$(NC) ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION} is ALREADY on Maven Central - a published version can never be uploaded again"; exit 1; \
	else \
		echo "$(RED)[ERROR]$(NC) unexpected answer from the Central Portal: $$(cat "$$body")"; exit 1; \
	fi

check_maven_gpg_key_valid: check_maven_central_credentials ## Assert MAVEN_GPG_KEY_B64 is an armoured secret key that signs with MAVEN_GPG_PASSPHRASE (throwaway keyring, needs gpg)
# The release signs with maven-gpg-plugin's `bc` signer, which reads MAVEN_GPG_KEY as ASCII-ARMOURED
# text: a binary export imports fine into gpg and still breaks the deploy, so the armour header line
# is looked for first - anywhere in the key, as the armour parser skips leading lines too - written as
# a regex whose text is not the literal header, which the detect-private-key pre-commit hook would
# flag as a committed key. The key is then imported into a temporary GNUPGHOME that the trap
# deletes, and a detached signature over a probe file proves key, passphrase and expiry in one go.
# Nothing is sent anywhere, and the passphrase reaches gpg on stdin, never on its command line.
	@command -v gpg >/dev/null 2>&1 || { echo "$(RED)[ERROR]$(NC) gpg is not on PATH - run 'make validate_release_credentials_via_docker_image'"; exit 1; }
	@gnupg_home=$$(mktemp -d); \
	trap 'GNUPGHOME="$$gnupg_home" gpgconf --kill gpg-agent >/dev/null 2>&1; rm -rf "$$gnupg_home"' EXIT; \
	chmod 700 "$$gnupg_home"; \
	printf %s "$$MAVEN_GPG_KEY_B64" | base64 -d > "$$gnupg_home/key.asc" 2>/dev/null \
		|| { echo "$(RED)[ERROR]$(NC) MAVEN_GPG_KEY_B64 is not valid base64"; exit 1; }; \
	grep -qE -e '^-{5}BEGIN PGP PRIVATE KEY[[:space:]]BLOCK-{5}' "$$gnupg_home/key.asc" \
		|| { echo "$(RED)[ERROR]$(NC) MAVEN_GPG_KEY_B64 is not an ASCII-armoured PGP private key - encode it with 'gpg --armor --export-secret-keys <id> | base64 -w0'"; exit 1; }; \
	GNUPGHOME="$$gnupg_home" gpg --batch --quiet --import "$$gnupg_home/key.asc" \
		|| { echo "$(RED)[ERROR]$(NC) gpg cannot import the key in MAVEN_GPG_KEY_B64"; exit 1; }; \
	echo "ONDEWO release signing probe" > "$$gnupg_home/probe.txt"; \
	printf '%s' "$$MAVEN_GPG_PASSPHRASE" | GNUPGHOME="$$gnupg_home" gpg --batch --quiet --pinentry-mode loopback \
		--passphrase-fd 0 --detach-sign --armor --output "$$gnupg_home/probe.txt.asc" "$$gnupg_home/probe.txt" \
		|| { echo "$(RED)[ERROR]$(NC) the key in MAVEN_GPG_KEY_B64 cannot sign with MAVEN_GPG_PASSPHRASE (wrong passphrase, expired, or no signing key)"; exit 1; }; \
	GNUPGHOME="$$gnupg_home" gpg --batch --quiet --verify "$$gnupg_home/probe.txt.asc" "$$gnupg_home/probe.txt" 2>/dev/null \
		|| { echo "$(RED)[ERROR]$(NC) the probe signature made with MAVEN_GPG_KEY_B64 does not verify"; exit 1; }; \
	key_id=$$(GNUPGHOME="$$gnupg_home" gpg --batch --with-colons --list-secret-keys | awk -F: '$$1 == "sec" { print $$5; exit }'); \
	echo "$(GREEN)[SUCCESS]$(NC) MAVEN_GPG_KEY_B64 (key $$key_id) signs with MAVEN_GPG_PASSPHRASE"

validate_release_credentials: check_gh_token_valid check_maven_central_token_valid check_maven_gpg_key_valid ## Read-only proof that GitHub, the Central Portal and gpg accept every release credential

validate_release_credentials_via_docker_image: check_release_credentials build_utils_docker_image ## Run `make validate_release_credentials` in the utils image - needs only docker
# Bare `-e NAME` forwards, and the line is @-prefixed: no value is ever spelled out or echoed.
	@${UTILS_DOCKER_RUN} \
		-e GITHUB_GH_TOKEN \
		-e MAVEN_CENTRAL_USERNAME -e MAVEN_CENTRAL_PASSWORD \
		-e MAVEN_GPG_KEY_B64 -e MAVEN_GPG_PASSPHRASE \
		${IMAGE_UTILS_NAME} make validate_release_credentials

dry_run_maven_central: check_central_pom ## Credential-free rehearsal of the release build - signs with a throwaway key and uploads nothing
# `make release` runs this (in the utils image) before its first push. It exercises the whole
# packaging path a release takes - the release profile, the javadoc jar, the gpg signing - and it
# CANNOT publish, because the central-publishing goal is bound to `deploy` and this stops at `verify`.
#
# The key it signs with is generated here, lives in a temp GNUPGHOME that the trap deletes and
# expires in a day. It is not a credential: a green dry run proves the CONFIGURATION signs, it
# proves nothing about ONDEWO's real release key.
#
# The build runs twice, online and then with maven OFFLINE (-o). The offline pass is what
# catches the classic release-day failure: a plugin that is only ever resolved during the
# release and is therefore never proven to be resolvable at all. It covers every phase up to
# `verify`; the `install` and `deploy` phases (maven-install-plugin, the Portal upload) run for
# the first time in the real deploy, online.
	@command -v mvn >/dev/null 2>&1 || { echo "$(RED)[ERROR]$(NC) maven is not on PATH"; exit 1; }
	@command -v gpg >/dev/null 2>&1 || { echo "$(RED)[ERROR]$(NC) gpg is not on PATH - the signing rehearsal needs it"; exit 1; }
	@set -e; \
	gnupg_home=$$(mktemp -d); \
	trap 'rm -rf "$$gnupg_home"' EXIT; \
	chmod 700 "$$gnupg_home"; \
	echo "$(BLUE)[INFO]$(NC) Generating a throwaway signing key ..."; \
	GNUPGHOME="$$gnupg_home" gpg --batch --quiet --pinentry-mode loopback --passphrase dry-run-only \
		--quick-generate-key "ONDEWO Maven Central dry run <office@ondewo.com>" rsa3072 sign 1d; \
	MAVEN_GPG_KEY=$$(GNUPGHOME="$$gnupg_home" gpg --batch --quiet --pinentry-mode loopback \
		--passphrase dry-run-only --armor --export-secret-keys); export MAVEN_GPG_KEY; \
	MAVEN_GPG_PASSPHRASE=dry-run-only; export MAVEN_GPG_PASSPHRASE; \
	echo "$(BLUE)[INFO]$(NC) mvn -Prelease clean verify - builds, tests and signs, uploads nothing ..."; \
	mvn -B --no-transfer-progress -Prelease clean verify; \
	echo "$(BLUE)[INFO]$(NC) re-running the identical build with maven offline ..."; \
	mvn -B --no-transfer-progress -o -Prelease clean verify; \
	echo "$(BLUE)[INFO]$(NC) Checking the deployment bundle ..."; \
	base=target/${MAVEN_ARTIFACT_ID}-${ONDEWO_SIP_VERSION}; \
	for artifact in "$$base.jar" "$$base-sources.jar" "$$base-javadoc.jar"; do \
		test -s "$$artifact" || { echo "$(RED)[ERROR]$(NC) Maven Central requires $$artifact"; exit 1; }; \
		GNUPGHOME="$$gnupg_home" gpg --batch --quiet --verify "$$artifact.asc" "$$artifact" \
			|| { echo "$(RED)[ERROR]$(NC) $$artifact.asc is missing or does not verify"; exit 1; }; \
		echo "  signed and verified: $$artifact"; \
	done; \
	GNUPGHOME="$$gnupg_home" gpg --batch --quiet --verify "$$base.pom.asc" pom.xml \
		|| { echo "$(RED)[ERROR]$(NC) $$base.pom.asc is missing or does not verify"; exit 1; }; \
	echo "  signed and verified: pom.xml"
	@echo "$(GREEN)[SUCCESS]$(NC) the Maven Central deployment bundle builds, signs and verifies"

dry_run_maven_central_via_docker: build_utils_docker_image ## Run `make dry_run_maven_central` in the utils image - needs only docker
	${UTILS_DOCKER_RUN} ${IMAGE_UTILS_NAME} make dry_run_maven_central

publish_to_maven_central: check_central_pom check_maven_central_credentials ## Sign, upload and publish the deployment, and wait until Maven Central reports it PUBLISHED (local JDK + maven)
# autoPublish=true + waitUntil=published in the pom's release profile: the Portal validates the
# deployment and publishes it with no human involved, and maven returns only once Central reports
# it PUBLISHED. A deployment the Portal rejects, or one that is not published within waitMaxTime,
# fails this target. Publishing is irreversible - a version can never be replaced or withdrawn -
# which is why `make release` proves the credentials, the build, the tests and the signing first.
	@sh -c '$(DECODE_MAVEN_GPG_KEY); $(MAVEN_CENTRAL_DEPLOY_CMD)'
	@echo "$(GREEN)[SUCCESS]$(NC) published ${MAVEN_GROUP_ID}:${MAVEN_ARTIFACT_ID}:${ONDEWO_SIP_VERSION} to Maven Central"

push_to_maven_central_via_docker: build_utils_docker_image check_central_pom check_maven_central_credentials ## Run `make publish_to_maven_central` in the utils image, so a release never depends on the local JDK
# Every secret is forwarded with the bare `-e NAME` form, which copies the value out of this
# recipe's environment: no credential is ever spelled out on a command line, and the whole
# recipe is @-prefixed so make does not echo it either.
	@${UTILS_DOCKER_RUN} \
		-e MAVEN_CENTRAL_USERNAME -e MAVEN_CENTRAL_PASSWORD \
		-e MAVEN_GPG_KEY_B64 -e MAVEN_GPG_PASSPHRASE \
		${IMAGE_UTILS_NAME} make publish_to_maven_central

########################################################
#		DEVOPS-ACCOUNTS

ondewo_release: update_pom_version spc run_release_with_devops ## Release with credentials from devops-accounts repo

run_release_with_devops: ## Load the credentials from devops-accounts, delete the clone, then run `make release`
# Exactly the five variables this client needs, each matched as `^NAME=` - ANCHORED, because the
# account_*.env files open with '#' comment lines that mention the same names (the comments of
# account_maven_central.env name MAVEN_GPG_KEY_B64 twice).
# Every account_*.env line is a single VAR=VALUE: that is why the signing key is carried
# base64-encoded (MAVEN_GPG_KEY_B64) - a multi-line armored key could not survive this hand-off.
# The values are exported into the ENVIRONMENT of the sub-make (`set -a`), never passed as
# `make release NAME=<value>`: make's argv - like every argv - is world-readable in
# /proc/<pid>/cmdline. Below, docker gets them with bare `-e NAME` forwards for the same reason.
# The clone is DELETED as soon as the values are in the environment - before `$(MAKE) release`, so no
# container that mounts this checkout can ever see it - and the trap deletes it on every other exit
# (a failed clone or load, Ctrl-C). A DEVOPS_ACCOUNT_DIR handed in by the caller is the caller's to
# delete. The ORDER is pinned by ReleaseOrderTest.
# make RUNS a recipe line that contains $(MAKE) even under `make -n`, so a dry run would clone and load
# the real credentials just to print `make release`; MAKE_DRY_RUN skips straight to the printing.
	@set -e; \
	if [ -n "${MAKE_DRY_RUN}" ]; then \
		echo "$(BLUE)[INFO]$(NC) make -n: not cloning or reading ${DEVOPS_ACCOUNT_GIT}"; \
		$(MAKE) release; exit 0; \
	fi; \
	if [ -n "$$DEVOPS_ACCOUNT_DIR" ] && [ -d "$$DEVOPS_ACCOUNT_DIR" ]; then \
		accounts="$$DEVOPS_ACCOUNT_DIR"; own_clone=; \
		echo "$(BLUE)[INFO]$(NC) reading the credentials from DEVOPS_ACCOUNT_DIR (not cloned, not deleted here)"; \
	else \
		accounts="${DEVOPS_ACCOUNT_CLONE_DIR}"; own_clone=1; \
		trap 'rm -rf "$$accounts"' EXIT; \
		trap 'exit 130' INT TERM HUP; \
		rm -rf "$$accounts"; \
		git clone -q --depth 1 "${DEVOPS_ACCOUNT_GIT_URL}" "$$accounts"; \
	fi; \
	for file in account_github.env account_maven_central.env; do \
		test -f "$$accounts/$$file" || { echo "$(RED)[ERROR]$(NC) $$file is missing from ${DEVOPS_ACCOUNT_GIT}"; exit 1; }; \
	done; \
	set -a; \
	eval "$$(grep -hE '^GITHUB_GH_TOKEN=' "$$accounts/account_github.env"; \
		grep -hE '^(MAVEN_CENTRAL_USERNAME|MAVEN_CENTRAL_PASSWORD|MAVEN_GPG_KEY_B64|MAVEN_GPG_PASSPHRASE)=' \
			"$$accounts/account_maven_central.env")"; \
	set +a; \
	if [ -n "$$own_clone" ]; then rm -rf "$$accounts"; fi; \
	$(MAKE) release

spc: ## Checks if the Release Branch, Tag and pom.xml version already exist
# Exact ref names, never a substring grep: `grep 9.1.0` also matches 19.1.0 and 9.1.0-rc1.
	@if git show-ref -q --verify "refs/heads/release/${ONDEWO_SIP_VERSION}" \
		|| git show-ref -q --verify "refs/remotes/origin/release/${ONDEWO_SIP_VERSION}"; then \
		echo "-- Test 1: Branch exists!!"; exit 1; \
	else echo "-- Test 1: Branch is fine"; fi
	@if git rev-parse -q --verify "refs/tags/${ONDEWO_SIP_VERSION}" >/dev/null; then \
		echo "-- Test 2: Tag exists!!"; exit 1; \
	else echo "-- Test 2: Tag is fine"; fi
	@pom_version=$$(test -f pom.xml && sed -n 's|^  <version>\(.*\)</version>$$|\1|p' pom.xml | head -n 1); \
	if test "$$pom_version" != "" && test "$$pom_version" != "${ONDEWO_SIP_VERSION}"; then \
		echo "-- Test 3: pom.xml is at '$$pom_version', not ${ONDEWO_SIP_VERSION} - run 'make build'!!"; exit 1; \
	else echo "-- Test 3: pom.xml is fine"; fi
