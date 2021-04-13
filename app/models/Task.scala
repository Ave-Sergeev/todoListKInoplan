package models

import play.api.libs.json._
import reactivemongo.bson.BSONObjectID

case class Task (
  _id: BSONObjectID = BSONObjectID.generate(),
  descriptions: String,
  isCompleted: Boolean,
  deleted: Boolean
)

trait TaskJson {
  //на макросе, сразу и reads и writes
  implicit val taskFormat: OFormat[Task] = Json.format[Task]
}

object Task extends TaskJson

/*
// на шаблоне
  implicit val reads: Reads[Task] = for {
    id <- (JsPath \ "id").readWithDefault[Long](0L)
    descriptions <- (JsPath \ "descriptions").read[String]
    isCompleted <- (JsPath \ "isCompleted").readWithDefault[Boolean](false)
    deleted <- (JsPath \ "deleted").readWithDefault[Boolean](false)
  } yield Task(id,  descriptions, isCompleted, deleted)

  implicit val writes: Writes[Task] = task => Json.obj(
    "id" -> task.id,
    "descriptions" -> task.descriptions,
    "isCompleted" -> task.isCompleted,
    "deleted" -> task.deleted
  )
 */
