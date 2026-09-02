# AGENTS.md

## Big picture
- Apache JMeter is a Gradle multi-project build (`settings.gradle.kts`) with protocol modules under `src/protocol/*`, shared engine code in `src/core`, utilities in `src/jorphan`, and packaging in `src/dist`.
- Build logic is centralized in included builds `build-logic` and `build-logic-commons`; most subprojects apply convention plugins rather than duplicating task config.
- Dependency versions are centrally aligned via BOM modules (`src/bom`, `src/bom-thirdparty`, `src/bom-testing`) that are consumed from modules with `platform(projects.src.bom*)` (for example in `src/dist/build.gradle.kts` and `src/dist-check/build.gradle.kts`).
- Runtime distribution is assembled by `:src:dist:createDist`, which repopulates `lib/*.jar`, `lib/ext/ApacheJMeter*.jar`, and `bin/ApacheJMeter.jar` from project/runtime dependencies.
- App startup flows through `src/launcher/src/main/java/org/apache/jmeter/NewDriver.java`, which discovers JMeter home and loads jars from `lib`, `lib/ext`, and `lib/junit`.
- Test plans/results serialization is handled by `src/core/src/main/java/org/apache/jmeter/save/SaveService.java` using aliases from `saveservice.properties`.

## High-value directories
- `src/core`: core engine + GUI support; generates version source at build time (`versionClass` task in `src/core/build.gradle.kts`).
- `src/protocol/http` (and sibling protocol modules): protocol-specific samplers/config; depends on `src/core`.
- `src/dist`: distribution assembly, docs/site generation, `runGui`, and release archives.
- `src/dist-check`: batch/non-GUI functional test harness (`batch*` tasks) and release validation.
- `src/release`: release vote/staging wiring (`prepareVote`, `publishDist`) and vote template generation (`src/release/build.gradle.kts`).
- `xdocs`: XML docs input; transformed into site/printable docs by tasks in `src/dist/build.gradle.kts`.

## Developer workflows (project-specific)
- Full build + checks: `./gradlew build` (uses toolchains; defaults to JDK 17 build targeting Java 8 per `README.md`).
- Fast local GUI loop: `./gradlew runGui` (depends on `createDist`, runs `org.apache.jmeter.NewDriver`).
- Rebuild runnable distro only: `./gradlew createDist` (note: refreshes `lib/`, keeps third-party jars in `lib/ext`).
- Focused module test: `./gradlew :src:core:test` (or any subproject path).
- Batch compatibility tests: `./gradlew :src:dist-check:batchTests` (flaky network-dependent tasks are disabled unless `-PenableFlaky`).
- Style/static checks: `./gradlew style` and `./gradlew checkstyleAll`; see `build-logic/verification` plugins.
- Release candidate flow (ASF-like local environment by default): `./gradlew prepareVote -Prc=1` then `./gradlew publishDist -Prc=<n> -Pasf` (see `gradle.md`).
- Discover supported build flags: `./gradlew parameters`.

## Conventions and patterns to follow
- Prefer Gradle type-safe project accessors (`projects.src.core`) over string paths in dependencies.
- Use `because("...")` for non-obvious dependency reasons (common in `src/core/build.gradle.kts`, `src/protocol/http/build.gradle.kts`).
- Choose the right convention plugin: published artifacts use `build-logic.jvm-published-library`; internal-only modules use `build-logic.jvm-library`.
- Keep publication intent aligned with root guardrails in `build.gradle.kts` (`publishedProjects`/`notPublishedProjects` fail the build if plugin choice and publishability diverge).
- Reuse `java-test-fixtures`/`testFixtures(...)` for shared test utilities (`src/testkit`, `src/testkit-wiremock`).
- Keep ASF license headers aligned with `config/license.header.java`; RAT (`rat` task) is part of release validation.
- Do not bypass dependency integrity checks: settings-level SHA-512 allowlist is enforced in `settings.gradle.kts`.

## Integration points and cross-component behavior
- Plugin/classpath extension points are configured in `bin/jmeter.properties` (`search_paths`, `user.classpath`, `plugin_dependency_paths`).
- Distribution dependency drift is guarded by `:src:dist:verifyReleaseDependencies`; update expected list with `-PupdateExpectedJars` when intentionally changing shipped external jars.
- `src/dist-check` tests run JMX plans against the assembled distro and extra libs in `lib/opt`, so distro assembly tasks are upstream dependencies.
- Site/release artifacts are generated from `xdocs` and packaging tasks in `src/dist`; avoid editing generated outputs under `build/`.
