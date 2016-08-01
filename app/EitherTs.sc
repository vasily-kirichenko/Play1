import java.nio.ByteBuffer

import scala.concurrent.{Await, ExecutionContext, Future}
import scalaz._
import concurrent.ExecutionContext.Implicits.global
import concurrent.duration._

//val f1: Future[String \/ Int] = Future { \/-(1) }
//  EitherT(f1).flatMap(x => {
//    val r: EitherT[Future, String, Int] = EitherT(Future(\/-(x + x)))
//    r
//  }).run
//
//Await.result(f1, 10 seconds)

val x: Throwable \/ Int = \/.fromTryCatchThrowable[Int, Throwable](1)

implicit def FutureApplicative(implicit executor: ExecutionContext) = new Applicative[Future] {
  def point[A](a: => A) = Future(a)
  def ap[A,B](fa: => Future[A])(f: => Future[A => B]) =
    (f zip fa) map { case (f1, a1) => f1(a1) }
}

val sha256 = EitherT.fromTryCatchThrowable[Future, Int, Throwable](Future { "4g".toInt })
sha256.run

