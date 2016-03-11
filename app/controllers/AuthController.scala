package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

import dao._

class AuthController @Inject()(userDao: UserDAO) extends Controller {


  def createUser = TODO

  def deleteUser = TODO

  def login = TODO

  def logout = TODO

}
