package modules

import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api._
import scala.concurrent.ExecutionContext
import scala.concurrent.Future

class ReactiveApiImpl(
  asyncDriver: AsyncDriver
)(implicit ec: ExecutionContext) extends ReactiveMongoApi {

  override val driver: AsyncDriver = asyncDriver
  val parsedUri: Future[ParsedURIWithDB] = MongoConnection.fromStringWithDB("uri to db")
  override val connection: Future[MongoConnection] = parsedUri
    .flatMap(parsed => driver.connect(parsed, parsed.db.some, strictMode = false))
  override def database: Future[DB] = parsedUri.flatMap(uri => connection.flatMap(_.database(uri.db)))
}
