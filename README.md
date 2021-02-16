# Pikt
Pikt is a work-in-progress image, pixel-based esoteric programming language that aims at generating fast and lightweight programs from aesthetically pleasant image sources: to accomplish this, the colors of all "keywords", functions, operators and so on are easily customizable.
  
Pikt compiles executables via the [Kotlin compiler](https://kotlinlang.org/docs/command-line.html), therefore compilation for both JVM and native targets is planned.

## Properties
The following properties must be inserted before Pikt jar's path in the command line.  
Example: `java -Dproperty=value -jar pikt.jar arguments_here`.  

- `-Dsource` source image file;
- `-Doutput` output name without extension. If not specified it will take `source`'s name without extension;
- `-Dcolors` path to the `.properties` color scheme without extension. Default values will be used if not specified;
- `-Dtarget` compilation targets divided by a comma. Can be `jvm`, `windows`, `osx` or `linux`. Note that Windows and OSX executables can be generated only on those platforms;
- `-Djvmcompiler` path to the Kotlin/JVM (`kotlinc`) compiler. Required if `target` contains `jvm`;
- `-Dnativecompiler` path to the Kotlin/Native (`kotlinc`) compiler. Required if `target` contains a native target;

## Arguments

- `-createcolors=name` creates a new color scheme with default values and exits;
- `-printoutput` prints the generated Kotlin code;
- `-nocompile` generates the code only and does not actually create an output, mostly for development purposes;

## Roadmap

**Code**
- [x] Variables
- [x] Method calls (both as part of expressions and standalone)
- [x] Method definitions
- [x] If / else / if else
- [x] Lambdas
- [x] Operators (equality, logical and arithmetic)
- [ ] Classes?
- [ ] Standard library

**Compilation**
- [x] JVM
- [x] Native
- [ ] Interpretation