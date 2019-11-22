data class Function(val arguments: Int? = null, val function: (inputValues: Array<Value>) -> Value) {
	operator fun invoke(inputValues: Array<Value>): Value {
		if(arguments != null && inputValues.size != arguments)
			throw InvalidArgumentNumberError(inputValues, arguments)
		return function(inputValues)
	}
}

val defaultFunctions = mapOf<String, Function>(
	"+" to Function(2) { IntValue(it[0].asInt() + it[1].asInt()) },
	"=" to Function(2) { if(it[0].asInt() == it[1].asInt()) TrueValue else ListValue(listOf()) },
	"list" to Function { ListValue(it.toList()) },
	"first" to Function(1) { it[0].asList().first() },
	"rest" to Function(1) { ListValue(it[0].asList().run { subList(1, size) }) },
	"append" to Function { ListValue(it[0].asList().plus(it.slice(1..it.lastIndex))) },
	"print" to Function { println(it.joinToString(" ")); FalseValue }
)
