package models

case class User(
  id: Option[Long],
  email: String,
  emailConfirmed: Boolean,
  name: String,
  password: String)
