import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

fun testInterpreter() {
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
    val list = listOf(1, 2, 3)
    val listLiteral = interpret("(1 2 3)")
    val listFunction = interpret("(list 1 2 3)")
    assertEquals(list, listLiteral.asList().map { it.asInt() })
    assertEquals(listLiteral, listFunction)
    // Interprets Equals
    assertEquals(Value.TrueValue, interpret("(= 1 1)"))
    assertEquals(0, interpret("(= 1 2)").asList().size)
}

fun main() {
    testInterpreter()
}
