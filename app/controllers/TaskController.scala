package controllers

import daos.TaskDao
import models.Task
import play.api.mvc._
import play.api.libs.json.Json
import com.google.inject.Inject

class TaskController @Inject() (
  cc: ControllerComponents,
  taskDao: TaskDao
) extends AbstractController (cc) {

  def allTasks(): Action[AnyContent] = Action {
    Ok(Json.toJson(taskDao.all()))
  }

  def newTask(): Action[AnyContent] = TODO

  def completeTask(id: Long): Action[AnyContent] = Action { implicit request =>
    taskDao.update(id)
    Ok("")
  }
  def deleteTask(id: Long): Action[AnyContent] = Action { implicit request =>
      taskDao.delete(id)
      Ok("")
  }
}
