package controllers

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
    Ok(views.html.index("Hello! This is  Index Page !!")(loggedIn.getOrElse(null)))
  }

  def admin = AsyncStack { implicit request =>
    val msg = "This is Admin Page. Check All Info"
    accountDAO.all.map(users => Ok(views.html.admin(msg, users)(loggedIn.get)))
  }


  def account(id: Long) = AsyncStack { implicit request =>
    val msg = "This is User Page. Check User Info"
    accountDAO.findById(id).map(user => Ok(views.html.user(msg, user)))
  }
}
