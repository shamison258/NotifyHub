package models

sealed trait Role

object Role {

  def valueOf(value: String): Role = value match {
    case "Admin" => Admin
    case "Common" => Common
    case _ => throw new IllegalArgumentException()
  }

  def toString(role: Role): String = role match {
    case Admin => "Admin"
    case Common => "Common"
    case _ => throw new IllegalArgumentException()
  }

  case object Admin extends Role
  case object Common extends Role

}
