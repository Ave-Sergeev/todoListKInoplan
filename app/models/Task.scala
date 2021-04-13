package models

import play.api.libs.json._

case class Task (
  id: Long,
  descriptions: String,
  isCompleted: Boolean,
  deleted: Boolean
)

object Task {
  //на макросе?
  implicit val taskFormat: OFormat[Task] = Json.format[Task]

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
}

