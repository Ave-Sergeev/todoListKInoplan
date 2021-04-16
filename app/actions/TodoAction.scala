package actions

import play.api.libs.json.Json
import play.api.mvc.Results.NotFound
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc._
import reactivemongo.api.bson.BSONObjectID
import services.TaskService

@Singleton
class TodoAction @Inject()(
  defaultParser: BodyParsers.Default
) (implicit
  ec: ExecutionContext,
  taskService: TaskService
) {

  def todoAction(id: BSONObjectID): ActionBuilder[TodoRequest, AnyContent] =
    new ActionBuilder[TodoRequest, AnyContent] {
      def executionContext: ExecutionContext = ec
      override def parser: BodyParser[AnyContent] = defaultParser
      override def invokeBlock[A](request: Request[A], block: TodoRequest[A] =>
        Future[Result]): Future[Result] = {
        taskService.findOne(id).map(taskO =>
          taskO.map(new TodoRequest(_, request))
            .toRight(NotFound(Json.obj("error" -> "placement not found")))
        )
      }
    }
}