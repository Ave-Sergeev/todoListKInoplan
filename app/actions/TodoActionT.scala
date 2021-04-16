package actions

import play.api.mvc.{ActionBuilder, ActionRefiner, AnyContent, Request}

trait TodoActionT extends ActionBuilder[TodoRequest, AnyContent]
  with ActionRefiner[Request, TodoRequest]