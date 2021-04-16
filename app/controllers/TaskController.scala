package controllers

import actions.TodoAction
import com.google.inject.Inject
import models.Task
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.api.bson.BSONObjectID
import services.TaskService
import scala.concurrent.ExecutionContext

class TaskController @Inject()(
  todoAction: TodoAction,
  taskService: TaskService
) (implicit ex: ExecutionContext) extends InjectedController {

  def allTasks(): Action[AnyContent] = Action.async { implicit request =>
    taskService.findAll.map(tasks => Ok(Json.toJson(tasks)))
  }
  def oneTask(id: BSONObjectID): Action[AnyContent] = todoAction.todoAction(id).async { request =>
    taskService.findOne(id).map(task => Ok(Json.toJson(task)))
  }
  def addTask(): Action[Task] = Action.async(parse.json[Task]) { request =>
    import request.{body => task}
    taskService.create(task).map(_ => Created(Json.toJson(task)))
  }
  def completeTask(id: BSONObjectID): Action[Task] = todoAction.todoAction(id).async(parse.json[Task]) {
    implicit request => import request.{body => task}
      taskService.update(id, task).map(_ => if (id != null) Ok(Json.toJson(task)) else NotFound)
  }
  def deleteTask(id: BSONObjectID): Action[AnyContent] = todoAction.todoAction(id).async { implicit request =>
    taskService.delete(id).map(_ => NoContent)
  }
}