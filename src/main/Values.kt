sealed class Value {
	open fun asInt(): Int =
		throw WrongParameterTypeError(IntValue::class, this)
	
	open fun asList(): List<Value> =
		throw WrongParameterTypeError(ListValue::class, this)
	
	abstract fun asBoolean(): Boolean
}

data class IntValue(val value: Int): Value() {
	override fun asInt() = value
	override fun asBoolean() = true
}

data class ListValue(val value: List<Value>): Value() {
	constructor(vararg values: Value): this(values.toList())
	constructor(vararg values: Int): this(values.map { IntValue(it) })
	
	override fun asList() = value
	override fun asBoolean() = value.isNotEmpty()
}

object TrueValue: Value() {
	override fun asBoolean() = true
}

val FalseValue = ListValue(listOf())
