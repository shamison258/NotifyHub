package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util._
import scala.concurrent._
import dao._

@Singleton
class HomeController @Inject() (val userDao: UserDAO) extends Controller {
  def index = Action.async {
    Future.successful(Ok(views.html.index("Hello! This is  Index Page !!")))
  }

  def admin = Action.async {
    Future.successful(Ok(views.html.admin("This is Admin Page. Check All Info")))
  }

  def user(id: Long) = Action.async {
    val msg = "This is User Page. Check User Info"
    userDao.findById(id).map(user => Ok(views.html.user(msg, user)))
  }
}
