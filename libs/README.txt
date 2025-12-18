Local mod jars for development
==============================

Place the exact game jars you use in your modpack here so Gradle can resolve them without remote Maven access:

- create-1.20.1-6.0.8.jar
- Sync-0.1.8.jar

Why: Public Maven coordinates for these specific builds are not guaranteed/consistent across hosts, and the build must match your in-game versions exactly. Using local flatDir resolution guarantees the code compiles against the same artifacts you play with.

After dropping the jars in this folder, run `./gradlew build` (generate the wrapper first if missing: `gradle wrapper --gradle-version 8.14.3`).
