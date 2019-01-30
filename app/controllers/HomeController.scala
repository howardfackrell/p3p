package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, conf: Configuration) extends AbstractController(cc) {


  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>

    request.cookies.get("nicelist")
        .map{
          cookie =>
            Ok(s"${cookie.value} is on the nice list")
        }.getOrElse(Ok(views.html.login()))
  }

  def login() = Action {implicit request: Request[AnyContent] =>

    val username: Option[String] = request.body.asFormUrlEncoded.flatMap {
      form: Map[String, Seq[String]] =>
        form.getOrElse("username", Seq[String]()).headOption
    }

    val p3p: String  = conf.get[String]("p3p")
    username.map {
      uname =>
        val response = Redirect("/").withCookies(Cookie("nicelist", uname))
        if (p3p == null || p3p.trim().isEmpty) {
          Logger.warn("No p3p value specified")
          response
        }
        else {
          Logger.warn(s"setting cookie with p3p value of '$p3p'")
          response.withHeaders("P3P" -> ("CP=\"" + p3p + "\""))
        }
    }.getOrElse(Ok(views.html.login()))
  }

  def user = Action {implicit request: Request[AnyContent] =>
    request.cookies.get("nicelist")
      .map{
        cookie =>
          Ok(s"${cookie.value}")
      }.getOrElse(Unauthorized("You don't have a nicelist cookie"))
  }

  def corsReq = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.corsreq())
  }
}
