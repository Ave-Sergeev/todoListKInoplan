package daos

import models.Task

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class TaskDao @Inject() () (implicit ec: ExecutionContext) {
  // тут описываются запросы к бд?
  def add(task: Task) {}

  def tasks() {}

  def taskById(id: Long) {}

  def tasksByUserId() {}

  def update(task: Task) {}

  def delete(id: Long) {}

}
