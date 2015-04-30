package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation._
import play.api.data.Forms._
import play.api.libs.json._

import models.DataGenerator

case class GenData(key:String)

trait UsingRedis {
  val generator: DataGenerator = models.SimpleRedis
}

object Application extends Controller 
with UsingRedis {
  val notAllLetters = """(?!^[a-zA-Z]+$)^.+$""".r
  val keyCheckConstraint: Constraint[String] = Constraint("constraints.keycheck")({
    plainText =>
      val errors = plainText match {
        case notAllLetters() => Seq(ValidationError("Password is not all letters"))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })
  val genDataForm = Form(
    mapping(
      "key" -> nonEmptyText(minLength = 3, maxLength = 10)
                .verifying(keyCheckConstraint)
    )(GenData.apply)(GenData.unapply)
  )
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def generateData = Action {
    Ok(views.html.generateData(genDataForm, genDataForm))
  }
  
  def genDataReq(block: GenData => Result)(implicit request: Request[_]): Result = {
    genDataForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(Json.obj("errors" -> formWithErrors.errorsAsJson))
      },
      genData => block(genData)
    )
  }
  
  def genData = Action { implicit request =>
    genDataReq { genData =>
      val key = genData.key
      val size = generator.generateData(key)
      Ok(Json.obj("size" -> size))
    }
  }
  
  def getData = Action { implicit request =>
    genDataReq { genData =>
      val key = genData.key
      generator.getData(key) match {
        case Some(results) => Ok(Json.obj("results" -> results))
        case None => BadRequest(Json.obj("errors" -> Map(
          "key" -> Seq("No values using '" + key + "'")))
        )
      }
    }
  }
}