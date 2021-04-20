package controllers

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._
import play.api.mvc._
import reactivemongo.api.bson.BSONObjectID
import services.TaskService
import actions.TodoAction
import models.Task

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

    taskService.create(task).map {
      case Right(_) =>
        Created(Json.toJson(task))
      case Left(error) =>
        InternalServerError(Json.obj("error" -> error))
    }
  }

  def completeTask(id: BSONObjectID): Action[Task] = todoAction.todoAction(id).async(parse.json[Task]) { request =>
    import request.{body => task}

    taskService.update(id, task).map {
      case Right(_) =>
        Ok(Json.toJson(task))
      case Left(error) =>
        InternalServerError(Json.obj("error" -> error))
    }
  }

  def deleteTask(id: BSONObjectID): Action[AnyContent] = todoAction.todoAction(id).async { implicit request =>
    taskService.delete(id).map {
      case Right(_) =>
        Ok
      case Left(error) =>
        InternalServerError(Json.obj("error" -> error))
    }
  }
}
