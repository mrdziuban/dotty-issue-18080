package database

import database.queries.Wrapper

trait MyTypeClass[A]

object MyTypeClass {
  implicit def wrapper[A]: MyTypeClass[Wrapper[A]] = new MyTypeClass[Wrapper[A]] {}
}
