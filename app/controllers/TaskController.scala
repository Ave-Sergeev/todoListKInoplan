package controllers

import com.google.inject.Inject
import models.Task
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import services.TaskService

import scala.concurrent.Future

class TaskController @Inject()(
  taskService: TaskService
) extends InjectedController {

  def allTasks(): Action[AnyContent] = Action.async { implicit request =>
    Future.successful(Ok(Json.toJson(taskService.allTasks)))
  }

  def addTask(): Action[Task] = Action(parse.json[Task]).async { request =>
    import request.{body => task}
    taskService.addTask(task)
    Future.successful(Ok(Json.toJson(task)))
  }

  def completeTask(id: Long): Action[AnyContent] = TODO

  def deleteTask(id: Long): Action[AnyContent] = Action.async {
    val savedTask: Task = taskService.deleted(id)
    Future.successful(Ok(Json.toJson(savedTask)))
  }
}