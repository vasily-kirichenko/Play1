import java.nio._

import com.datastax.driver.core.SocketOptions
import com.websudos.phantom.connectors.ContactPoints
import com.websudos.phantom.dsl._
import scala.concurrent._
import scala.concurrent.duration._

object Hex {
  def arrayToHexString(buf: Array[Byte]): String = buf.map("%02X" format _).mkString

  def hexStringToByteArray(s : String) = {
    val len = s.length()
    val buff = new Array[Byte](len / 2)

    for (i <- 0 until len by 2) {
      val b = (Character.digit(s charAt i, 16) << 4) + Character.digit(s charAt (i + 1), 16)
      buff(i / 2) = b.asInstanceOf[Byte]
    }

    buff
  }
}
// Seq("10.70.16.76", "10.70.16.77")

val connector = ContactPoints(Seq("localhost"), 9042)
  .withClusterBuilder(
    _.withSocketOptions(new SocketOptions().setConnectTimeoutMillis(10000)))
  .noHeartbeat
  .keySpace("klsrl")

class KlSrlDb(override val connector: KeySpaceDef) extends Database(connector) {
  object file extends ConcreteFiles with connector.Connector
}

case class File(sha256: ByteBuffer, md5: ByteBuffer, sha1: ByteBuffer, zone: Short) {
  override def toString: String = {
    def buffToStr(buff: ByteBuffer) = Hex.arrayToHexString(buff.array)
    s"""sha256 = ${buffToStr(sha256)}, sha1 = ${buffToStr(sha1)}, md5 = ${buffToStr(md5)}, zone = $zone"""
  }
}

sealed class Files extends CassandraTable[Files, File] {
  object sha256 extends BlobColumn(this) with PartitionKey[ByteBuffer]
  object md5 extends BlobColumn(this)
  object sha1 extends BlobColumn(this)
  object zone extends SmallIntColumn(this)

  override def fromRow(r: Row): File = File(sha256(r), md5(r), sha1(r), zone(r))
}

abstract class ConcreteFiles extends Files with RootConnector {
  def getFileBySha256(sha256: ByteBuffer): Future[Option[File]] =
    select.where(_.sha256 eqs sha256).one

  def getFirstFile: Future[Option[File]] = select.one
}

object KlSrlDb extends KlSrlDb(connector)

val sha256 = ByteBuffer.wrap(Hex.hexStringToByteArray("bff6fe8af3bd9412cdc2e8d2d2043add134fe133a6d8dcc22f8028b144656e8c"))

val res =
  Await.result(
    KlSrlDb.file.getFileBySha256(sha256),
    5 seconds)

res match {
  case Some(file) => s"found: ${file.toString}"
  case None => "file was not found"
}