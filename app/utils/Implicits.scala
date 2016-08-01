package utils

import java.nio.ByteBuffer
import scala.concurrent.{ExecutionContext, Future}
import scalaz.Applicative
import language.implicitConversions

object Implicits {
  implicit def hexStringToByteArray(s: String): Array[Byte] = Hex.hexStringToByteArray(s)
  implicit def hexStringToByteBuffer(s: String): ByteBuffer = ByteBuffer.wrap(s)

  implicit def FutureApplicative(implicit executor: ExecutionContext) = new Applicative[Future] {
    def point[A](a: => A) = Future(a)
    def ap[A, B](fa: => Future[A])(f: => Future[A => B]) =
      (f zip fa) map { case (f1, a1) => f1(a1) }
  }
}