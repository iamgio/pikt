<p id="begin" align="center">
    <img width="60%" src="https://user-images.githubusercontent.com/16124324/174399599-462c8ab7-e76d-4ea9-9589-022092ff0074.png#gh-dark-mode-only" alt="Banner" />
    <img width="60%" src="https://user-images.githubusercontent.com/16124324/174399545-b753a93e-3b5e-4ef5-8c16-0d7e4f012218.png#gh-light-mode-only" alt="Banner" />
    <br>
    <a href="https://nightly.link/iAmGio/pikt/workflows/maven/master/pikt.zip">
        <img width="35%" src="https://user-images.githubusercontent.com/16124324/174477531-eff55703-63df-474a-a617-a6e51293fb1c.svg" alt="Download">
    </a>
</p>
<br>

> _Ah a language to code by while listening to Pink Floyd_ - a guy from Reddit

> _This looks like it belongs on the list of those things I totally want to do if I ever become immortal_ - another guy from Reddit

---

<br>

Pikt is a pixel-based, Turing complete esoteric programming language that is able to generate fast and lightweight programs out of **aesthetically pleasant image sources**.  
Indeed, Pikt's most interesting feature is **flexibility**: every keyword, statement, function and operator
is linked to one - or more - color, easily customizable via [color schemes](core/src/main/resources/colors.properties).

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <br>
  <a href="https://www.youtube.com/watch?v=Wr7RJqqrw7s">
    <img width="55%" src="https://i.imgur.com/rYscSbe.png" alt="Video" /><br>
  </a>
  <br>
</p>
  
Pikt compiles executables via the Kotlin compiler, therefore compilation for both JVM and native* targets is available, along with runtime interpretation.

> Want to create your first Pikt program? **Check out the [wiki](https://github.com/iAmGio/pikt/wiki)**!

<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <br>
  <a href="https://github.com/iAmGio/pikt/wiki/Fibonacci-breakdown">
    <img width="130" src="https://i.imgur.com/lytDoDv.png" alt="Fibonacci" /><br>
  </a>
  <i>A high-performance Fibonacci sequence algorithm.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Prime-numbers-breakdown">
    <img width="130" src="https://i.imgur.com/bP5fuyc.png" alt="Prime numbers" /><br>
  </a>
  <i>A prime numbers algorithm.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Insertion-sort-breakdown">
    <img width="200" src="https://i.imgur.com/2zNX4eT.png" alt="Insertion sort" /><br>
  </a>
  <i>An insertion sort algorithm.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/String-reverser-breakdown">
    <img width="130" src="https://i.imgur.com/4Kz9DsI.png" alt="Reverser" /><br></a>
  <i>A string reverser.</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Tree-breakdown">
    <img width="130" src="https://i.imgur.com/aKg4I59.png" alt="Tree" /><br></a>
  <i>A tree that prints "A tree!".</i>
</p>

These examples take advantage of [custom color schemes](core/src/test/resources/schemes).  
Click on the examples for a breakdown/explanation.

<br>

> **[How does Pikt work?](https://github.com/iAmGio/pikt/wiki/How-does-Pikt-work%3F)**

<br>

## Table of contents

- [Properties](#properties)
- [Settings arguments](#properties)
- [Command arguments](#command-arguments)
- [Building](#building)
- [Roadmap](#roadmap)

## Properties
The following properties define parameters needed by Pikt to run.  
Syntax: `java -Dproperty=value -jar pikt.jar -arguments`.  

- `-Dsource` source image file;


- `-Doutput` output name without extension. Defaults to the value of `source` if not specified;


- `-Dcolors` path to the `.properties` color scheme without extension.  
Default values will be used if not specified (not recommended);


- `-Dtargets` compilation targets divided by a comma. Only `jvm` is supported,
while `windows`, `osx` and `linux` compilation is currently disabled.


- `-Dlib` path to JAR libraries, including the bundled `stdlib.jar` file, divided by a comma.
If not specified, points by default to `./libraries/stdlib.jar`; 


- `-Djvmcompiler` path to the Kotlin/JVM (`kotlinc`) executable compiler. Required if `target` contains `jvm` or if `-interpret` is used;


- `-Dproject` optional path to a [project info](https://github.com/iAmGio/pikt/wiki/Project-info-file) YAML configuration.


- `-Dtask` optional [task]((https://github.com/iAmGio/pikt/wiki/Project-info-file)) name (defined within a project info configuration) to be executed.

## Settings arguments

The following arguments enable settings that affect Pikt's behavior.

- `-interpret` runs the generated code via the JVM compiler;


- `-printoutput` displays the generated Kotlin code;


- `-nocompile` prevents the generation of any executable file;


- `-pl[=type]` sets the active pixel logger type.
A pixel logger is responsible for printing pixels on screen,
for example in case of compile-time errors.  
If not set, it is disabled by default. If `type` is not specified, `rgb` is used.  
Available types:
  - `rgb`: prints each pixel as a square with its RGB color.
  Not all terminals support this;
  - `box`: prints each pixel in an ASCII box with its hex code inside.


- `-pixelinfo` adds information about pixel coordinates to the output code as comments;


- `-imgoutput=path` sets the output file for image-generating commands (see below). If not specified, defaults to the source image path followed by a suffix;  


- `-chainoutput` enables output chaining for image-generating commands: the output of a command becomes the input for the next one. It requires `-imgoutput` to be set.

## Command arguments

The following arguments execute tasks and exit when completed.  
These are handy shortcuts that replace several manual actions, such as image transformations, while being unrelated to code generation and compilation.

- `-downloadcompiler=type[,version]` downloads the zipped Kotlin compiler for the given platform (`jvm`, `windows`, `macos`, `linux`).   
`version` defaults to `1.7.20`.


- `-createscheme` creates a new [color scheme](core/src/main/resources/colors.properties) with default values.  
It automatically appends library colors too, e.g. the [stdlib scheme](stdlib/src/main/resources/colors.properties), loaded from `-Dlib`;


- `-exportscheme` generates a useful color palette image out of the given color scheme;


- `-recolorize[=method]` creates a copy of the source image (that relies on the default scheme) and adapts it to a custom scheme (specified by `-Dcolors`).  
`method` defines the way properties with more than one color are handled; it can be either `first` (default), `last` or `random`;


- `-standardize` creates a copy of the source image (that relies on a custom scheme) and adapts it to the default scheme;


- `-compact[=size]` creates a compacted copy of the source image with no whitespaces between pixels.  
If `size` is not specified, it will try to create a square-ish image.  
`size` can be defined via `w?h?`, where both `w`and `h` are optional (in case one is missing, it will be calculated the same way as before) (e.g. `w10h5`, `w10`, `h5`);


- `-decompact` creates a decompacted copy of the source image with one statement per line;


- `-standardecompact` runs `-standardize` + `-decompact`;


- `-colorswap=<swaps>` swaps colors from the source image.  
`swaps` is defined as `from1:to1,from2:to2,...` where `from` and `to` are hexadecimal colors; 


- `-mask=path` creates a masked copy of the source image, loading the mask image from `path`;


- `-strconvert[=string]` converts a string into a sequence of RGB (grayscale) values supported by Pikt and prints them out.  
If `string` is not specified, input is read from stdin.  
_See [Hello world!](https://github.com/iAmGio/pikt/wiki/Hello-world) for further information._  
For instance, `-strconvert="Hello Pikt!"` prints:
```
RGB:  72  101  108  108  111  32  80  105  107  116  33  
      H   e    l    l    o        P   i    k    t    !   
```

- `-welcome` runs `-createscheme`, `-exportscheme` (both on `colors`), `-downloadcompiler=jvm` and creates a ready-to-use Hello World source.
Its output is already zipped in the downloadable archive.


More in-depth information about image transformation commands can be found [here](https://github.com/iAmGio/pikt/wiki/Image-transformation-commands).

## Building
The [downloadable archive](#begin) is already built off the latest GitHub commit.
If you wish to build it yourself from source simply run `mvn clean install`.

> **Tip:** setting your run configuration to execute `mvn clean install -pl stdlib -am` compiles the standard library before launching Pikt, in case you need to make frequent updates to it.   
If you are using IntelliJ IDEA consider importing configuration templates from the [runConfigurations](runConfigurations) folder.

## Roadmap

**Code**
- [x] Variables
- [ ] Constants
- [x] Function calls (both as part of expressions and standalone)
- [x] Function definition
- [x] If / else / if else
- [x] Lambdas (code blocks)
- [x] Operators (equality, logical and arithmetic)
- [x] Loops
  - [x] For-each
  - [x] Indexed for (for-each + `range` function)
  - [x] While
- [ ] Try/catch
- [x] Structs
- [x] [Standard library](stdlib) (work in progress, see [CONTRIBUTING](CONTRIBUTING.md) for contribution guidelines)
- [x] External libraries support (following certain standards, wiki in progress)

**Generation**
- [x] Compilation (JVM ~~and Native~~*)
- [x] Interpretation (JVM)
- [x] Error handling
- [ ] Runtime information

_* Native support is limited due to the lack of Kotlin/Native libraries and is being temporarily discontinued._