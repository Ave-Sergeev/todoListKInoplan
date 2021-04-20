package actions

import play.api.mvc.{Request, WrappedRequest}
import models.Task

class TodoRequest[A](
  val task: Task,
  val request: Request[A]
) extends WrappedRequest[A](request)
