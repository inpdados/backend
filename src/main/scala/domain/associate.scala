package domain.associates

import java.sql.Timestamp
import java.util.Date

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.libs.json.Reads.dateReads
import play.api.libs.json.Writes.dateWrites
import slick.jdbc.PostgresProfile.api._
import xingu.commons.play.services.Services

import scala.concurrent.Future

case class RegisterAssociateRequest(kind: String)

case class Associate (
  id       : Long   ,
  created  : Date   ,
  kind     : String ,
)

object json {
  val format = "yyyyMMdd'T'HHmmss"
  implicit val CustomDateWrites = dateWrites(format)
  implicit val CustomDateReads  = dateReads(format)
  implicit val AssociateWriter  = Json.writes[Associate]
  implicit val RegisterAssociateRequestReader = Json.reads[RegisterAssociateRequest]
}

object db {

  type AssociateTuple = (Long, Timestamp, String)

  class AssociateTable(tag: Tag) extends Table[AssociateTuple](tag, "associates") {
    def id       : Rep[Long]      = column[Long]      ("id", O.PrimaryKey, O.AutoInc)
    def created  : Rep[Timestamp] = column[Timestamp] ("created")
    def kind     : Rep[String]    = column[String]    ("kind")
    def *  = (id, created, kind)
  }

  val associates = TableQuery[AssociateTable]
}

trait Associates {
  def all(): Future[Seq[Associate]]
  def register(req: RegisterAssociateRequest): Future[Either[Throwable, Associate]]
}

@Singleton
class DatabaseAssociates @Inject() (services: Services, db: Database) extends Associates {

  import domain.associates.db._
  implicit val ec = services.ec()

  override def all() = {
    val query = associates.sortBy(_.created)
    db.run(query.result) map {
      _ map {
        case (id, kind, created) =>
          Associate(id, kind, created)
      }
    }
  }

  override def register(req: RegisterAssociateRequest): Future[Either[Throwable, Associate]] = {
    val instant = services.clock().instant()
    val created = new Timestamp(instant.toEpochMilli)
    db.run {
      (associates returning associates.map(_.id)) += (0L, created, "user")
    } map {
      id =>
        Right(
          Associate(
            id      = id,
            created = new Date(created.getTime),
            kind    = "user"
          )
        )
    }
  }
}

