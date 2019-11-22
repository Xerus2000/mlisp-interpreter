import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec

class InterpreterTest: StringSpec({
	"List" {
		interpret("(list -1 2 3)") shouldBe ListValue(-1, 2, 3)
	}
	"Summation" {
		interpret("(+ 1 2)").asInt() shouldBe 3
		interpret("(+ -22 3)") shouldBe IntValue(-19)
	}
	"Equals" {
		interpret("(= 1 1)").asBoolean() shouldBe true
		interpret("(= 1 2)").asBoolean() shouldBe false
	}
	"Standard methods" {
		interpret("(first (1 2 3))") shouldBe IntValue(1)
		interpret("(rest (6 2 5))") shouldBe ListValue(2, 5)
		interpret("(append (1 5) 2)") shouldBe ListValue(1, 5, 2)
	}
	"Method extension" {
		interpret("(append (1 4 6) 5 19)") shouldBe ListValue(1, 4, 6, 5, 19)
	}
	"Nested Lists" {
		interpret("(list (1))") shouldBe ListValue(ListValue(1))
		interpret("(list (1 5) (2 3) 8)") shouldBe ListValue(ListValue(1, 5), ListValue(2, 3), IntValue(8))
		interpret("(list (2 (3)) 8)") shouldBe ListValue(ListValue(IntValue(2), ListValue(3)), IntValue(8))
	}
	"Nested Function Calls" {
		interpret("(+ 1 (+ 3 -2))") shouldBe IntValue(2)
		interpret("(first (append ((1)) 2))") shouldBe ListValue(1)
		interpret("(append (first ((1) 2)) (list 3 4) 7)") shouldBe ListValue(IntValue(1), ListValue(3, 4), IntValue(7))
	}
	"Chained calls" {
		interpret("(first (1 2))(first (4))") shouldBe IntValue(4)
	}
	"Variable definition" {
		interpret("(define a 1)") shouldBe IntValue(1)
		interpret("(define a 1) (append (4) a)") shouldBe ListValue(4, 1)
		interpret("(define 1 3) (= 1 3)") shouldBe TrueValue
		interpret("(define 1 3) (define 3 1) (= 3 1)") shouldBe TrueValue
	}
	"Catches Syntax errors" {
		shouldThrow<ValidationException> { interpret("= 1 2") }
		shouldThrow<ValidationException> { interpret("(= 1 2") }
		shouldThrow<InvalidArgumentNumberError> { interpret("(= 1 2 3)") }
		shouldThrow<InvalidArgumentNumberError> { interpret("(first (1 2) 3)") }
	}
})
