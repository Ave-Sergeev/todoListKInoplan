package models

import scala.util.{Failure, Success}
import play.api.libs.json._
import reactivemongo.api.bson.{BSONDocumentHandler, BSONObjectID, Macros}

case class Task (
  _id: BSONObjectID = BSONObjectID.generate(),
  descriptions: String,
  completed: Boolean,
  deleted: Boolean
)

trait TaskJson extends BsonIdToJson {
  implicit val jsonFormat: OFormat[Task] = Json.using[Json.WithDefaultValues].format[Task]
}

trait TaskBson {
  implicit val bsonFormat: BSONDocumentHandler[Task] = Macros.handler[Task]
}

object Task extends TaskJson with TaskBson

trait BsonIdToJson {

  implicit val objectIdReads: Reads[BSONObjectID] = Reads[BSONObjectID] { jsonValue: JsValue =>
    BSONObjectID.parse(jsonValue.as[String]) match {
      case Success(bsonId) => JsSuccess(bsonId)
      case Failure(e) =>
        JsError("Invalid id")
    }
  }

  implicit val objectIdWrites: Writes[BSONObjectID] = (bsonId: BSONObjectID) => {
    Json.toJson(bsonId.stringify)
  }
}
