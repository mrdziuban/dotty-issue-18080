package database

import database.models.Client

object queries {
  trait GetValue[A]

  object GetValue {
    implicit def inst[A]: GetValue[A] = new GetValue[A] {}
  }

  final case class ResultSet private[queries] () {
    final def getV[A: GetValue]: A = ???
  }

  trait DBParse[A] {
    def apply(rs: ResultSet): A
  }

  case class AVG(column: Column) {
    def call: String = s"AVG(${column.name})"
  }

  case class Column(name: String)

  sealed trait ClientOwnerId

  object ClientOwnerId {
    case class CompanyId(id: Long, client: Client) extends ClientOwnerId

    private[queries] def parseClientOwnerId[C: DBParse, CID <: ClientOwnerId, D[_], A]: DBParse[D[A]] = ???
  }

  case class Wrapper[+A](companyId: ClientOwnerId.CompanyId, data: A)
}
