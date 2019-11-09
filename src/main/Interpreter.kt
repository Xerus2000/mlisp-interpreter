val availableFunctions = HashMap(defaultFunctions)

fun interpret(code: String): Value {
    @Suppress("NAME_SHADOWING")
    val code = code.trim()
    return interpretRecursively(code.substringAfter('(')).value
}

fun isLiteral(first: Char) = first.isDigit() || first == '-'

fun parseLiteral(expression: String): IntValue =
    IntValue(expression.toIntOrNull() ?: throw SyntaxError(expression, "Expression is not an Integer!"))

data class InterpretResult(val value: Value, val remainder: String)

fun interpretRecursively(code: String): InterpretResult {
    if(!isLiteral(code[0]) && code[0] != '(') {
        val split = code.split(' ', limit = 2)
        val function = availableFunctions[split[0]] ?: throw FunctionNotDefinedError(split[0])
        val result = interpretRecursively(split[1])
        val arguments = result.value.asList().toTypedArray()
        return InterpretResult(function.invoke(arguments), result.remainder)
    }
    val values = arrayListOf<Value>()
    var remainder = code
    loop@ while (remainder.isNotEmpty()) {
        when {
            remainder[0] == '(' -> {
                val expression = remainder.substring(1)
                val result = interpretRecursively(expression)
                remainder = result.remainder
                values.add(result.value)
            }
            else -> {
                val split = remainder.split(' ', limit = 2)
                val currentValue = split[0]
                remainder = split.getOrNull(1) ?: ""
                if (currentValue.contains(')')) {
                    values.add(parseLiteral(currentValue.substringBefore(')')))
                    break@loop
                } else {
                    values.add(parseLiteral(currentValue))
                }
            }
        }
    }
    return InterpretResult(ListValue(values), remainder)
}
