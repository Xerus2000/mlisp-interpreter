sealed class Value {
    open fun asInt(): Int =
        throw WrongParameterTypeError(IntValue::class, this)

    open fun asList(): List<Value> =
        throw WrongParameterTypeError(ListValue::class, this)

    data class IntValue(val value: Int) : Value() {
        override fun asInt() = value
    }

    data class ListValue(val value: List<Value>) : Value() {
        override fun asList() = value
    }

    object TrueValue : Value()
}
