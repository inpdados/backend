package controllers

import domain.associates._
import domain.associates.json._
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.InjectedController
import xingu.commons.play.controllers.XinguController
import xingu.commons.play.services.Services

class AssociatesController @Inject() (
  services   : Services,
  associates :  Associates) extends InjectedController with XinguController
{

  implicit val ec = services.ec()

  def register() = Action.async(parse.json) { implicit r =>
    validateThen[RegisterAssociateRequest] { req =>
      associates.register(req) map {
        case Left(e)                  => InternalServerError(e.getMessage)
        case Right(result: Associate) => Ok(Json.toJson(result))
      }
    }
  }
}
