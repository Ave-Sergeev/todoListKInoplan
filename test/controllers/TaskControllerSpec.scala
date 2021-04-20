package controllers

import scala.concurrent.{ExecutionContext, Future}
import play.api.Play
import play.api.http.Status.{BAD_REQUEST, CREATED, INTERNAL_SERVER_ERROR, NOT_FOUND, OK}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{BodyParsers, ControllerComponents, Request, Results}
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{CONTENT_TYPE, GET, POST, PUT, call, contentAsJson, defaultAwaitTimeout, status}
import play.api.http.MimeTypes.JSON
import akka.actor.ActorSystem
import Actions.TodoActionMock
import org.mockito.ArgumentMatchers.any
import org.scalatest.MustMatchers
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import reactivemongo.api.bson.BSONObjectID
import services.TaskService
import models.Task

class TaskControllerSpec
  extends PlaySpec with Results with GuiceOneAppPerSuite with MockitoSugar with MustMatchers with TodoActionMock {

  implicit val ex: ExecutionContext = app.injector.instanceOf[ExecutionContext]
  implicit val mat: ActorSystem = ActorSystem()
  implicit val bp: BodyParsers.Default = new BodyParsers.Default

  def beforeAll(): Unit = Play.start(app)
  def afterAll(): Unit = Play.stop(app)

  val taskService: TaskService = mock[TaskService]
  val sessionTask: Task = Task(BSONObjectID.generate, "testDescriptions" ,completed = false ,deleted = false)

  val controller: TaskController = new TaskController(todoAction, taskService) {
    override def controllerComponents: ControllerComponents = Helpers.stubControllerComponents()
  }

  when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionWithRequest(sessionTask))

  "allTasks() test" should {
    val request = FakeRequest(GET, "/api/all")

      "empty response response from dao" in {
        when( taskService.findAll() ).thenReturn(Future(Nil))

        val method = controller.allTasks().apply(request)

        status(method) mustBe OK
        contentAsJson(method) mustBe Json.toJson(Seq[Task]())
      }
  }

  "oneTask(id: BSONObjectID) test" should {
    val requestId: BSONObjectID = BSONObjectID.generate
    val repoResponseTask = Task(BSONObjectID.generate, "descriptions", completed = false, deleted = false)
    val request = FakeRequest(GET, "/api/one/:id")

    when( taskService.findOne(any[BSONObjectID])).thenReturn(Future(Option(repoResponseTask)))
    val method = controller.oneTask(requestId).apply(request)

    "user not found in dao" in {
      when( taskService.findOne(any[BSONObjectID]) ).thenReturn(Future(Option.empty))
      when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionNotFound(sessionTask))
      val method = controller.oneTask(requestId).apply(request)
      status(method) mustBe NOT_FOUND
    }
  }

  "addTask() test" should {
    val newTask = Task(BSONObjectID.generate, "descriptions", completed = false, deleted = false)
    val request: Request[JsValue] = FakeRequest(POST, "/api/add")
      .withHeaders(CONTENT_TYPE -> JSON)
      .withBody(Json.toJson(newTask))

    "add" in {
      when( taskService.create(any[Task]) ).thenReturn(Future.successful(Right(()) ))
      when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionCreated(sessionTask))
      val method = call(controller.addTask(), request)
      status(method) mustBe CREATED
    }

    "read data from request" in {
      verify(taskService).create(newTask)
    }

    "not add" in {
      when( taskService.create(any[Task]) ).thenReturn(Future.successful(Left("test error") ))
      //when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionBadRequest(sessionTask))
      val method = call(controller.addTask(), request)
      status(method) mustBe INTERNAL_SERVER_ERROR
    }

    "corrupt json" in {
      val request = FakeRequest(POST, "/api/add")
        .withHeaders(CONTENT_TYPE -> JSON)
        .withBody(Json.obj(
          "_id"    -> "",
          "descriptions" -> "error",
          "completed" -> false,
          "deleted" -> false
        ))

      val method = call(controller.addTask(), request)
      status(method) mustBe BAD_REQUEST
    }
  }

  "update(id: BSONObjectID)" should {
    val updateId = BSONObjectID.generate
    val request: Request[JsValue] = FakeRequest(PUT, "/api/update/:id")
      .withHeaders(CONTENT_TYPE -> JSON)
      .withBody(Json.toJson(sessionTask))

    "user found" in {
      when( taskService.update(any[BSONObjectID], any[Task]) ).thenReturn(Future.successful(Right(()) ))
      val method = call(controller.completeTask(updateId), request)
      status(method) mustBe OK
    }

    "user not found" in {
      when( taskService.update(any[BSONObjectID], any[Task]) ).thenReturn(Future.successful(Right(()) ))
      when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionNotFound(sessionTask))
      val method = call(controller.completeTask(updateId), request)
      status(method) mustBe NOT_FOUND
    }

    "user update not his data" in {
      val strangerUser = Task(updateId, "testDescription", completed = false, deleted = false )
      val request = FakeRequest(PUT, "/api/update/:id")
        .withHeaders(CONTENT_TYPE -> JSON)
        .withBody(Json.toJson(strangerUser))
      val method = call(controller.completeTask(updateId), request)
      status(method) mustBe NOT_FOUND
    }

    "corrupt json" in {
      val request = FakeRequest(GET, "/api/update/:id")
        .withHeaders(CONTENT_TYPE -> JSON)
        .withBody(Json.obj(
          "_id"    -> "",
          "descriptions" -> "error",
          "completed" -> false,
          "deleted" -> false
        ))

      val method = call(controller.completeTask(updateId), request)
      status(method) mustBe BAD_REQUEST
    }
  }

  "delete(id: BSONObjectID)" should {
    val deleteId = BSONObjectID.generate
    val request = FakeRequest(GET, s"/api/delete/$deleteId")

    "user found" in {
      when( taskService.delete(any[BSONObjectID]) ).thenReturn(Future.successful(Right(()) ))
      val method = controller.deleteTask(deleteId).apply(request)
      status(method) mustBe OK
    }

   "user not found" in {
     when( taskService.delete(deleteId) ).thenReturn(Future.successful(Right(()) ))
     when( todoAction.todoAction(any[BSONObjectID]) ).thenReturn(todoActionNotFound(sessionTask))
     val method = controller.deleteTask(deleteId).apply(request)
      status(method) mustBe NOT_FOUND
    }
  }
}

