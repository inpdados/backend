package domain

import java.util.Date

import play.api.libs.json.Json
import play.api.libs.json.Reads.dateReads
import play.api.libs.json.Writes.dateWrites

case class ServerTime(time: Date)

object json {
  val format = "yyyyMMdd'T'HHmmss"
  implicit val CustomDateWrites = dateWrites(format)
  implicit val CustomDateReads  = dateReads(format)
  implicit val ServerTimeWriter = Json.writes[ServerTime]
}