@file:Suppress("ArrayInDataClass")

import kotlin.reflect.KClass

sealed class CompilationException(message: String) : Exception(message)

data class SyntaxError(val code: String, val msg: String) :
    CompilationException("'$code' is not valid: $msg")

data class FunctionNotDefinedError(val name: String) :
    CompilationException("Function not defined: $name")

data class WrongParameterTypeError(val expectedType: KClass<out Value>, val actual: Value) :
    CompilationException("Expected ${expectedType.java.simpleName} but got $actual")

data class InvalidArgumentNumberError(val arguments: Array<*>, val expected: Int) :
    CompilationException("Expected $expected arguments but got ${arguments.joinToString(" ", "(", ")")}")

