import scalaz._
import Scalaz._

None | 1.some | 2

-\/("foo") | \/-("bar")

"foo".left | "bar".right

1.some |+| 2.some
List(1, 2) |+| List(3, 4)

(1 > 10) ? 2 | 3