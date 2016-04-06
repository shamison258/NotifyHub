package models

case class Account(
  id: Option[Long],
  role: Role,
  email: String,
  emailConfirmed: Boolean,
  name: String,
  password: String)
