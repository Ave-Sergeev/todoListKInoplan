package controllers


import com.google.inject.Inject
import models.Task
import play.api.libs.json._
import play.api.mvc._


class TaskController @Inject() (
  cc: ControllerComponents
) extends AbstractController (cc)
{

  def allTasks(): Action[AnyContent] = Action { implicit request =>
    val tasks = List(Task(1,"",isCompleted = false, deleted = false))
    Ok(Json.toJson(tasks)).as("application/json")
  }

  def addTask(): Action[JsValue] = Action(parse.json) { implicit request =>
    val taskResult = request.body.validate[Task]
    taskResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "error", "message" -> JsError.toJson(errors)))
      },
      task =>  {
    Ok(Json.toJson(task)).as("application/json")
  }
    )
  }

  def completeTask(id: Long): Action[AnyContent] = TODO

  def deleteTask(id: Long): Action[AnyContent] = TODO
}
