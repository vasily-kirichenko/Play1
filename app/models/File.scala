package models

case class File(sha1: Array[Byte])

object File {
  import play.api.libs.json.{Json, Writes}
  import utils.Hex

  implicit val fileWrites = new Writes[File] {
    def writes(file: File) = Json.obj(
      "sha1" -> Hex.arrayToHexString(file.sha1)
    )
  }
}