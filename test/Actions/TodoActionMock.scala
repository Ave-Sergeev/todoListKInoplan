package Actions

import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.{ActionBuilder, AnyContent, BodyParser, BodyParsers, Request, Result, Results}
import actions.{TodoAction, TodoRequest}
import models.Task
import org.mockito.MockitoSugar
import org.scalatest.MustMatchers
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import reactivemongo.api.bson.BSONObjectID
import services.TaskService

trait TodoActionMock extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar with MustMatchers {

  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  val taskService: TaskService = mock[TaskService]
  val sessionTask: Task = Task(BSONObjectID.generate, "testDescriptions" ,completed = false ,deleted = false)
  val todoAction: TodoAction = mock[TodoAction]

  val todoActionWithRequest: Task => ActionBuilder[TodoRequest, AnyContent] = (task: Task) =>
    new ActionBuilder[TodoRequest, AnyContent] {

    override def parser: BodyParser[AnyContent] = mock[BodyParsers.Default]

    override def executionContext: ExecutionContext = ec

    def invokeBlock[A](request: Request[A], block: TodoRequest[A] => Future[Result]): Future[Result] = {
      block(new TodoRequest(task, request))
    }
  }

  val todoActionNotFound: Task => ActionBuilder[TodoRequest, AnyContent] = (task: Task) =>
    new ActionBuilder[TodoRequest, AnyContent] {

    override def parser: BodyParser[AnyContent] = mock[BodyParsers.Default]

    override def executionContext: ExecutionContext = ec

    def invokeBlock[A](request: Request[A], block: TodoRequest[A] => Future[Result]): Future[Result] = {
      Future.successful(NotFound(s"task with id ${task._id.stringify} not exist"))
    }
  }
}
