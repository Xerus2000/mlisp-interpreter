@file:Suppress("ArrayInDataClass")

import kotlin.reflect.KClass

sealed class CompilationException(message: String): Exception(message)

data class ValidationException(val msg: String):
	CompilationException(msg)

data class ParseError(val code: String):
	CompilationException("Unable to parse '$code'")

data class FunctionNotDefinedError(val name: String):
	CompilationException("Function not defined: $name")

data class WrongParameterTypeError(val expectedType: KClass<out Value>, val actual: Value):
	CompilationException("Expected ${expectedType.java.simpleName} but got $actual")

data class InvalidArgumentNumberError(val arguments: Array<*>, val expected: Int):
	CompilationException("Expected $expected arguments but got ${arguments.joinToString(" ", "(", ")")}")

