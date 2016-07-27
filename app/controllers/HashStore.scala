package controllers

import models.File
import models.File._
import play.api.libs.json._
import play.api.mvc._
import utils.Implicits._

class HashStore extends Controller {
  def getFiles(hash: Long) = Action {
    val f1 = File("000102030405060708090A0B0C0D0E0F10111213")
    val f2 = File("0102030405060708090A0B0C0D0E0F1011121314")
    val f3 = File("02030405060708090A0B0C0D0E0F101112131415")

    Ok(Json.toJson(Array(f1, f2, f3)))
  }
}
