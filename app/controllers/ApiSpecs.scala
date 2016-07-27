package controllers

import com.iheart.playSwagger.SwaggerSpecGenerator
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._

import scala.concurrent.Future

class ApiSpecs extends Controller {
  implicit val cl = getClass.getClassLoader
  private lazy val generator = SwaggerSpecGenerator("controllers")

  def specs = Action.async { _ =>
    Future.fromTry(generator.generate()).map(Ok(_))
  }
}