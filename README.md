# Pikt
Pikt is a work-in-progress image, pixel-based esoteric programming language that aims at generating fast and lightweight programs from aesthetically pleasant image sources: to accomplish this, the colors of all "keywords", functions, operators and so on are easily customizable.
  
Pikt compiles executables via the [Kotlin compiler](https://kotlinlang.org/docs/command-line.html), therefore compilation for both JVM and native targets is available, along with runtime interpretation.

<p align="center">
  <br>
  <img width="130" src="https://i.imgur.com/1KFhhic.png" alt="Fibonacci" /><br>
  <i>A high-performance Fibonacci sequence algorithm*.</i>
</p>

_* Using [custom color schemes](https://github.com/iAmGio/pikt/blob/master/src/test/resources/schemes)._

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

## Arguments

- `-createcolors=name` creates a new [color scheme](https://github.com/iAmGio/pikt/blob/master/src/main/resources/properties/colors.properties) with default values and exits;
- `-printoutput` prints the generated Kotlin code;
- `-nocompile` prevents the generation of any executable file.

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