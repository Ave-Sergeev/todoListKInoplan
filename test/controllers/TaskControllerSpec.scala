package controllers

import org.mockito.Mockito._
import actions.TodoAction
import akka.actor.ActorSystem
import models.Task
import org.mockito.ArgumentMatchers.{any, anyLong, anyString}
import org.scalatest.MustMatchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Play
import play.api.http.Status.OK
import play.api.libs.json.Json
import play.api.mvc.{BodyParsers, ControllerComponents, Results}
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{GET, contentAsJson, defaultAwaitTimeout, status}
import reactivemongo.api.bson.BSONObjectID
import services.TaskService
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

  val controller: TaskController = new TaskController(todoAction, taskService) {
    override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
  }

  "allTasks test" should {
    val request = FakeRequest(GET, "/api/all")

      "empty response response from dao" in {
        when( taskService.findAll() ).thenReturn(Future(Nil))

        val method = controller.allTasks().apply(request)

        status(method) mustBe OK
        contentAsJson(method) mustBe Json.toJson(Seq[Task]())
      }
  }

//  "oneTask(id: BSONObjectID)" should {
//    val requestId: BSONObjectID = "6076e7758700001a026e1040"
//    val repoResponseUser = Task("6076e7758700001a026e1040" ,"descriptions", false, false)
//    val request = FakeRequest(GET, "/api/one/:id")
//
//    when( taskService.findOne(anyLong()).thenReturn(Future()))
//    val method = controller.oneTask(requestId).apply(request)
//
//    "read data from request" in {
//      verify(taskService).findOne(requestId)
//    }
//
//    "transfer data from dao to Result" in {
//      status(method) mustBe OK
//      contentAsJson(method) mustBe Json.toJson(repoResponseUser)
//    }
//
//    "user not found in dao" in {
//      when( taskService.findOne(anyLong()) ).thenReturn(Future(Option.empty))
//      val method = controller.oneTask().apply(request)
//      status(method) mustBe NOT_FOUND
//    }
//  }

  "delete(id: BSONObjectID)" should {
    val deleteId = BSONObjectID.generate
    val request = FakeRequest(GET, "/api/delete/:id")

//    "user found" in {
//      when( taskService.delete(deleteId) ).thenReturn(Future())
//      val method = controller.deleteTask(deleteId).apply(request)
//      status(method) mustBe OK
//    }

//    "user not found" in {
//      when( taskService.delete(deleteId) ).thenReturn(Future())
//      val method = controller.deleteTask(deleteId).apply(request)
//      status(method) mustBe NOT_FOUND
//    }
  }
}

