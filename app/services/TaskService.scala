package services

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import reactivemongo.api.bson.BSONObjectID
import daos.TaskDAO
import models.Task

class TaskService @Inject() (
  taskDao: TaskDAO)(
) {

  def findAll(): Future[Seq[Task]] =
    taskDao.findAll()

  def findOne(id: BSONObjectID): Future[Option[Task]] =
    taskDao.findById(id)

  def create(task: Task): Future[Either[String, Unit]] =
    taskDao.create(task).map { writeResult =>
      val errors = writeResult.writeErrors

      if (errors.isEmpty) {
        Right(())
      } else {
        Left(errors.mkString(", "))
      }
    }

  def update(id: BSONObjectID, task: Task): Future[Either[String, Unit]] =
    taskDao.update(id, task).map { writeResult =>
      val errors = writeResult.writeErrors

      if (errors.isEmpty) {
        Right(())
      } else {
        Left(errors.mkString(", "))
      }
    }

  def delete(id: BSONObjectID): Future[Either[String, Unit]] =
    taskDao.delete(id).map { writeResult =>
      val errors = writeResult.writeErrors

      if (errors.isEmpty) {
        Right(())
      } else {
        Left(errors.mkString(", "))
      }
    }
}
