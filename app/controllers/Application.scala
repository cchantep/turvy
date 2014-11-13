package controllers

import scala.concurrent.Future

import play.api.mvc.{ Action, Controller, Result }
import play.api.libs.json.Json

import eu.europa.ec.CheckVatBindings

object Application extends Controller {
  import play.api.libs.concurrent.Execution.Implicits._

  def index = Assets.at("/public", "index.html")

  /*
   Returns `{"valid":false}` or `{"valid":true,"name":"COMPANY NAME","address":"COMPANY ADDRESS","requestDate":"0000-00-00Z"}
   */
  def checkVatNumber(countryCode: String, number: String) =
    Action async { req =>
      vatService.checkVat(countryCode, number) map { resp =>
        if (!resp.valid) Ok(Json.obj("valid" -> false))
        else Ok(Json.obj("valid" -> resp.valid, "name" -> resp.name,
          "address" -> resp.address, "requestDate" -> resp.requestDate.
            normalize().toString()))
      } map { _.withHeaders("Access-Control-Allow-Origin" -> "*") }
    }

  lazy val vatService = (new CheckVatBindings with scalaxb.Soap11ClientsAsync with scalaxb.DispatchHttpClientsAsync {}).service
}
