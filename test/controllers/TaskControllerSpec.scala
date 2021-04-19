package controllers


import org.mockito.Mockito._
import actions.TodoAction
import akka.actor.ActorSystem
import models.Task
import org.scalatest.MustMatchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import play.api.http.Status.OK
import play.api.libs.json.Json
import play.api.mvc.{BodyParsers, Results}
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, contentAsJson, defaultAwaitTimeout, status}
import services.TaskService
import scala.Option.when
import scala.concurrent.{ExecutionContext, Future}

class TaskControllerSpec
  extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar with MustMatchers {

  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  implicit val mat: ActorSystem = ActorSystem()
  implicit val bp: BodyParsers.Default = new BodyParsers.Default

  def beforeAll(): Unit = Play.start(app)
  def afterAll(): Unit = Play.stop(app)

  val taskService: TaskService = mock[TaskService]
  val todoAction: TodoAction = mock[TodoAction]

  val controller = new TaskController(todoAction, taskService)

  "allTasks test" should {
    val request = FakeRequest(GET, "/api/all")

      "empty response response from dao" in {
        when( taskService.findAll() ).thenReturn(Future(Nil))
        val method = controller.allTasks().apply(request)
        status(method) mustBe OK
        contentAsJson(method) mustBe Json.toJson(Seq[Task]())
      }
  }
}

