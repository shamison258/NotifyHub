package controllers.nonauth

import javax.inject._

import config.AuthConfigImpl
import dao._
import jp.t2v.lab.play2.auth.OptionalAuthElement
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(val accountDAO: AccountDAO)
    extends Controller with OptionalAuthElement with AuthConfigImpl {
  def index = StackAction { implicit request =>
    Ok(views.html.index("Hello! This is  Index Page !!")(loggedIn))
  }
}
