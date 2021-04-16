package actions

import models.Task
import play.api.mvc.{Request, WrappedRequest}

class TodoRequest[A](
  val task: Task,
  val request: Request[A]
) extends WrappedRequest[A](request)