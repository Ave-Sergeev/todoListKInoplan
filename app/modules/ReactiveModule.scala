package modules

import com.google.inject.AbstractModule
import play.modules.reactivemongo.ReactiveMongoApi

class ReactiveModule extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[ReactiveMongoApi]).to(classOf[ReactiveApi])
  }
}
