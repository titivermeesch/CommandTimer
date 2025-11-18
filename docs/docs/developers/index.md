---
sidebar_position: 7
---

# Developers

## Installation

### Gradle

```groovy
repositories {
   maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation 'com.github.titivermeesch:CommandTimer:<version>'
}
```

### Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

```xml
<dependency>
    <groupId>com.github.titivermeesch</groupId>
    <artifactId>CommandTimer</artifactId>
    <version>version</version>
</dependency>
```

## Usage

The preferred way to interact with CommandTimer is through [extensions](../extensions/index.md). If certain
functionalities are not available there, the internal CommandTimer API can be used.

**There is no official documentation for this yet, so you will have to look into the source code for now**

## Contributing

Did you find a bug or want to make a general improvement to the plugin or the documentation? Feel free to open a PR!
