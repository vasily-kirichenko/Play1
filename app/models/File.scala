package models

case class File(sha1: Array[Byte])

object File {
  import play.api.libs.json._
  import utils.Implicits._

  implicit val fileWrites = new Writes[File] {
    def writes(file: File) = Json.obj(
      "sha1" -> file.sha1.toHex
    )
  }
}