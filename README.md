# Pikt
Pikt is a work-in-progress image, pixel-based esoteric programming language that aims at generating fast and lightweight programs from aesthetically pleasant image sources: to accomplish this, the colors of all "keywords", functions, operators and so on are easily customizable.
  
Pikt compiles executables via the Kotlin compiler, therefore compilation for both JVM and native targets is planned.

## Arguments

- `-createcolors=name` creates a new color scheme;
- `-printoutput` prints the generated Kotlin code;
- `-nocompile` generates the code only and does not actually create an output, mostly for development purposes;

## Roadmap

**Code**
- [x] Variables
- [x] Method calls (both as part of expressions and standalone)
- [ ] Method definitions
- [x] If / else / if else
- [x] Lambdas
- [x] Operators (equality, logical and arithmetic)
- [ ] Classes?
- [ ] Standard library

**Compilation**
- [x] JVM
- [ ] Native
- [ ] Interpretation