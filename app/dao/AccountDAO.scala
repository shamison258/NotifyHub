package dao

import javax.inject._

import models.{Account, Role}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.Future

@Singleton
class AccountDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._
  private val accounts = TableQuery[AccountsTable]

  // 初期化
  db.run(accounts += Account(  
    Some(9999L), Role.valueOf("Admin"),
    "admin@admin.com", true,
    "admin", "admin"))

  def create(account: Account): Future[Unit] =
    db.run(accounts += account).map(_ => ())

  def findById(id: Long): Future[Option[Account]] =
    db.run(accounts.filter(_.id === id).result.headOption)

  def authenticate(emailOrName: String, password: String): Future[Option[Account]] =
    db.run(accounts
      .filter(eon => eon.email === emailOrName || eon.name === emailOrName)
      .filter(_.password === password)
      .result.headOption)

  def all: Future[Seq[Account]] = db.run(accounts.result)

  private class AccountsTable(tag: Tag) extends Table[Account](tag, "ACCOUNT") {

    implicit val mappedRole = MappedColumnType.base[Role, String](
      { r => Role.toString(r) },
      { s => Role.valueOf(s) }
    )
    def * = (id, role, email, emailConfirmed, name, password) <>(Account.tupled, Account.unapply)
    def id = column[Option[Long]]("ID", O.PrimaryKey, O.AutoInc)
    def role = column[Role]("ROLE")
    def email = column[String]("EMAIL")
    def emailConfirmed = column[Boolean]("EMAIL_CONFIRMED")
    def name = column[String]("NAME")
    def password = column[String]("PASSWORD")
  }

}
