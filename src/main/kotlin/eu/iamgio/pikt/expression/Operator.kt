package eu.iamgio.pikt.expression

import eu.iamgio.pikt.image.Pixel
import eu.iamgio.pikt.properties.OperatorColorsProperties

/**
 * Represents arithmetic (+ - * /), logical (&& ||) and equality (== != > >= < <=) operators
 *
 * @author Giorgio Garofalo
 */
enum class Operator(override val code: String, val hex: (OperatorColorsProperties) -> String): ExpressionMember {
    PLUS("+",               { colors -> colors.plus            }),
    MINUS("-",              { colors -> colors.minus           }),
    TIMES("*",              { colors -> colors.times           }),
    DIVIDE("/",             { colors -> colors.divide          }),
    MODULO("%",             { colors -> colors.modulo          }),
    AND("&&",               { colors -> colors.and             }),
    OR("||",                { colors -> colors.or              }),
    EQUALITY("==",          { colors -> colors.equality        }),
    INEQUALITY("!=",        { colors -> colors.inequality      }),
    GREATER(">",            { colors -> colors.greater         }),
    GREATER_OR_EQUALS(">=", { colors -> colors.greaterOrEquals }),
    LESS("<",               { colors -> colors.less            }),
    LESS_OR_EQUALS("<=",    { colors -> colors.lessOrEquals    });

    companion object {

        /**
         * Gets an operator by looking for one matching [pixel] color.
         * @param pixel pixel to compare
         * @return matched operator if exists. <tt>null</tt> otherwise
         */
        fun byPixel(pixel: Pixel): Operator? = values().firstOrNull {
            pixel.matches(it.hex(pixel.colors.operators))
        }
    }
}