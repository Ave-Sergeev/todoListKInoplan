package controllers

import daos.TaskDao
import models.Task
import play.api.mvc._
import play.api.libs.json.{JsObject, Json}
import com.google.inject.Inject
import play.modules.reactivemongo.MongoController
import reactivemongo.play.json.collection.JSONCollection

class TaskController @Inject() (
  cc: ControllerComponents,
  taskDao: TaskDao
) extends AbstractController (cc)
  with MongoController
{
  //A reference of a JSON style collection in Mongo
  def tasksCollection: Any = db.collection[JSONCollection]("tasks")

  //Convinience helper thar marshalls json or sends a 404 if none found
  def asJson(v: Option[JsObject]): Result = v.map(Ok(_)).getOrElse(NotFound)


  //Default index entry point
  def index: Action[AnyContent] = Action {
    Ok("REST!")
  }

  def allTasks(): Action[AnyContent] = Action {
    Ok(Json.toJson(taskDao.all()))
  }

  def addTask(): Action[AnyContent] = TODO

  def completeTask(id: Long): Action[AnyContent] = Action { implicit request =>
    taskDao.update(id)
    Ok("")
  }
  def deleteTask(id: Long): Action[AnyContent] = Action { implicit request =>
      taskDao.delete(id)
      Ok("")
  }
}
