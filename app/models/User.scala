package models

case class User(
  id: Long,
  email: String,
  emailConfirmed: Boolean,
  name: String,
  password: String
)
