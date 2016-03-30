package controllers

import javax.inject._

import config.AuthConfigImpl
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import dao._
import jp.t2v.lab.play2.auth.LoginLogout
import models._


case class LoginForm(emailOrName: String, password: String)

class AuthController @Inject()(val accountDAO: AccountDAO,
  val messagesApi: MessagesApi) extends Controller
  with I18nSupport with AuthConfigImpl with LoginLogout {

  val signUpForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "role" -> ignored(Role.valueOf("Common")),
      "email" -> email,
      "emailConfirmed" -> ignored(false),
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(Account.apply)(Account.unapply))

  def signUp = Action.async { implicit request =>
    Future.successful(Ok(views.html.signup(signUpForm)))
  }

  def createAccount = Action.async { implicit request =>
    signUpForm.bindFromRequest.fold(
      error => {
        println("error")
        Future.successful(BadRequest(views.html.signup(error)))
      },
      account => accountDAO.create(account).map(_ =>
        Redirect(routes.HomeController.index())
      )
    )
  }

  val loginForm = Form(
    mapping(
      "emailOrName" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginForm.apply)(LoginForm.unapply))

  def login = Action.async { implicit request =>
    Future.successful(Ok(views.html.login(loginForm)))
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.login(formWithErrors))),
      form => accountDAO.authenticate(form.emailOrName, form.password)
        .flatMap(account => gotoLoginSucceeded(account.get.id.get))
    )
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded
  }

  def deleteAccount = TODO


}
