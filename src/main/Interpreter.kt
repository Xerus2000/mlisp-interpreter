val availableFunctions = HashMap(defaultFunctions)

fun interpret(code: String): Value {
    @Suppress("NAME_SHADOWING")
    val code = code.trim()
    return if (code.startsWith('(')) {
        if (code.last() != ')')
            throw SyntaxError(code, "Missing closing parenthesis!")
        val expression = code.substring(1, code.lastIndex)
        if (code[1].isDigit() || code[1] == '-')
            interpretList(expression)
        else
            interpretFunction(expression)
    } else {
        parseLiteral(code)
    }
}

fun interpretList(expression: String): ListValue {
    return interpretRecursively(expression).value as ListValue
}

fun parseLiteral(expression: String): IntValue =
    IntValue(expression.toIntOrNull() ?: throw SyntaxError(expression, "Expression is not an Integer!"))

data class InterpretResult(val value: Value, val remainder: String)
fun interpretRecursively(expression: String): InterpretResult {
    val values = arrayListOf<Value>()
    var remainder = expression
    while (remainder.isNotEmpty()) {
        if (remainder[0] == '(') {
            val result = interpretRecursively(remainder.substring(1))
            remainder = result.remainder
            values.add(result.value)
        } else {
            val split = remainder.split(' ', limit = 2)
            val currentValue = split[0]
            remainder = split.getOrNull(1) ?: ""
            if (currentValue.last() == ')') {
                values.add(parseLiteral(currentValue.substring(0, currentValue.lastIndex)))
                break
            } else {
                values.add(parseLiteral(currentValue))
            }
        }
    }
    return InterpretResult(ListValue(values), remainder)
}

fun interpretFunction(code: String): Value {
    val split = code.split(' ', limit = 2)
    val function = availableFunctions[split[0]]
    val arguments = interpretList(split[1]).value.toTypedArray()
    return function?.invoke(arguments)
        ?: throw FunctionNotDefinedError(split[0])
}

