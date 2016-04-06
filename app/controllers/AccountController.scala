package controllers

import javax.inject._

import config.AuthConfigImpl
import dao._
import models._
import jp.t2v.lab.play2.auth.AuthElement
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AccountController @Inject()(val accountDAO: AccountDAO)
    extends Controller with AuthElement with AuthConfigImpl {
  def admin = AsyncStack(AuthorityKey -> Role.valueOf("Admin")) {
    implicit request =>
    val msg = "This is Admin Page. Check All Info"
    accountDAO.all
      .map(users => Ok(views.html.admin(msg, users)(Option(loggedIn))))
  }

  def account(id: Long) = AsyncStack(AuthorityKey -> Role.valueOf("Common")) {
    implicit request =>
    val msg = "This is User Page. Check User Info"
    accountDAO.findById(id).map(user => Ok(views.html.user(msg, user)))
  }
}
