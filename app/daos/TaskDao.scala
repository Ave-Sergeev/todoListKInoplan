package daos

import models.Task

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class TaskDao @Inject() () (implicit ec: ExecutionContext) {
  // тут описываются запросы к бд?
  def all() {}

  def newTasks() {}

  def update(id: Long) {}

  def delete(id: Long) {}

}
