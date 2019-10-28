import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

data class Function(val arguments: Int, val function: (inputValues: Array<Value>) -> Value) {
   operator fun invoke(inputValues: Array<Value>): Value {
       if (inputValues.size != arguments)
           throw InvalidArgumentNumberError(inputValues, arguments)
       return function(inputValues)
   }
}

val availableFunctions = mapOf<String, Function>(
    "+" to Function(2) { Value.IntValue(it[0].asInt() + it[1].asInt()) },
    "=" to Function(2) { if (it[0].asInt() == it[1].asInt()) Value.TrueValue else Value.ListValue(listOf()) }
)

fun main() {
    // Interprets Int with padding
    assertEquals(Value.IntValue(3), interpret(" 3 "))
    // Catches Syntax errors
    assertFailsWith<SyntaxError> { interpret("= 1 2") }
    assertFailsWith<SyntaxError> { interpret("(= 1 2") }
    assertFailsWith<InvalidArgumentNumberError> { interpret("(= 1 2 3)") }
    // Interprets summation
    val sum = interpret("(+ 1 2)")
    assertEquals(3, sum.asInt())
    // Interprets List
    val list = interpret("(1 2 3)")
    assertEquals(listOf(1, 2, 3), list.asList().map { it.asInt() })
    // Interprets Equals
    assertEquals(Value.TrueValue, interpret("(= 1 1)"))
    assertEquals(0, interpret("(= 1 2)").asList().size)
}

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

sealed class CompileError(message: String) : Exception(message)

data class SyntaxError(val code: String, val msg: String) :
    CompileError("'$code' is not valid: $msg")

data class FunctionNotDefinedError(val name: String) :
    CompileError("Function not defined: $name")

data class WrongParameterTypeError(val expectedType: KClass<out Value>, val actual: Value) :
    CompileError("Expected ${expectedType.simpleName} but got $actual")

data class InvalidArgumentNumberError(val arguments: Array<*>, val expected: Int) :
    CompileError("Expected $expected arguments but got ${arguments.joinToString(" ", "(", ")")}")

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

