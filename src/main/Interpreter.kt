const val STRICT_MODE = true

val availableFunctions = HashMap(defaultFunctions)

fun interpret(code: String): Value {
    @Suppress("NAME_SHADOWING")
    validate(code)
    val result = interpretRecursively(code.trim())
        return result.value.asList().last()
}

fun validate(code: String) {
    if(STRICT_MODE) {
        if(!code.startsWith('(') && !code.endsWith(')'))
            throw ValidationException("Code is not surrounded by parenthesis")
        val opening = code.count { it == '(' }
        val closing = code.count { it == ')' }
        if(opening != closing)
            throw ValidationException("Amount of opening ($opening) and closing ($closing) parenthesis does not match")
    }
}

fun isLiteral(first: Char) = first.isDigit() || first == '-'

fun parseLiteral(expression: String): IntValue =
    IntValue(expression.toIntOrNull() ?: throw ParseError(expression, "Expression is not an Integer!"))

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
    loop@ while(remainder.isNotEmpty()) {
        if(remainder[0] == ')') {
            remainder = remainder.substring(1)
            break
        }
        when {
            remainder[0] == '(' -> {
                val expression = remainder.substring(1)
                val result = interpretRecursively(expression)
                remainder = result.remainder.trim()
                values.add(result.value)
            }
            else -> {
                val split = remainder.split(' ', ')', limit = 2)
                val currentValue = split[0]
                remainder = split[1]
                values.add(parseLiteral(currentValue))
                if(remainder.isEmpty() || remainder[0] in arrayOf(')', ' '))
                    break@loop
            }
        }
    }
    return InterpretResult(ListValue(values), remainder)
}
