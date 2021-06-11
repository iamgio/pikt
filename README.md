# Pikt [![Download](https://img.shields.io/badge/Download%20latest-snapshot-blue.svg)](https://nightly.link/iAmGio/pikt/workflows/maven/master/pikt.zip)

> _Ah a language to code by while listening to Pink Floyd_ - a guy from Reddit

Pikt is a pixel-based esoteric programming language that aims at generating fast and lightweight programs from aesthetically pleasant image sources: to accomplish this, the colors of all "keywords", functions, operators and so on are easily customizable.
  
Pikt compiles executables via the [Kotlin compiler](https://kotlinlang.org/docs/command-line.html), therefore compilation for both JVM and native targets is available, along with runtime interpretation.

Make sure to check out the **[wiki](https://github.com/iAmGio/pikt/wiki)** to learn how to create your first Pikt program! 

<p align="center">
  <br>
  <a href="https://github.com/iAmGio/pikt/wiki/Fibonacci:-breakdown">
    <img width="130" src="https://i.imgur.com/1KFhhic.png" alt="Fibonacci" /><br>
  </a>
  <i>A high-performance Fibonacci sequence algorithm. *</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Prime-numbers:-breakdown">
    <img width="130" src="https://i.imgur.com/LFYekAD.png" alt="Prime numbers" /><br>
  </a>
  <i>A prime numbers algorithm. *</i>
  <br><br>
  <a href="https://github.com/iAmGio/pikt/wiki/Tree:-breakdown">
    <img width="130" src="https://i.imgur.com/aKg4I59.png" alt="Tree" /><br></a>
  <i>A tree that prints "A tree!". *</i>
</p>

_* Using [custom color schemes](https://github.com/iAmGio/pikt/blob/master/src/test/resources/schemes)._  
Click on the examples for a breakdown/explanation.

## Properties
The following properties must be inserted before Pikt jar's path in the command line.  
Example: `java -Dproperty=value -jar pikt.jar arguments_here`.  

- `-Dsource` source image file;
- `-Doutput` output name without extension. If not specified it will take `source`'s name without extension;
- `-Dcolors` path to the `.properties` color scheme without extension. Default values will be used if not specified;
- `-Dtargets` compilation targets divided by a comma. Can be `jvm`, `windows`, `osx` or `linux`. Note that Windows and OSX executables can be generated only on those platforms. No compilation will be executed if not specified;
- `-Dinterpret` defines whether the interpretation should run on the native or JVM compiler. Can be either `jvm` or `native` (native interpretation still has some issues). No interpretation will be executed if not specified.
- `-Djvmcompiler` path to the Kotlin/JVM (`kotlinc`) compiler. Required if `target` contains `jvm` or `interpret` is `jvm`;
- `-Dnativecompiler` path to the Kotlin/Native (`kotlinc`) compiler. Required if `target` contains a native target or `interpret` is `native`.

## Settings arguments

The following arguments enable settings that change Pikt's behavior.

- `-printoutput` prints the generated Kotlin code;
- `-nocompile` prevents the generation of any executable file;
- `-pixelinfo` adds information about pixel coordinates to the output code as comments;
- `-imgoutput=path` sets the output file for image-generating commands (see below). It must be inserted _before_ the other command. If not specified, defaults to the source image path followed by a suffix.

## Command arguments

The following arguments execute tasks and exit when completed.

- `-createscheme=name` creates a new [color scheme](https://github.com/iAmGio/pikt/blob/master/src/main/resources/properties/colors.properties) with default values;
- `-exportscheme=name` generates a useful color palette image out of the given scheme;
- `-recolorize[=method]` creates a copy of the source image (that relies on the default scheme) and adapts it to a custom scheme (specified by `-Dcolors`). `method` defines the way properties with more than one color are handled; it can be either `first` (default), `last` or `random`;
- `-standardize` creates a copy of the source image (that relies on a custom scheme) and adapts it to the default scheme;
- `-compact[=size]` creates a compacted copy of the source image. If `size` is not specified, it will try to create a square-ish image with no whitespaces. `size` can be defined via `w?h?`, where both `w`and `h` are optional (in case one is missing, it will be calculated the same way as before) (e.g. `w10h5`, `w10`, `h5`);
- `-decompact` creates a decompacted copy of the source image with a statement per line;
- `-standardecompact` runs `-standardize` + `-decompact`.

## Roadmap

**Code**
- [x] Variables
- [x] Method calls (both as part of expressions and standalone)
- [x] Method definitions
- [x] If / else / if else
- [x] Lambdas
- [x] Operators (equality, logical and arithmetic)
- [x] Loops
- [ ] [Standard library](https://github.com/iAmGio/pikt/tree/master/src/main/resources/pikt.stdlib) (2%)

**Compilation**
- [x] JVM
- [x] Native
- [x] Interpretation (currently only on JVM)
- [ ] Error handling

## Pikt Lab

I'm working on an IDE for Pikt called [Pikt Lab](https://github.com/iAmGio/pikt-lab), check it out!  
Meanwhile, here are some mockups:

![Lab-Landing](https://i.imgur.com/oylceCd.png)

![Lab-Workspace](https://i.imgur.com/wlj3gpg.png)