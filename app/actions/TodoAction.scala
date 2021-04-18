package actions

import models.Task
import play.api.libs.json.Json
import play.api.mvc.Results.{BadRequest, NotFound}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import reactivemongo.api.bson.{BSONDocument, BSONObjectID}
import services.TaskService

@Singleton
class TodoAction @Inject()(
  defaultParser: BodyParsers.Default
) (implicit
  ec: ExecutionContext,
  taskService: TaskService
) {

  def todoAction(id: BSONObjectID): ActionBuilder[TodoRequest, AnyContent] = {

    new ActionBuilder[TodoRequest, AnyContent] {

      def executionContext: ExecutionContext = ec

      override def parser: BodyParser[AnyContent] = defaultParser

      override def invokeBlock[A](request: Request[A], block: TodoRequest[A] =>
        Future[Result]): Future[Result] = {

        request match {
          case req: TodoRequest[A] => block(req)
          case _ => Future.successful(BadRequest("400 Invalid Request"))
        }
      }
    }
  }
}

//taskService.findOne(id).map(taskO =>
//taskO.map(new TodoRequest(_, request))
//.toRight(NotFound(Json.obj("error" -> "placement not found")))
//)