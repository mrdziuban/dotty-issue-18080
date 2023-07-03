package database.models

import database.queries._

case class Client(name: String)

object Client {
  implicit val parseClient: DBParse[Client] =
    new DBParse[Client] {
      def apply(rs: ResultSet) = Client(rs.getV[String])
    }
}
