package tokens.operators.binary_operators.comparison_operators

object GreaterThanOrEqualOperator : ComparisonOperator() {
    override val matchedPattern = Regex(">=")
}