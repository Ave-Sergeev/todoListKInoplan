package actions

import play.api.mvc.{Request, WrappedRequest}
import models.Task

@Singleton
class TodoRequest[A](
  val task: Task,
  val request: Request[A]
) extends WrappedRequest[A](request)
