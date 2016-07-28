trait Connection

trait Database {
  def connect() : Connection
  def execute[R](sql: String)(implicit conn: Connection): Seq[R]
}

trait Log {
  protected def info(msg: String) : Unit
}

case class Product(id: Int, name: String)

trait Storage { self: Database with Log =>
  def getProduct(id: Int) : Product = {
    implicit val conn = connect()
    val product =
      execute(s"select id, name from dbo.Procuct where id = $id")
        .headOption
        .getOrElse(Product(0, "default"))
    info(s"Returning $product")
    product
  }
}

trait FakeDatabase extends Database with Log {
  def connect(): Connection = new Connection {}
  def execute[R](sql: String)(implicit conn: Connection): Seq[R] = {
    info(s"Executing $sql...")
    Seq(Product(1, "foo").asInstanceOf[R])
  }
}

trait ConsoleLogger extends Log {
  def info(msg: String): Unit = println(s"[I] $msg")
}

val app = new Storage with FakeDatabase with ConsoleLogger
val product = app.getProduct(2)
product