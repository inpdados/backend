package domain.comments

import java.sql.Timestamp
import java.util.Date

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.libs.json.Reads.dateReads
import play.api.libs.json.Writes.dateWrites
import slick.jdbc.PostgresProfile.api._
import xingu.commons.play.services.Services

import scala.concurrent.Future


case class AppendCommentRequest(
  target   : String,
  text     : String,
  name     : Option[String],
  email    : Option[String]
)

case class Comment(
  id       : Long,
  at       : Date,
  target   : String,
  text     : String,
  likes    : Int,
  dislikes : Int,
  name     : Option[String],
  email    : Option[String]
)

object json {
  val format = "yyyyMMdd'T'HHmmss"
  implicit val CustomDateWrites = dateWrites(format)
  implicit val CustomDateReads  = dateReads(format)
  implicit val CommentWriter    = Json.writes[Comment]
  implicit val AppendCommentRequestReader = Json.reads[AppendCommentRequest]
}

object db {
  type TheTuple = (Long, Timestamp, String, String, Int, Int, Option[String], Option[String])

  class TheTable(tag: Tag) extends Table[TheTuple](tag, "comments") {
    def id       : Rep[Long]           = column[Long]           ("id", O.PrimaryKey, O.AutoInc)
    def at       : Rep[Timestamp]      = column[Timestamp]      ("at")
    def target   : Rep[String]         = column[String]         ("target")
    def text     : Rep[String]         = column[String]         ("comment")
    def likes    : Rep[Int]            = column[Int]            ("likes")
    def dislikes : Rep[Int]            = column[Int]            ("dislikes")
    def name     : Rep[Option[String]] = column[Option[String]] ("name")
    def email    : Rep[Option[String]] = column[Option[String]] ("email")
    def *  = (id, at, target, text, likes, dislikes, name, email)
  }

  val comments = TableQuery[TheTable]
}

trait Comments {
  def all(): Future[Seq[Comment]]
  def append(req: AppendCommentRequest): Future[Either[Throwable, Comment]]
}

@Singleton
class DatabaseComments @Inject() (services: Services, db: Database) extends Comments {
  import domain.comments.db._

  implicit val ec = services.ec()

  override def all() = {
    val query = comments.sortBy(_.at)
    db.run(query.result) map {
      _ map {
        case (id, at, target, text, likes, dislikes, name, email) =>
          Comment(id, at, target, text, likes, dislikes, name, email)
      }
    }
  }

  override def append(req: AppendCommentRequest): Future[Either[Throwable, Comment]] = {
    val instant = services.clock().instant()
    val at = new Timestamp(instant.toEpochMilli)
    db.run {
      (comments returning comments.map(_.id)) += (0l, at, req.target, req.text, 0, 0, req.name, req.email)
    } map {
      id =>
        Right(
          Comment(
            id       = id,
            at       = new Date(at.getTime),
            target   = req.target,
            text     = req.text,
            likes    = 0,
            dislikes = 0,
            name     = req.name,
            email    = req.email
          )
        )
    }
  }
}

