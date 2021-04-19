package Actions

import actions.TodoRequest
import play.api.mvc.Result
import play.api.mvc.Security.AuthenticatedRequest

import scala.concurrent.{ExecutionContext, Future}

class TodoActionMock (implicit val executionContext: ExecutionContext) {
   def filter[A](request: TodoRequest[A]): Future[Option[Result]] = {
    Future.successful(None)
  }
}
