package controllers

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._
import play.api.mvc._
import actions.TodoAction
import com.google.inject.Inject
import models.Task
import reactivemongo.api.bson.BSONObjectID
import services.TaskService

class TaskController @Inject()(
  todoAction: TodoAction,
  taskService: TaskService
) (implicit ex: ExecutionContext)
  extends InjectedController {

  def allTasks(): Action[AnyContent] = Action.async { implicit request =>
    taskService.findAll().map(tasks => Ok(Json.toJson(tasks)))
  }

  def oneTask(id: BSONObjectID): Action[AnyContent] = todoAction.todoAction(id).async { request =>
    val foundedTask = request.task
    Future.successful(Ok(Json.toJson(foundedTask)))
  }

  def addTask(): Action[Task] = Action.async(parse.json[Task]) { request =>
    import request.{body => task}
    taskService.create(task).map(_ => Created(Json.toJson(task)))
  }

  def completeTask(id: BSONObjectID): Action[Task] = todoAction.todoAction(id).async(parse.json[Task]) { request =>
    import request.{body => task}
    taskService.update(id, task).map { writeResult =>
      val errors = writeResult.writeErrors

      if (errors.isEmpty) {
        Ok(Json.toJson(task))
      } else {
        InternalServerError(Json.obj("error" -> errors.mkString(", ")))
      }
    }
  }

  def deleteTask(id: BSONObjectID): Action[AnyContent] = todoAction.todoAction(id).async { implicit request =>
    taskService.delete(id).map(_ => NoContent)
  }
}