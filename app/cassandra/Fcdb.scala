package cassandra

import java.nio._
import com.datastax.driver.core.SocketOptions
import com.websudos.phantom.connectors.ContactPoints
import utils._
import utils.Implicits._
import concurrent._
import scalaz._
//import concurrent.ExecutionContext.Implicits.global
import com.websudos.phantom.dsl._

trait CassandraConfig {
  val hosts: List[String]
  val keySpace: String
}

trait Fcdb extends CassandraConfig {
  sealed class Files extends CassandraTable[Files, File] {
    object sha256 extends BlobColumn(this) with PartitionKey[ByteBuffer]
    object md5 extends BlobColumn(this)
    object sha1 extends BlobColumn(this)
    object zone extends SmallIntColumn(this)
    override def fromRow(r: Row): File = File(sha256(r), md5(r), sha1(r), zone(r))
  }

  abstract class ConcreteFiles extends Files with RootConnector {
    def getFileBySha256(sha256: ByteBuffer): EitherT[Future, Throwable, Option[File]] =
      EitherT.fromTryCatchThrowable(select.where(_.sha256 eqs sha256).one)
  }

  case class File(sha256: ByteBuffer, md5: ByteBuffer, sha1: ByteBuffer, zone: Short) {
    override def toString: String = {
      def buffToStr(buff: ByteBuffer) = Hex.arrayToHexString(buff.array)
      s"""sha256 = ${buffToStr(sha256)}, sha1 = ${buffToStr(sha1)}, md5 = ${buffToStr(md5)}, zone = $zone"""
    }
  }

  lazy val connector = ContactPoints(hosts, 9042)
    .withClusterBuilder(_.withSocketOptions(new SocketOptions().setConnectTimeoutMillis(10000)))
    .noHeartbeat
    .keySpace(keySpace)

  object Db extends Database(connector) {
    object file extends ConcreteFiles with connector.Connector
  }

  //val sha256 = ByteBuffer.wrap(Hex.hexStringToByteArray("bff6fe8af3bd9412cdc2e8d2d2043add134fe133a6d8dcc22f8028b144656e8c"))
  //
  //val res =
  //Await.result(
  //KlSrlDb.file.getFileBySha256(sha256),
  //5 seconds)
  //
  //res match {
  //  case Some(file) => s"found: ${file.toString}"
  //  case None => "file was not found"
  //}
}

trait ApplicationCassandraConfig extends CassandraConfig {
  import com.typesafe.config.ConfigFactory
  import collection.JavaConversions._

  private val config = ConfigFactory.load()

  val hosts: List[String] = config.getStringList("cassandra.hosts").toList
  val keySpace: String = config.getString("cassandra.keyspace")
}