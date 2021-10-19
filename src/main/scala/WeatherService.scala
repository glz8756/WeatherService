import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.QueryParamDecoderMatcher
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.blaze.BlazeServerBuilder
import io.circe.generic.auto._
import io.circe.syntax._

object WeatherService extends IOApp {

case class Person(name: String, age: Int)
  object Lat extends QueryParamDecoderMatcher[Double]("lat")
  object Lon extends QueryParamDecoderMatcher[Double]("lon")

  def weatherRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "weather" :? Lat(lat) +& Lon(lon) => {
        val report=  WeatherDataLive.getWeatherData(lat, lon)
        val weatherReport = report match {
          case Right(weather) => Ok(weather.asJson.deepDropNullValues.spaces2)
          case Left(msg) => BadRequest(s"$msg")
        }
        weatherReport
      }
    }
  }

  def allRoutesComplete[F[_] : Monad]: HttpApp[F] = {
    weatherRoutes.orNotFound
  }
  import scala.concurrent.ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(allRoutesComplete)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
