package controllers

import javax.inject._
import play.api._
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {


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


    username.map {
      uname =>
        Redirect("/").withCookies(Cookie("nicelist", uname, sameSite = Some(Cookie.SameSite.Lax)))
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
