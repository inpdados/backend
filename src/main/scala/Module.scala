import java.time.Clock

import com.google.inject.AbstractModule
import domain.comments.{Comments, DatabaseComments}
import play.api.{Configuration, Environment}
import xingu.commons.play.services.{BasicServices, Services}

class Module (env: Environment, conf: Configuration) extends AbstractModule {
  override def configure() = {

    import slick.jdbc.PostgresProfile.api._

    bind(classOf[Database]).toInstance(
      Database.forConfig(
        path   = "database",
        config = conf.underlying /* important: otherwise the configuration overrides may not work */
      )
    )

    bind(classOf[Clock])    .toInstance(Clock.systemDefaultZone)
    bind(classOf[Services]) .to(classOf[BasicServices]).asEagerSingleton()
    bind(classOf[Comments]) .to(classOf[DatabaseComments])
  }
}
