package sentry

import javax.inject.{Inject, Singleton}

import play.api.Configuration
import io.sentry.SentryClientFactory

@Singleton
class SentryExceptionReporter @Inject() (
  config: Configuration
) {

  private val sentryUri = config.get[String]("sentry.uri")
  private val sentryClient = SentryClientFactory.sentryClient(sentryUri)

  def sendException(ex: Throwable): Unit = {
    sentryClient.sendException(ex)
  }
}
