package sentry

import javax.inject.Inject
import scala.concurrent.Future
import play.api.http.HttpErrorHandler
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

class ProdErrorHandler @Inject()(
  exceptionReporter: SentryExceptionReporter
)

  extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(Status(statusCode)(message))
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Status] = {
    val stackTrace = exception.getStackTrace.mkString("\n")

    exceptionReporter.sendException(exception)
    Future.successful(InternalServerError)
  }
}
