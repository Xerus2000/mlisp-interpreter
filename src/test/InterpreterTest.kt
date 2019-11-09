import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class InterpreterTest : StringSpec({
    "List" {
        val list = ListValue(-1, 2, 3)
        val listFunction = interpret("(list -1 2 3)")
        list shouldBe listFunction
    }
    "Summation" {
        3 shouldBe interpret("(+ 1 2)").asInt()
        IntValue(-19) shouldBe interpret("(+ -22 3)")
    }
    "Equals" {
        true shouldBe interpret("(= 1 1)").asBoolean()
        false shouldBe interpret("(= 1 2)").asBoolean()
    }
    "Standard methods" {
        IntValue(1) shouldBe interpret("(first (1 2 3))")
        ListValue(2, 5) shouldBe interpret("(rest (6 2 5))")
        ListValue(1, 5, 2) shouldBe interpret("(append (1 5) 2)")
    }
    "Method extension" {
        ListValue(1, 4, 6, 5, 19) shouldBe interpret("(append (1 4 6) 5 19)")
    }
    "Multi-Dimensional Lists" {
        ListValue(ListValue(1)) shouldBe interpret("(list (1))")
        ListValue(ListValue(1, 5), ListValue(2, 3), IntValue(8)) shouldBe interpret("(list (1 5) (2 3) 8)")
    }
    "Catches Syntax errors" {
        shouldThrow<SyntaxError> { interpret("= 1 2") }
        shouldThrow<SyntaxError> { interpret("(= 1 2") }
        shouldThrow<InvalidArgumentNumberError> { interpret("(= 1 2 3)") }
        shouldThrow<InvalidArgumentNumberError> { interpret("first (1 2) 3") }
    }
})
