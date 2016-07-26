package controllers

import play.api.mvc._

class HashStore extends Controller {
  def getFiles(hash: Long) = Action {
    Ok(s"{ hash: $hash, files: [] }")
  }
}
