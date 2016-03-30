package config

import controllers.routes
import dao.AccountDAO
import jp.t2v.lab.play2.auth.{AuthConfig, CookieTokenAccessor}
import models.Role.{Admin, Common}
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.{ClassTag, classTag}


trait AuthConfigImpl extends AuthConfig {

  type Id = Long
  type User = models.Account
  type Authority = models.Role
  /**
    * (Optional)
    * SessionID Tokenの保存場所の設定です。
    * デフォルトでは Cookie を使用します。
    */
  override lazy val tokenAccessor = new CookieTokenAccessor(
    /*
     * cookie の secureオプションを使うかどうかの設定です。
     * デフォルトでは利便性のために false になっていますが、
     * 実際のアプリケーションでは true にすることを強く推奨します。
     */
    cookieSecureOption = play.api.Play.isProd(play.api.Play.current),
    cookieMaxAge = Some(sessionTimeoutInSeconds)
  )
  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds: Int = 3600
  val accountDAO: AccountDAO

  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] =
    accountDAO.findById(id)

  /**
    * ログインが成功した際に遷移する先を指定します。
    */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.HomeController.index()))

  /**
    * ログアウトが成功した際に遷移する先を指定します。
    */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.HomeController.index()))

  /**
    * 認証が失敗した場合に遷移する先を指定します。
    */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.HomeController.index()))

  /**
    * 認可(権限チェック)が失敗した場合に遷移する先を指定します。
    */
  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext): Future[Result] = {
    Future.successful(Forbidden("no permission"))
  }

  /** redirect
    * 権限チェックのアルゴリズムを指定します。
    * 任意の処理を記述してください。
    */
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
    (user.role, authority) match {
      case (Admin, _) => true
      case (Common, Common) => true
      case _ => false
    }
  }

}
