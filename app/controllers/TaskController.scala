package controllers

import models.Task
import play.api.mvc._
import play.api.libs.json.Json
import com.google.inject.Inject

class TaskController @Inject() (
  cc: ControllerComponents
) extends AbstractController (cc) {

  def getAllTasks(): Action[AnyContent] = Action {
    NoContent
  }

  def newTask(): Action[AnyContent] = Action {
    NoContent
  }

  def completeTask(id: Long): Action[AnyContent] = Action {
    NoContent
  }

  def deleteTask(id: Long): Action[AnyContent] = Action {
    NoContent
  }
}
