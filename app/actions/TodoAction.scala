package actions

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.Results.NotFound
import play.api.mvc._
import services.TaskService
import reactivemongo.api.bson.BSONObjectID

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

      override def invokeBlock[A](
        request: Request[A],
        block: TodoRequest[A] => Future[Result]
      ): Future[Result] = {

        taskService
          .findOne(id)
          .flatMap(_.map(task =>
            block(new TodoRequest(task, request)))
            .getOrElse(Future.successful(NotFound(s"task with id ${id.stringify} not exist"))
          ))
      }
    }
  }
}