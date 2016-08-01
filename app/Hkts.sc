import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

trait Monoid[T] {
  val zero: T
  def append(x: T, y: T): T
}

implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
  val zero: Int = 0
  def append(x: Int, y: Int): Int = x + y
}

implicit def seqMonoid[T]: Monoid[Seq[T]] = new Monoid[Seq[T]] {
  val zero: Seq[T] = Seq.empty
  def append(x: Seq[T], y: Seq[T]): Seq[T] = x ++ y
}

def f[T: Monoid](x: T, y: T) = (implicitly[Monoid[T]]).append(x, y)

f(1, 2)
f(Seq(1, 2), Seq(3, 4))
f(List(1, 2), Seq(3, 4))

class AsyncTry[A](val at: Future[Try[A]]) {
  def map[B](f: A => B): AsyncTry[B] = {
    new AsyncTry(at map (_ map f))
  }

  def flatMap[B](f: A => Future[Try[B]]): AsyncTry[B] = {
    new AsyncTry(
      at flatMap {
        case Success(x) => f(x)
        case Failure(e) => Future { Failure(e) }
    })
  }

//  def flatMap[B](f: A => Try[B]): AsyncTry[B] = {
//    new AsyncTry(
//      at flatMap {
//        case Success(x) => Future { f(x) }
//        case Failure(e) => Future { Failure(e) }
//    })
//  }
}

object AsyncTry {
  def apply[A](x: => A): AsyncTry[A] = new AsyncTry(Future(Try(x)))
  def apply[A](x: Try[A]): AsyncTry[A] = new AsyncTry(Future(x))
  def apply[A](x: Future[Try[A]]): AsyncTry[A] = new AsyncTry(x)
}

import concurrent.ExecutionContext.Implicits.global

val f = AsyncTry(1).flatMap((x: Int) => Future { Try { x * x }}).at
Await.result(f, 5 seconds)