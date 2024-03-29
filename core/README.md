# pikt/core

This module contains the key features of Pikt, including code generation, interpretation and compilation.

### Packages
- `command`: command-line arguments that trigger certain actions or settings;
- `compiler`: bridge to the Kotlin compilers to allow compilation and interpretation;
- `eval`: code generation;
- `exit`: process closure handling;
- `expression`: expression parsing;
- `image`: image parsing and transformation;
- `lib`: external libraries management, including stdlib;
- `log`: logging utilities;
- `properties`: system properties and color scheme parsing;
- `schemes`: color-based processing and operations;
- `statements`: statements (such as `if`, `variable.set`, etc.) and their behaviors;
- `targetlang`: access to specific tools for each target language available for transpilation (currently Kotlin only); 
- `util`: general utilities.