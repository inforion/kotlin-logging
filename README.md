# kotlin-logging

Simple Kotlin logging library with color support for Unix-like systems written in pure Kotlin.

Available different levels of logging for messages: `severe`, `warning`, `config`, `info`, `fine`, `finer`, `finest`. These message's types are similar to standard Java logging library. Also two additional logging levels exist: `debug` and `trace`. 

## Example

First add to your `build.gradle` import of Kotlin-logging library either from JitPack or Maven Central:

```Gradle
repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation group: 'com.github.inforion', name: 'kotlin-logging', version: '0.2.3'
}
```

To use Kotlin-logging library in the code just import it and create log field in `object` or `companion object` (for class) and then use this variable to do actual log, see example below:

```Kotlin
import ru.inforion.lab403.common.logging.logger
import ru.inforion.lab403.common.logging.TRACE

object Application {
    val log = logger(TRACE)

    @JvmStatic
    fun main(args: Array<String>) {
        log.severe { "This is severe message" }
        log.warning { "This is warning message" }
        log.info { "This is info message" }
        log.config { "This is config message" }
        log.fine { "This is fine message" }
        log.finer { "This is finer message" }
        log.finest { "This is finest message" }
        log.debug { "This is debug message" }
        log.trace { "This is trace message" }
    }
}
```

![image](https://user-images.githubusercontent.com/2856140/99907902-9942d800-2cf0-11eb-8f19-a2149242dbe8.png)

Log level parameter is not mandatory and by default `FINE` used in `logger()` function.
Library outputs log with format compatible with IntelliJ IDEA, so all places are clickable and coupled with codeplace where this logging message emit.
NOTE: Due to heavily inlining IntelliJ may jump to the wrong place in some cases.

Logging methods are only accept lamda (no string-parameter methods!) and make actual string only if logging level is met minimum log level condition for this logger. So you may place all your code (even with heavy computation) in lambda and it will be executed only if log message emit.

Another example of using Kotlin-logging library for class case:

```Kotlin
import ru.inforion.lab403.common.logging.logger

class Lumberjack {
    companion object {
        val log = logger()
    }

    var count = 0

    fun saw() {
        count++
        log.config { "I saw $counts logs" }
    }
}
```

## Configuration

Companion object is lazy in Kotlin so loggers will not be created when program start. But actually it constructed only when we do something with them. So to configure loggers in runtime two methods available: 
- `Logger.onCreate { logger -> ... }` - execute code for each newly created logger
- `Logger.forEach { logger -> ... }` - execute code for each already existed logger

Using these methods you could change logger options, i.e.: publishers, flush, level.

## Flush

Loggers by default flush messages immediately. If you don't want flush it immediately you could use `flush` parameter for each logger definition. 

```Kotlin
val log = logger(flush = false)
```

To configure all loggers at once `onCreate` method may be used:

```Kotlin
Logger.onCreate { it.flushOnPublish = false }
```

But if some message should be flushed immediately each logging method have `flush` option.

```
log.info(true) { "This message flushed immediately in anycase" }
```

## Publishers

Also if you want to add other publisher for all created loggers you could use method `onCreate` and `addPublisher`, i.e.:

```Kotlin
Logger.onCreate { it.addPublisher(BeautyPublisher.file("my_log_file.txt")) }
```

or create new one publisher type:

```Kotlin
val publisher = object : AbstractPublisher("MyPublisher") {
    override fun flush() = Unit

    override fun publish(message: String, record: Record) {
        println("${record.logger.name} -> $message")
    }
}

Logger.forEach { it.addPublisher(publisher) }.onCreate { it.addPublisher(publisher) }
```

Please refer to `BeautyPublisher` for other publisher options.

## Serialization

If you want to serialize object (class) with logger just put a `Transient` annotation to this field, i.e.:

```Kotlin
import ru.inforion.lab403.common.logging.logger

class Lumberjack {
    companion object {
        @Transient val log = logger()
    }

    var count = 0

    fun saw() {
        log.config { "I saw ${++count} logs" }
    }
}
```

[![](https://jitpack.io/v/inforion/kotlin-logging.svg)](https://jitpack.io/#inforion/kotlin-logging)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.inforion/kotlin-logging/badge.svg)](https://mvnrepository.com/artifact/com.github.inforion/kotlin-logging)
