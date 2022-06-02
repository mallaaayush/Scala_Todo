package controllers

import models.{Todo,TodoForm}
import javax.inject._
import play.api.Logging
import play.api._
import play.api.mvc._
import services.TodoService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, todoService: TodoService) extends AbstractController(cc) with Logging {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action.async { implicit request: Request[AnyContent] =>
    todoService.listAlltodos map {
      todos => 
        println("This is index")
        println(todos)
        println(TodoForm.form)
        Ok(views.html.index(TodoForm.form,todos))
    }
  }

    def addTodo() = Action.async { implicit request: Request[AnyContent] =>
    TodoForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        println("Failure")
        logger.warn(s"Form submission with error: ${errorForm.errors}")
        Future.successful(Ok(views.html.index(errorForm, Seq.empty[Todo])))
      },
      data => {
        println(data)
        val newUser = Todo(0,data.title, data.description)
        println( "Success ")
        todoService.addTodo(newUser).map( _ => Redirect(routes.HomeController.index()))
      })
  }

  def deleteTodo(id: Long) = Action.async { implicit request: Request[AnyContent] =>
    todoService.deleteTodo(id) map { res =>
      Redirect(routes.HomeController.index())
    }
  }

   def about() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.about())
  }

    def updateTodo(id : Long) = Action.async { implicit request : Request[AnyContent] =>
     
      todoService.getTodo(id) map {
        res => println(TodoForm.form)
        println("Ya bhitra chireko ho")
        println(res)
        
        Ok(views.html.update(TodoForm.form,res))

      }
  


}

 def modifyTodo(id : Long) = Action.async { implicit request: Request[AnyContent] =>
    TodoForm.form.bindFromRequest.fold(
      // if any error in submitted data
      errorForm => {
        println("Failure")
        logger.warn(s"Form submission with error: ${errorForm.errors}")
        Future.successful(Ok(views.html.index(errorForm, Seq.empty[Todo])))
      },
      data => {
        println(data)
        println(id)
        val newUser = Todo(id,data.title, data.description)
        println(newUser)
        println( "Success ")
        todoService.modifyTodo(newUser).map( _ => Redirect(routes.HomeController.index()))
      })
  }
}
