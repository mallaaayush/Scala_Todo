package  services;
import com.google.inject.Inject
import models.{Todo, Todos}
import scala.concurrent.Future


class TodoService @Inject() (todos: Todos) {

    def addTodo(todo: Todo): Future[String] = {
        println("This is TodoService")
       
      todos.add(todo)
    }

    def deleteTodo(id: Long): Future[Int] = {
      todos.delete(id)
    }

    def getTodo(id: Long): Future[Seq[Todo]] = {
      todos.get(id)
    }

    def listAlltodos: Future[Seq[Todo]] = {
      todos.listAll
    }
    
    // def updateTodo(id: Long ):Future[Int] = {
    //     todos.update(id)
    // }
     def modifyTodo(todo: Todo): Future[String] = {
        println("This is TodoService")
       
      todos.update(todo)
    }
  
}
