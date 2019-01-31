package customfilters

import akka.stream.Materializer
import com.typesafe.config.Config
import javax.inject.Inject
import play.Configuration
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}

class P3PFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext, conf: Config) extends Filter {

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

    val p3p = conf.getString("p3p")

    nextFilter(requestHeader).map { result =>
      if (p3p == null || p3p.trim().isEmpty) {
        Logger.warn("No p3p value specified")
        result
      }
      else {
        Logger.warn(s"adding P3P header $p3p" )
        result.withHeaders("P3P" -> ("CP=\"" + p3p + "\""))
      }
    }
  }
}