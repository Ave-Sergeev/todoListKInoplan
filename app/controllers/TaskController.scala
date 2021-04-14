package controllers
import com.google.inject.Inject
import daos.TaskDao
import models.Task
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.api.bson.BSONObjectID
import scala.concurrent.ExecutionContext
class TaskController @Inject()(
  taskDao: TaskDao
)(implicit ec: ExecutionContext
) extends InjectedController {

  def allTasks(): Action[AnyContent] = Action.async { implicit request =>
    taskDao.findAll.map(tasks => Ok(Json.toJson(tasks)))
  }
  def addTask(): Action[Task] = Action.async(parse.json[Task]) { request =>
    import request.{body => task}
    taskDao.create(task).map(_ => Created(Json.toJson(task)))
  }
  def completeTask(id: BSONObjectID): Action[Task] = Action.async(parse.json[Task]) {
    implicit request => import request.{body => task}
      taskDao.update(id, task).map(_ => Ok(Json.toJson(task)))
  }
  def deleteTask(id: BSONObjectID): Action[AnyContent] = Action.async { implicit request =>
    taskDao.delete(id).map(_ => NoContent)
  }
}