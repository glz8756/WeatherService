import DomainError.ParsingError
import sttp.client3.{HttpURLConnectionBackend, UriContext, basicRequest}
import io.circe.parser.parse
import model.WeatherData.{WeatherOneCall, WeatherReport}
import io.circe.generic.auto._

object WeatherDataLive {
  val url = "https://api.openweathermap.org/data/2.5/onecall"
  val appid = "eddd436740603d84077121844d3e63b0"

  def getWeatherData(lat: Double, lon: Double): Either[DomainError, WeatherReport] = {
    val backend = HttpURLConnectionBackend()
    val response = basicRequest
      .get(uri"$url?appid=$appid&lat=$lat&lon=$lon&units=imperial")
      .send(backend)
    val wjson = response.body match {
      case Left(error) => Left(ParsingError(error))
      case Right(rbody) => parse(rbody).fold(pf => Left(ParsingError(pf.message)), Right.apply)
    }
    val weatherOneCallData = for {
      json <- wjson
      weatherOneCall <- json.as[WeatherOneCall]
    } yield weatherOneCall
    val report = weatherOneCallData match {
      case Left(_) => Left(ParsingError("Parsing Error"))
      case Right(data) => Right(parseWeatherData(data))
    }
    report
  }

  def parseWeatherData(data: WeatherOneCall): WeatherReport = {
    val descriptions = data.current.weather.map { w => w.description }
    val temp = data.current.temp match {
      case t if (t < 40) => "cold"
      case t if (t > 80) => "hot"
      case _ => "moderate"
    }
    val isAlert = data.alerts.isDefined
    val alertDetail = data.alerts
    WeatherReport(descriptions, temp, isAlert, alertDetail)
  }
}
