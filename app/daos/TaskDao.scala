package daos

import models.Task
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api._
import reactivemongo.api.bson.collection.BSONCollection
import reactivemongo.bson._

import javax.inject._
import scala.concurrent._
import scala.concurrent.ExecutionContext

@Singleton
class TaskDao @Inject()(
  mongoApi: ReactiveMongoApi
) (implicit ec: ExecutionContext) {

  val collection: Future[BSONCollection] = mongoApi.database.map(_.collection("tasks"))

  //пример
  def findById(id: BSONObjectID): Future[List[Task]] = {
    collection.flatMap(c =>
      c.find(BSONDocument("_id" -> id), None)
        .cursor[Task](ReadPreference.secondaryPreferred)
        .collect[List](-1, Cursor.FailOnError[List[Task]]())
    )
  }

  def all() {}

  def newTasks() {}

  def update(id: Long) {}

  def delete(id: Long) {}

}
