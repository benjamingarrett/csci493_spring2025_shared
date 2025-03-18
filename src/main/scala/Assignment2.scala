import scala.util.Random

trait RandInt {
  def nextInt(n: Int): Int
}
case class UtilRandom(r: Random) extends RandInt {
  def nextInt(n: Int) = r.nextInt(n)
}

object Assignment2 extends App {

  // Problem 1: Sieve of Eratosthenes
  // Determine a lambda expression having the type specified below which returns a list
  // of prime numbers that are less than or equal to the value given as the first argument. 
  // This expression should only use the combinators we've studied so far, i.e. map, flatMap, 
  // reduce, etc. The type signature specified suggests a recursive solution.
  // TODO: replace the ??? with the correct expression
  val sieveEratosthenes: (List[Int],List[Int]) => List[Int] = ???
  // TODO: replace the ??? with the correct expression
  val primesUpToN: Int => List[Int] = ??? // this expression uses the previous one
  println(s"All primes less than 30")
  println("Expected: List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29)")
  println(s"Actual:   ${primesUpToN(30)}")

  // Problem 2: Approximate Pi
  // Determine a lambda expression having the type specified below which approximates pi
  // using a sequence of random integers. This expression should only use the combinators we've
  // studied to far, i.e. map, flatMap, reduce, etc. 
  // The first parameter forces us to start with integers. The second parameter specifies how many 
  // random values to use in order to approximate pi.
  // TODO: replace the ??? with the correct expression
  val pi: RandInt => Int => Double = ???
  val randInt = new UtilRandom(new Random())
  val result = pi(randInt)(50000)
  println("Expected: approximately 3.14")
  println(s"Actual:   $result")

  // Problem 3: Chain of functions design pattern
  // Here the simple task of computing the slope and y-intercept given two points is used 
  // to give us the pretext to handle partial functions using good functional design patterns. 
  // The following three custom error types represent the three ways our task can fail.
  trait MiscError
  object ItemNotFoundError extends MiscError
  object TypeConversionError extends MiscError
  object UndefinedSlopeError extends MiscError
  // these type aliases merely render the type signatures below a little easier to read
  // these coordinates represent (x1,y1) and (x2,y2)
  type StringCoords = Tuple4[String,String,String,String]
  type DoubleCoords = Tuple4[Double,Double,Double,Double]
  val m: Map[String,StringCoords] = Map(
    "slope one" -> ("0.0","0.0","1.0","1.0"),
    "slope zero" -> ("1.0","1.0","2.0","1.0"),
    "undefined slope" -> ("2.0","2.0","2.0","3.0"),
    "bad values" -> ("foo","bar","baz","biz")
  )
  // TODO: replace the ??? with the correct expression
  val findValues: (Map[String,StringCoords],String) => Either[MiscError,StringCoords] = ???

  // TODO: replace the ??? with the correct expression
  val convertValues: StringCoords => Either[MiscError,DoubleCoords] = ???

  // If the slope is defined for the given coordinates, this function returns
  // the slope and y-intercept
  // TODO: replace the ??? with the correct expression
  val slopeAndIntercept: DoubleCoords => Either[MiscError,(Double,Double)] = ???

  // Given the slope, m, the y-intercept, b, and a new x-value, this function determines the y-value
  // TODO: replace the ??? with the correct expression
  val extrapolated: (Double,Double) => Double => Double = ???

  // Determine a lambda expression having the specified type which retrieves the coordinates
  // from the Map above using the given key and then extrapolates a new predicted y-value.
  // This should be done using flatMap and map calls.
  // TODO: replace the ??? with the correct expression
  val predict: Map[String,StringCoords] => String => Double => Either[MiscError,Double] = ???

  // Determine a lambda expression having the specified type which does the same thing as 
  // what was just done, this time using a for-comprehension.
  // TODO: replace the ??? with the correct expression
  val predictFC: Map[String,StringCoords] => String => Double => Either[MiscError,Double] = ???

  val oneSlope: Either[MiscError,Double] = predict(m)("slope one")(4.0)
  val oneSlopeFC: Either[MiscError,Double] = predictFC(m)("slope one")(4.0)

  val zeroSlope: Either[MiscError,Double] = predict(m)("slope zero")(4.0)
  val zeroSlopeFC: Either[MiscError,Double] = predictFC(m)("slope zero")(4.0)

  val undefined: Either[MiscError,Double] = predict(m)("undefined slope")(4.0)
  val undefinedFC: Either[MiscError,Double] = predictFC(m)("undefined slope")(4.0)

  val badValue: Either[MiscError,Double] = predict(m)("bad values")(4.0)
  val badValueFC: Either[MiscError,Double] = predictFC(m)("bad values")(4.0)

  println("Expected: Right(5.0)")
  println(s"Actual:   $oneSlope")
  println(s"Actual:   $oneSlopeFC")

  println("Expected: Right(1.0)")
  println(s"Actual:   $zeroSlope")
  println(s"Actual:   $zeroSlopeFC")

  println("Expected: Left(Main$UndefinedSlopeError$@21f9277b)  (NB: memory references vary)")
  println(s"Actual:   $undefined")
  println(s"Actual:   $undefinedFC")

  println("Expected: Left(Main$TypeConversionError$@201aa8c1)  (NB: memory references vary)")
  println(s"Actual:   $badValue")
  println(s"Actual:   $badValueFC")


  // Problem 4: Corecursion using Streams
  // i. factorials: this expression computes a lazy list of all factorials, i.e. f_0 = 1, f_n = n * f_n-1
  // TODO: replace the ??? with the correct expression
  val factorials: Stream[Int] = ???

  // ii. Fibonacci numbers: this expression computes a lazy list of all Fibonacci numbers, i.e.
  // t_1 = 1, t_2 = 1, t_n = t_n-1 + t_n-2
  // TODO: replace the ??? with the correct expression
  val fibs: Stream[Int] = ???

  // iii. a third-order recurrence relation: t_1 = 2, t_2 = 3, t_3 = 5, t_n = 2 * t_n-1 + 7 * t_n-2 + 9 * t_n-3
  // this expression computes a lazy list of all values for this recurrence relation
  // TODO: replace the ??? with the correct expression
  val t: Stream[Int] = ???

  // iv. define using corecursion an infinite stream of positive integers starting with 2, i.e. [2,3,4,5,...]
  // TODO: replace the ??? with the correct expression
  val naturals: Stream[Int] = ???

  // v. define using corecursion a lazy list of all primes starting with 2
  // TODO: replace the ??? with the correct expression
  val primes: Stream[Int] = ???

  println("First ten factorials (expected): List(1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880)")
  println(s"First ten factorials (actual):   ${factorials.take(10).toList}")
  println("First ten Fibonacci numbers (expected): List(1, 1, 2, 3, 5, 8, 13, 21, 34, 55)")
  println(s"First ten Fibonacci numbers (actual):   ${fibs.take(10).toList}")
  println("First ten values of recurrence (expected): List(2, 3, 5, 49, 160, 708, 2977, 12350, 51911, 217065)")
  println(s"First ten values of recurrence t (actual): ${t.take(10).toList}")
  println("First 10 natural numbers starting with 2 (expected): List(2, 3, 4, 5, 6, 7, 8, 9, 10, 11)")
  println(s"First 10 natural numbers starting with 2 (actual):   ${naturals.take(10).toList}")
  println("First 20 primes (expected): List(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71)")
  println(s"First 20 primes (actual):   ${primes.take(20).toList}")
}
