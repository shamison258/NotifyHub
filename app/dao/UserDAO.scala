package dao

import javax.inject._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

import models.User


@Singleton
class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
    extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val users = TableQuery[UsersTable]

  def create(user: User): Future[Unit] = db.run(users += user).map(_ => ())

  def findById(id: Long):Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)

  def list: Future[Seq[User]] = db.run(users.result)

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER")  {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def email = column[String]("EMAIL")
    def emailConfirmed = column[Boolean]("EMAIL_CONFIRMED")
    def name = column[String]("NAME")
    def password = column[String]("PASSWORD")
    def * = (id, email, emailConfirmed, name, password) <> (User.tupled, User.unapply)
  }

}
