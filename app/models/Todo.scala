package models

import com.google.inject.Inject
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import play.api.data.validation.Constraints._
import slick.jdbc.MySQLProfile.api._
import scala.concurrent.{ExecutionContext, Future}

case class Todo(id : Long, title: String, description: String)
case class TodoFormData(title : String, description:String)


object TodoForm {

    val form = Form(
        mapping(
            "title" ->  nonEmptyText,
            "description" -> nonEmptyText
        )(TodoFormData.apply)(TodoFormData.unapply)
    )
}
class TodoTableDef(tag: Tag) extends Table[Todo](tag, "user") {

  def id = column[Long]("id", O.PrimaryKey,O.AutoInc)
  def title = column[String]("title")
  def description = column[String]("description")
  
  override def * =
    (id,title,description) <>(Todo.tupled, Todo.unapply)
}

class Todos @Inject() (protected val dbConfigProvider : DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile]{

  val todos = TableQuery[TodoTableDef]
def add(todo: Todo): Future[String] = {
  println("Here is the list ")
  println(todos)
    dbConfig.db.run(todos += todo).map(res => "Todo successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Long): Future[Int] = {
    dbConfig.db.run(todos.filter(_.id === id).delete)
  }

  def get(id: Long): Future[Seq[Todo]] = {
    dbConfig.db.run(todos.filter(_.id === id).result)
  }

  def listAll: Future[Seq[Todo]] = {
   dbConfig.db.run(todos.result)
  }

 def update(todo: Todo): Future[String] = {
    dbConfig.db.run(todos.filter(_.id === todo.id).update(todo)).map(res => "Success").recover {
        case ex : Exception => ex.getCause.getMessage
    }
  }

}

