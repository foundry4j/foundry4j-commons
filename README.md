# Foundry4j Commons

<img src="logo.jpg" alt="Foundry4j Commons Logo" align="right" width="128" height="128"/>

 [![Maven Central](https://img.shields.io/maven-central/v/io.github.foundry4j/foundry4j-commons?logo=apachemaven&color=blue)](https://central.sonatype.com/artifact/io.github.foundry4j/foundry4j-commons)
 [![CI](https://github.com/foundry4j/foundry4j-commons/actions/workflows/ci.yml/badge.svg)](https://github.com/foundry4j/foundry4j-commons/actions/workflows/ci.yml)
 [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=foundry4j_foundry4j-commons&metric=coverage)](https://sonarcloud.io/summary/new_code?id=foundry4j_foundry4j-commons)
 ![Java 27+](https://img.shields.io/badge/Java-27%2B-ED8B00?logo=openjdk&logoColor=white)
 [![License: MIT](https://img.shields.io/badge/License-MIT-green)](https://opensource.org/license/mit)

A lightweight, general-purpose utility library for Java 27. It covers strings, collections, streams, I/O, reflection, dates and times, concurrency, networking, and math. Every utility is a stateless final class with static methods, and the runtime code has zero third-party dependencies: only the JDK standard library is used.

## Features

- String utilities: null/empty/blank checks, truncation with a suffix, null-skipping join, null-to-default fallback
- Collection utilities: null-safe emptiness checks, immutable list conversion, grouping by a key extractor, fixed-size partitioning
- Stream utilities: filter/map/distinct with list or set collection, preserving encounter order
- I/O utilities: stream-to-byte-array and stream-to-string conversion, classpath resource reading, stream copy to a path
- Reflection utilities: class hierarchy inspection, field and method lookup including inherited members, annotation lookup (direct and deep), constructor invocation, static and instance method invocation
- Date and time utilities: parsing and formatting for `LocalDate`, `LocalTime`, and `LocalDateTime`, day arithmetic and interval calculation, leap-year and quarter helpers, time-zone conversion and UTC offset formatting, legacy `Date`/`Calendar` bridging
- Concurrency utilities: named thread factories, fixed/cached/single/scheduled thread pools with graceful shutdown, countdown latch, cyclic barrier, and semaphore helpers
- Network utilities: local port availability checks, available-port discovery, local IP detection, private IPv4 detection and masking, IPv4/`long` conversion, URL validation, resolution and encoding
- Math utilities: sum, average, maximum, and minimum over `int` arrays
- Zero runtime dependencies: production code uses only the JDK standard library

## Requirements

- JDK 27 or later
- The library is compiled with `--enable-preview`, so applications using it should also run with `--enable-preview` on JDK 27

## Installation

Maven:

```xml
<dependency>
    <groupId>io.github.foundry4j</groupId>
    <artifactId>foundry4j-commons</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Quick Start

```java
import io.github.foundry4j.commons.collection.CollectionUtils;
import io.github.foundry4j.commons.stream.StreamUtils;
import io.github.foundry4j.commons.string.StringUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

void main() {
    List<String> unique = StreamUtils.distinctToList(
            Stream.of("alice", "bob", "alice"));

    Map<Integer, List<String>> byLength =
            CollectionUtils.groupBy(unique, String::length);

    String label = StringUtils.defaultIfNull(null, "anonymous");
    System.out.println(label + ": " + byLength); // anonymous: {5=[alice], 3=[bob]}
}
```

All utility classes are stateless and expose only static methods; there is nothing to instantiate. Null handling is consistent: emptiness checks treat `null` as empty, `CollectionUtils.toUnmodifiableList` and `CollectionUtils.groupBy` return an empty immutable result for `null`, and methods that require a value fail fast with `NullPointerException` or `IllegalArgumentException`.

## Strings

```java
StringUtils.isEmpty("");                       // true
StringUtils.isNotBlank("  value  ");           // true
StringUtils.truncate("abcdef", 3, "...");      // "abc..."
StringUtils.join(",", "a", null, "b");         // "a,b"
StringUtils.defaultIfNull(null, "anonymous");  // "anonymous"
```

## Collections and Streams

```java
CollectionUtils.isEmpty(List.of());                    // true
CollectionUtils.toUnmodifiableList(null);              // immutable empty list
CollectionUtils.groupBy(List.of("a", "bb"), String::length); // {1=[a], 2=[bb]}
CollectionUtils.partition(List.of(1, 2, 3, 4, 5), 2);  // [[1, 2], [3, 4], [5]]

StreamUtils.filterToList(Stream.of(1, 2, 3), n -> n > 1);      // [2, 3]
StreamUtils.mapToList(Stream.of("a", "b"), String::toUpperCase); // [A, B]
StreamUtils.distinctToList(Stream.of("a", "a", "b"));          // [a, b]
StreamUtils.distinctToSet(Stream.of("a", "a", "b"));           // [a, b] as a LinkedHashSet
```

`groupBy` returns an unmodifiable `LinkedHashMap` that preserves first-encounter order, and `partition` rejects a size less than or equal to zero.

## Date and Time

```java
LocalDate today = DateUtils.today();
String date = DateUtils.formatDate(today, "yyyy-MM-dd");
long days = DateUtils.daysBetween(today, DateUtils.plusDays(today, 7));

LocalDateTime dateTime = DateTimeFormatterUtils.parseDateTime("2026-11-09 10:30:00");
String stamp = DateTimeFormatterUtils.nowCompact();   // yyyyMMddHHmmss
String iso = DateTimeFormatterUtils.nowIso();

ZonedDateTime tokyo = TimeZoneUtils.nowInZone(TimeZoneUtils.ZONE_TOKYO);
boolean workTime = TimeUtils.isWorkTime(LocalTime.of(10, 0)); // 09:00 - 18:00
```

`DateUtils` accepts custom patterns for both parsing and formatting and can convert between `LocalDate` and legacy `java.util.Date`. `DateTimeFormatterUtils` provides ready-made constants such as `PATTERN_DATE`, `PATTERN_DATETIME`, and `PATTERN_ISO_ZONED`. `TimeZoneUtils` ships with constants for Shanghai, UTC, New York, Tokyo, and London, and offers zone ID validation.

## I/O

```java
byte[] bytes = IOUtils.toByteArray(inputStream);
String text = IOUtils.toUtf8String(inputStream);
String config = IOUtils.readResourceAsString("application.properties");
IOUtils.copy(inputStream, Path.of("output.bin"));
```

`toByteArray` and `toString` consume the stream but do not close it; the caller remains responsible for closing. `readResourceAsString` resolves the path through the thread context class loader and throws `IllegalArgumentException` when the resource does not exist.

## Reflection

```java
Object value = FieldUtils.readField(user, "name");
FieldUtils.writeField(user, "name", "alice");

Object result = MethodUtils.invoke(user, "greet", "world");
Object constant = FieldUtils.readStaticField(Constants.class, "VERSION");

List<Field> fields = FieldUtils.getAllFields(User.class);
List<Class<?>> interfaces = ClassUtils.getAllInterfaces(ArrayList.class);
boolean assignable = ClassUtils.isAssignable(Number.class, Integer.class);

User created = ConstructorUtils.newInstance(User.class);
boolean marked = AnnotationUtils.hasAnnotationDeep(User.class, Deprecated.class);
```

The hierarchy-aware lookup helpers (`getField`, `getMethod`, `getAllFields`, `getAllMethods`) search the declared members first and then walk up the superclass chain, and they call `setAccessible` so private members can be read and invoked. `AnnotationUtils` also supports deep lookup through meta-annotations and reading a single annotation attribute by name.

## Concurrency

```java
ThreadPoolExecutor pool = ThreadPoolUtils.newFixedThreadPool(4, "worker");
Future<Integer> future = ThreadUtils.submit(pool, () -> 42);
ThreadPoolUtils.shutdownAndAwait(pool, 5, TimeUnit.SECONDS);

CountDownLatch latch = JucUtils.newCountDownLatch(1);
JucUtils.countDown(latch);
JucUtils.await(latch, 1, TimeUnit.SECONDS);

Semaphore semaphore = JucUtils.newSemaphore(2);
JucUtils.tryAcquire(semaphore, 1, TimeUnit.SECONDS);
JucUtils.release(semaphore);
```

The fixed, cached, single-thread, and scheduled pools created by `ThreadPoolUtils` all use named threads (`prefix-1`, `prefix-2`, ...); the non-scheduled pools are backed by a `LinkedBlockingQueue`. `shutdownAndAwait` waits for termination and falls back to `shutdownNow` when the timeout expires. `ThreadUtils` also provides interruption-aware `sleep`, `join`, plus daemon-thread creation and thread state inspection.

## Network

```java
boolean free = NetUtils.isLocalPortAvailable(8080);
int port = NetUtils.findAvailablePort();
String ip = NetUtils.getLocalIp();
String masked = NetUtils.hideIpPart("192.168.1.100");

String url = NetUtils.toAbsoluteUrl("https://example.com/api/", "users");
boolean valid = NetUtils.isValidUrl(url);
String encoded = NetUtils.urlEncode("a b");
long numeric = NetUtils.ipv4ToLong("127.0.0.1");
```

Port checks bind a local socket and release it immediately. `findAvailablePort()` asks the operating system to assign an ephemeral port, while the range overload scans for a free port and throws `IllegalStateException` when the whole range is occupied. `hideIpPart` masks the last IPv4 segment for logging or display.

## Math

```java
MathUtils.sum(1, 2, 3);      // 6L
MathUtils.average(1, 2, 3);  // 2.0
MathUtils.max(3, 1, 2);      // 3
MathUtils.min(3, 1, 2);      // 1
```

`sum` accumulates into a `long` to avoid integer overflow. `average`, `max`, and `min` throw `IllegalArgumentException` for an empty array.

## API Overview

| Package | Class | Responsibility |
| --- | --- | --- |
| `collection` | `CollectionUtils` | Emptiness checks, immutable lists, grouping, partitioning |
| `concurrent` | `JucUtils` | `CountDownLatch`, `CyclicBarrier`, and `Semaphore` helpers |
| `concurrent` | `ThreadPoolUtils` | Named thread factories, thread pool creation and lifecycle |
| `concurrent` | `ThreadUtils` | Sleep, join, thread creation, submission, and state inspection |
| `date` | `CalendarUtils` | Legacy `Calendar` helpers: week, quarter, month and year boundaries |
| `date` | `DateTimeFormatterUtils` | `LocalDateTime` parsing/formatting and common pattern constants |
| `date` | `DateUtils` | `LocalDate` parsing/formatting, day arithmetic, `Date` bridging |
| `date` | `TimeUtils` | `LocalTime` parsing/formatting, duration, work-time checks |
| `date` | `TimeZoneUtils` | Zone constants, zone conversion, UTC offsets, DST checks |
| `io` | `IOUtils` | Stream reading, classpath resources, stream copy |
| `math` | `MathUtils` | Sum, average, maximum, and minimum |
| `net` | `NetUtils` | Ports, local IP, IPv4 conversion and masking, URLs |
| `reflect` | `AnnotationUtils` | Direct and deep annotation lookup, attribute reading |
| `reflect` | `ClassUtils` | Names, type predicates, hierarchy inspection, class loading |
| `reflect` | `ConstructorUtils` | Constructor lookup and instance creation |
| `reflect` | `FieldUtils` | Field lookup, read/write, static access, modifiers |
| `reflect` | `MethodUtils` | Method lookup, invocation, getter/setter detection |
| `stream` | `StreamUtils` | Filter, map, and distinct collectors |
| `string` | `StringUtils` | Null/blank checks, truncation, join, default value |