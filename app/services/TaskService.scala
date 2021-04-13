package services

import models.Task

class TaskService {
  def allTasks() {}

  def addTask(task: Task) {}

  def update(id: Long) {}

  def deleted(id: Long): Unit = {
    //    taskService.find(id)
    //    savedTask.copy(deleted = true)
    //    taskService.editTask(task)
    //    OK()
    //    savedTask.setDeleted(id)
    //    OK()
  }
}
