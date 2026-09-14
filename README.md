[![Build Status](https://github.com/akuhtz/oxbow/workflows/Java%20CI/badge.svg)](https://github.com/akuhtz/oxbow/actions)
[![Maven Central](https://img.shields.io/maven-central/v/org.bidib.org.oxbow/swingbits.svg?label=Maven%20Central&style=flat)](https://search.maven.org/artifact/org.bidib.org.oxbow/swingbits)

**SwingBits** is a collection of useful components and utilities for Java Swing Toolkit. 

The documentation can be found in the [Project Wiki](https://github.com/eugener/oxbow/wiki/_pages).   

```
 NOTE: The TaskDialog library is becoming a part of SwingBits starting with SwingBits v1.0 
```

Here are some of its features:
   
#### Table Filtering

![Table Filtering](https://github.com/eugener/oxbow/wiki/images/TableFiltering-seach-actions.png)

#### Task Dialogs    

![Nimbus LAF Task Dialog](https://github.com/eugener/oxbow/wiki/images/TaskDialog-error-metal.png)

![Windows LAF Task Dialog](https://github.com/eugener/oxbow/wiki/images/TaskDialog-error-win.png)

![Task Dialog 3](https://github.com/eugener/oxbow/wiki/images/TaskDialog-error-mac.png)

![Task Dialog 5](https://github.com/eugener/oxbow/wiki/images/TaskDialog-commandLinks-win.png)

![Windows LAF Exception Dialog](https://github.com/eugener/oxbow/wiki/images/TaskDialog-exception-win.jpg)

## One-time setup
 
Copy the relevant bits from [`settings.xml.example`](./settings.xml.example)
into your own `~/.m2/settings.xml`:

- a `<server id="sonatype-central-portal">` entry with your token as
  username/password, and `njord.publisher` / `njord.releaseUrl` configuration
  so Njord knows this server means "publish to Central Portal"
- (optional) a `<pluginGroup>` entry so you can type `mvn njord:...` instead
  of the fully-qualified plugin coordinates

For the GPG passphrase, the **recommended** approach is to *not* put it in
`settings.xml` at all — export it as an environment variable right before
signing:

```bash
export MAVEN_GPG_PASSPHRASE="your-passphrase"
```

(`settings.xml.example` also shows the older `<server id="gpg.passphrase">`
convention, which still works but is discouraged since 3.2.x of the plugin.)

## Build & test (no signing, no publishing)

```bash
mvn verify
```

The `release` profile (and therefore GPG signing) is *not* active by
default, so this works without a GPG key present.

## Stage, sign, and publish

1. **Locally stage** the release (this creates a Njord "store"; the `id`
   before `::njord:` is a throwaway value just to satisfy deploy-plugin
   syntax):

   ```bash
   mvn -Prelease deploy -DaltDeploymentRepository=id::njord:
   ```

   Because the `release` profile is active, `maven-gpg-plugin` signs the
   jar, sources jar, javadoc jar and POM during the `verify` phase, before
   Njord stages everything locally.

2. **List staged stores** to get the store name:

   ```bash
   mvn njord:list
   ```

3. **Inspect** what got staged (optional but recommended):

   ```bash
   mvn njord:list-content -Dstore=release-xxx
   ```

4. **Validate** the store against Central's requirements (optional but
   recommended — catches missing sources/javadoc/signatures/POM metadata
   before you publish):

   ```bash
   mvn njord:validate -Ddetails -Dstore=release-xxx
   ```

5. **Publish** to Central Portal:

   ```bash
   mvn njord:publish -Dstore=release-xxx -Dpublisher=sonatype-cp
   ```

6. If publish succeeds, the store is dropped automatically and your release
   is now on Central Portal (check https://central.sonatype.com/publishing
   to confirm/finalize, depending on whether your namespace has automatic
   publishing enabled).

If you ever want to abandon a staged release instead of publishing it:

```bash
mvn njord:drop -Dstore=release-xxx
```

## Notes

- Replace the placeholder `groupId`, `url`, `scm`, `developers` and
  `licenses` in `pom.xml` with real values — Central Portal validates that
  this metadata is present and rejects releases that lack it.
- `version.njord` in `pom.xml` should be bumped to whatever the
  [latest Njord release](https://github.com/maveniverse/njord/releases) is
  at the time you use this.
- Signing is scoped to the `release` profile so everyday `mvn test` /
  `mvn install` don't require a GPG key on every dev machine / CI job.