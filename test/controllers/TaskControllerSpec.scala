package controllers

import scala.concurrent.{ExecutionContext, Future}
import akka.actor.ActorSystem
import actions.TodoAction
import models.Task
import org.mockito.ArgumentMatchers._
import org.mockito.Mockito._
import org.scalatest.MustMatchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{BodyParsers, MessagesControllerComponents, Request, Results}
import play.api.test.Helpers._
import play.api.test._
import services.TaskService
import scala.Option.when

class TaskControllerSpec
  extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar with MustMatchers {

  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  implicit val mat: ActorSystem = ActorSystem()
  implicit val bp: BodyParsers.Default = new BodyParsers.Default

  val smcc: MessagesControllerComponents = stubMessagesControllerComponents()

  def beforeAll(): Unit = Play.start(app)
  def afterAll(): Unit = Play.stop(app)

  val taskService: TaskService = mock[TaskService]
  val todoAction: TodoAction = mock[TodoAction]

  val controller = new TaskController(todoAction, taskService)
//
//  "allTasks test" should {
//    val url = controllers.routes.TaskController.allTasks().url
//    val request = FakeRequest(GET, url)
//
//    "not empty response from dao" in {
//      when( taskService.allTasks.thenReturn(Future(task)))
//      val method = controller.apply(request)
//      status(method) mustBe OK
//      contentAsJson(method) mustBe Json.toJson()
//    }
//
//    "empty response from dao" in {
//
//    }
//  }

}

