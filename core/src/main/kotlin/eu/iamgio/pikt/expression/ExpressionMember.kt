package eu.iamgio.pikt.expression

/**
 * Interface implemented by [Expression]s and [Operator]s. Represents parts of complex expressions.
 */
interface ExpressionMember {

    /**
     * Transpiles this expression into output code.
     * @param transpiler expression to string converter
     * @return the output code
     */
    fun toCode(transpiler: ExpressionTranspiler): String
}
