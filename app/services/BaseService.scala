package services

trait BaseService {
  def findAll(): Unit
  def findOne(): Unit
  def create(): Unit
  def update(): Unit
  def delete(): Unit
}