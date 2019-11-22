const val STRICT_MODE = true

val variables = HashMap<String, Value>()
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

fun parseLiteral(expression: String): Value =
	variables[expression] ?: IntValue(expression.toIntOrNull() ?: throw ParseError(expression))

data class InterpretResult(val value: Value, val remainder: String)

fun InterpretResult.unwrap() = value.asList().single()

fun interpretRecursively(code: String): InterpretResult {
	// Check whether this can be interpreted as Function
	val funSplit = code.split(' ', limit = 2)
	if(funSplit[0] == "define") {
		val defSplit = funSplit[1].split(' ', limit = 2)
		val result = interpretRecursively(defSplit[1])
		val variableValue = result.unwrap()
		variables[defSplit[0]] = variableValue
		return InterpretResult(variableValue, result.remainder)
	}
	val function = availableFunctions[funSplit[0]]
	if(function != null) {
		val result = interpretRecursively(funSplit[1])
		val arguments = result.value.asList().toTypedArray()
		return InterpretResult(function.invoke(arguments), result.remainder)
	}
	
	// Interpret as List
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
