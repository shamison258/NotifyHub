package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import dao._
import models._

class AuthController @Inject()(userDao: UserDAO,
  val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val userForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "email" -> email,
      "emailConfirmed" -> ignored(false),
      "name" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)(User.unapply))

  def inputUserInfo = Action.async {
    Future.successful(Ok(views.html.createUser(userForm)))
  }

  def insertUser = Action.async { implicit request =>
    userForm.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.createUser(error))),
      user => userDao.create(user).map(_ =>
        Redirect(routes.HomeController.index)
      )
    )
  }

  def login = TODO

  def logout = TODO

  def deleteUser = TODO

}
