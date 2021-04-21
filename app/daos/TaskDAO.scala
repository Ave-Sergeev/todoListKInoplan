package daos

import javax.inject._

import scala.concurrent._
import scala.concurrent.ExecutionContext

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.Cursor
import reactivemongo.api.bson.{BSONDocument, BSONObjectID}
import reactivemongo.api.bson.Macros.Placeholder.Handler
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.api.commands.WriteResult
import models.Task

@Singleton
class TaskDAO @Inject()(
  mongoApi: ReactiveMongoApi
)(
  implicit ec: ExecutionContext
) {

  val collection: Future[BSONCollection] = mongoApi.database.map(_.collection("tasks"))

  def findAll(): Future[Seq[Task]] =
    collection.flatMap(
      _.find(BSONDocument(), None)
        .cursor[Task]()
        .collect[Seq](-1, Cursor.FailOnError[Seq[Task]]())
    )

  def findById(id: BSONObjectID): Future[Option[Task]] =
    collection.flatMap(
      _.find(BSONDocument("_id" -> id), Option.empty[Task])
        .one[Task]
    )

  def create(task: Task): Future[WriteResult] =
    collection.flatMap(
      _.insert(ordered = false).one(task)
    )

  def update(id: BSONObjectID, task: Task): Future[WriteResult] =
    collection.flatMap(
      _.update(ordered = false)
        .one(BSONDocument("_id" -> id), task)
    )

  def delete(id: BSONObjectID): Future[WriteResult] =
    collection.flatMap(
      _.delete()
        .one(BSONDocument("_id" -> id), Some(1))
    )
}
