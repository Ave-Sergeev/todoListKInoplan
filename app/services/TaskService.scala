package services

import com.google.inject.Inject
import daos.TaskDao
import models.Task
import reactivemongo.api.bson.BSONObjectID
import reactivemongo.api.commands.WriteResult
import scala.concurrent.Future

class TaskService @Inject()(
  taskDao: TaskDao)(
) {

  def findAll: Future[Seq[Task]] =
    taskDao.findAll
  def findOne(id: BSONObjectID): Future[Option[Task]] =
    taskDao.findById(id)
  def create(task: Task): Future[WriteResult] =
    taskDao.create(task)
  def update(id: BSONObjectID, task: Task): Future[WriteResult] =
    taskDao.update(id, task)
  def delete(id: BSONObjectID): Future[WriteResult] =
    taskDao.delete(id)
}