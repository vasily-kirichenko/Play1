package controllers

import java.nio.ByteBuffer

import cassandra._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import utils.Implicits._

import scala.concurrent.Future
import scalaz.Scalaz._
import scalaz._

object Db extends Fcdb with ApplicationCassandraConfig

class HashStore extends Controller {
  def getFiles(sha256: String) = Action.async {
    import models.File._

    val buff = EitherT.fromTryCatchThrowable[Future, ByteBuffer, Throwable](Future { sha256 : ByteBuffer })
    buff
      .flatMap { sha256 => Db.Instance.file.getFileBySha256(sha256) }
      .run
      .map {
        case \/-(Some(file)) => Ok(Json toJson models.File(file.sha256.array))
        case \/-(None) => NotFound
        case -\/(exn) => InternalServerError
      }
  }
}
