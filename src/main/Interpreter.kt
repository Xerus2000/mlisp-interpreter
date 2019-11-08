data class Function(val arguments: Int? = null, val function: (inputValues: Array<Value>) -> Value) {
    operator fun invoke(inputValues: Array<Value>): Value {
        if (arguments != null && inputValues.size != arguments)
            throw InvalidArgumentNumberError(inputValues, arguments)
        return function(inputValues)
    }
}

val availableFunctions = mapOf<String, Function>(
    "+" to Function(2) { Value.IntValue(it[0].asInt() + it[1].asInt()) },
    "=" to Function(2) { if (it[0].asInt() == it[1].asInt()) Value.TrueValue else Value.ListValue(listOf()) },
    "list" to Function { Value.ListValue(it.toList()) }
)

fun interpret(code: String): Value {
    @Suppress("NAME_SHADOWING")
    val code = code.trim()
    return if (code.startsWith('(')) {
        if (code.last() != ')')
            throw SyntaxError(code, "Missing closing parenthesis!")
        val expression = code.substring(1, code.lastIndex)
        if (code[1].isDigit())
            Value.ListValue(expression.split(' ').map { interpret(it) })
        else
            interpretFunction(expression)
    } else {
        Value.IntValue(code.toIntOrNull() ?: throw SyntaxError(code, "Expression is not an Integer!"))
    }
}

fun interpretFunction(code: String): Value {
    val split = code.split(' ', limit = 2)
    return availableFunctions[split[0]]?.invoke(split[1].split(' ').map { interpret(it) }.toTypedArray())
        ?: throw FunctionNotDefinedError(split[0])
}

