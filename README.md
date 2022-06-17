<p align="center">
    <img width="60%" src="https://user-images.githubusercontent.com/16124324/174398253-36eff685-846b-4b51-949e-9b8004678195.png#gh-dark-mode-only" alt="Banner" />
    <img width="60%" src="https://user-images.githubusercontent.com/16124324/174398256-3c886e3f-11ab-4f39-967d-39d0eaf0c6be.png#gh-light-mode-only" alt="Banner" />
    <br>
    <a href="https://nightly.link/iAmGio/pikt/workflows/maven/master/pikt.zip">
        <img width="25%" src="https://img.shields.io/badge/Download%20latest-snapshot-brightgreen.svg" alt="Download">
    </a>
</p>
<br>

> _Ah a language to code by while listening to Pink Floyd_ - a guy from Reddit

---

<br>

Pikt is a pixel-based, Turing complete esoteric programming language that generates fast and lightweight programs out of aesthetically pleasant image sources.
Indeed, Pikt's most interesting feature is flexibility: every keyword, statement, function, operator and so on is linked to one - or more - color, which can be easily customized via [color schemes](core/src/main/resources/colors.properties).

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <br>
  <a href="https://www.youtube.com/watch?v=Wr7RJqqrw7s">
    <img width="55%" src="https://i.imgur.com/rYscSbe.png" alt="Video" /><br>
  </a>
  <br>
</p>
  
Pikt compiles executables via the Kotlin compiler, therefore compilation for both JVM and native* targets is available, along with runtime interpretation.

Make sure to check out the **[wiki](https://github.com/iAmGio/pikt/wiki)** to learn how to create your first Pikt program! 

_* Native support is limited due to the lack of Kotlin/Native libraries and is being discontinued._

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <br>
  <a href="https://github.com/iAmGio/pikt/wiki/Fibonacci-breakdown">
    <img width="130" src="https://i.imgur.com/1KFhhic.png" alt="Fibonacci" /><br>
  </a>
  <i>A high-performance Fibonacci sequence algorithm.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Prime-numbers-breakdown">
    <img width="130" src="https://i.imgur.com/YKm9bFD.png" alt="Prime numbers" /><br>
  </a>
  <i>A prime numbers algorithm.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/String-reverser-breakdown">
    <img width="130" src="https://i.imgur.com/4Kz9DsI.png" alt="Reverser" /><br></a>
  <i>A string reverser.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Tree-breakdown">
    <img width="130" src="https://i.imgur.com/aKg4I59.png" alt="Tree" /><br></a>
  <i>A tree that prints "A tree!".</i>
</p>

These examples use [custom color schemes](core/src/test/resources/schemes).  
Click on the examples for a breakdown/explanation.

## Table of contents

- [Properties](#properties)
- [Settings arguments](#properties)
- [Command arguments](#command-arguments)
- [Building](#building)
- [Roadmap](#roadmap)

## Properties
The following properties define parameters needed by Pikt.  
Example: `java -Dproperty=value -jar pikt.jar -argument`.  

- `-Dsource` source image file;


- `-Doutput` output name without extension. It will take `source` name without extension if not specified;


- `-Dcolors` path to the `.properties` color scheme without extension.  
Default values will be used if not specified (not recommended);


- `-Dtargets` compilation targets divided by a comma. Can be `jvm`, `windows`, `osx` or `linux`.
Note that Windows and OSX executables can be generated only on those platforms. No compilation will be executed if not specified;


- `-Dlib` path to JAR libraries, including the bundled `stdlib.jar` file, divided by a comma.
If not specified, points by default to `./libraries/stdlib.jar`; 


- `-Djvmcompiler` path to the Kotlin/JVM (`kotlinc`) executable compiler. Required if `target` contains `jvm` or if `-interpret` is used;


- `-Dnativecompiler` path to the Kotlin/Native (`kotlinc`) executable compiler. Required if `target` contains a native target;


- `-Dproject` optional path to a [project info](https://github.com/iAmGio/pikt/wiki/Project-info-file) YAML configuration.

## Settings arguments

The following arguments enable settings that affect Pikt's behavior.

- `-interpret` runs the generated code via the JVM compiler;


- `-printoutput` prints the generated Kotlin code;


- `-nocompile` prevents the generation of any executable file;


- `-pixelinfo` adds information about pixel coordinates to the output code as comments;


- `-imgoutput=path` sets the output file for image-generating commands (see below). If not specified, defaults to the source image path followed by a suffix.

## Command arguments

The following arguments execute tasks and exit when completed.

- `-downloadcompiler=type[,version]` downloads the zipped Kotlin compiler for the given platform (`jvm`, `windows`, `macos`, `linux`).   
`version` defaults to `1.5.10`.


- `-createscheme` creates a new [color scheme](core/src/main/resources/colors.properties) with default values.  
It automatically appends library colors too, i.e. as the [stdlib scheme](stdlib/src/main/resources/colors.properties), loaded from `-Dlib`;


- `-exportscheme` generates a useful color palette image out of the given color scheme;


- `-recolorize[=method]` creates a copy of the source image (that relies on the default scheme) and adapts it to a custom scheme (specified by `-Dcolors`).  
`method` defines the way properties with more than one color are handled; it can be either `first` (default), `last` or `random`;


- `-standardize` creates a copy of the source image (that relies on a custom scheme) and adapts it to the default scheme;


- `-compact[=size]` creates a compacted copy of the source image.  
If `size` is not specified, it will try to create a square-ish image with no whitespaces.  
`size` can be defined via `w?h?`, where both `w`and `h` are optional (in case one is missing, it will be calculated the same way as before) (e.g. `w10h5`, `w10`, `h5`);


- `-decompact` creates a decompacted copy of the source image with a statement per line;


- `-standardecompact` runs `-standardize` + `-decompact`;


- `-strconvert=string` converts a string into a sequence of RGB (grayscale) values supported by Pikt and prints them out.  
  _See [Hello world!](https://github.com/iAmGio/pikt/wiki/Hello-world) for further information._  
For instance, `-strconvert="Hello Pikt!"` prints:
```
RGB:  72  101  108  108  111  32  80  105  107  116  33  
      H   e    l    l    o        P   i    k    t    !   
```

- `-welcome` runs `-createscheme`, `-exportscheme` (both on `colors`) and `-downloadcompiler=jvm`. Its output is already zipped in the downloadable file.

> Commands can be chained.
> For example, `java -Dsource=img.png -Dcolors=scheme -jar pikt.jar -imgoutput=img_new.png -recolorize -compact`
> applies the `scheme` color scheme to `img.png` and compacts the output to `img_new.png` at the same time.
> 
> It is possible to overwrite the source image, albeit highly discouraged.

## Building
The downloadable archive is already built off the latest GitHub commit.
If you wish to build it yourself from source just run `mvn clean install`.

As a bonus tip, consider setting your run configuration to execute `mvn clean install -pl stdlib -am` in order to compile the standard library before launching Pikt.   
If you are using IntelliJ IDEA consider importing configuration templates from the [runConfigurations](runConfigurations) folder.

## Roadmap

**Code**
- [x] Variables
- [ ] Constants
- [x] Function calls (both as part of expressions and standalone)
- [x] Function definition
- [x] If / else / if else
- [x] Lambdas
- [x] Operators (equality, logical and arithmetic)
- [x] Loops (`for-each` + `range` function for indexed `for` loop)
- [ ] Try/catch
- [x] Structs
- [ ] [Standard library](stdlib) (2%, see [CONTRIBUTING](CONTRIBUTING.md) for contribution guidelines)
- [x] External libraries support (following certain standards, wiki in progress)

**Generation**
- [x] Compilation (JVM and Native)
- [x] Interpretation (JVM)
- [x] Error handling
- [ ] Runtime information
