package controllers

import domain.comments._
import domain.comments.json._
import javax.inject.Inject
import play.api.libs.json.Json
import play.api.mvc.InjectedController
import xingu.commons.play.controllers.XinguController
import xingu.commons.play.services.Services

class CommentsController @Inject()(
  services : Services,
  comments :  Comments) extends InjectedController with XinguController {

  implicit val ec = services.ec()

  def all() = Action.async {
    comments.all() map { it =>
      Ok(Json.toJson(it))
    }
  }

  def append() = Action.async(parse.json) { implicit r =>
    validateThen[AppendCommentRequest] { req =>
      comments.append(req) map {
        case Left(e)                => InternalServerError(e.getMessage)
        case Right(result: Comment) => Ok(Json.toJson(result))
      }
    }
  }
}
