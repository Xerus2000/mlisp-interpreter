# Mini LISP
​​
## Expression based
​
```lisp
(+ 4 5) # evaluates to -> 9
```
​​
## Predefined functions
​
+, =, print, list, first, rest, append
​
```lisp
(+ 1 2) # -> 9
(= 1 1) # -> true
(= 1 2) # -> () which is an empty list and equivalent to false
```
​
```lisp
(print 1) # outputs  1
```
​
```lisp
(list 1 2 3) # -> (1 2 3)
```
​​
```lisp
(first (1 2 3)) # -> 1
(rest (1 2 3)) # -> (2 3)
(append (1 2 3) 4) # -> (1 2 3 4)
```
​​
## define variables
​
```lisp
(define a 1)  # a is bound to 1
(define b (+ 3 4)) # b is bound to 7
```​
## define functions
​
```lisp
(define twice lambda (a) (+ a a))  # twice is a function that calculates a + a
(define sum lambda (a b) (+ a b)) # b is bound to 7
```
​​
## Conditionals
​
```lisp
(define a 5)
(if (= a 7) (twice a) (twice 2)) # evaluates to 4 (twice 2)
(if (= a 5) (twice a) (twice 2)) # evaluates to 10 (twice a)
```