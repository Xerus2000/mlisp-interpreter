import kotlin.math.exp

val availableFunctions = HashMap(defaultFunctions)

fun interpret(code: String): Value {
    @Suppress("NAME_SHADOWING")
    val code = code.trim()
    return interpretRecursively(code).value
}

fun isLiteral(first: Char) = first.isDigit() || first == '-'

fun parseLiteral(expression: String): IntValue =
    IntValue(expression.toIntOrNull() ?: throw SyntaxError(expression, "Expression is not an Integer!"))

data class InterpretResult(val value: Value, val remainder: String)

fun interpretRecursively(code: String): InterpretResult {
    val values = arrayListOf<Value>()
    var remainder = code
    while (remainder.isNotEmpty()) {
        if (remainder[0] == '(') {
            val expression = remainder.substring(1)
            if (isLiteral(expression[0]) || expression[0] == '(') {
                val result = interpretRecursively(expression)
                remainder = result.remainder
                values.add(result.value)
            } else {
                val split = expression.split(' ', limit = 2)
                val function = availableFunctions[split[0]] ?: throw FunctionNotDefinedError(split[0])
                val result = interpretRecursively(split[1])
                remainder = result.remainder
                val arguments = result.value.asList().toTypedArray()
                values.add(function.invoke(arguments))
            }
        } else {
            val split = remainder.split(' ', limit = 2)
            val currentValue = split[0]
            remainder = split.getOrNull(1) ?: ""
            if (currentValue.contains(')')) {
                values.add(parseLiteral(currentValue.substringBefore(')')))
                break
            } else {
                values.add(parseLiteral(currentValue))
            }
        }
    }
    val isFunction = code[0] == '(' && !isLiteral(code[1]) && code[1] != '('
    return InterpretResult(if(isFunction) values[0] else ListValue(values), remainder)
}
