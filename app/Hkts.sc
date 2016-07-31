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