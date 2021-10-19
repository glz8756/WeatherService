package model

object WeatherData {

 final case class WeatherReport
  (
    condition: List[String],
    temp: String,
    isWeatherAlertOn: Boolean,
    alerts: Option[List[Alerts]]
  )

 final case class WeatherOneCall
  (
    alerts: Option[List[Alerts]] = None,
    current: Current
  )

 final case class Current
  (
    temp: Double,
    weather: List[Weather]
  )

final case class Weather
  (
    description: String,
    icon: String,
    id: Int,
    main: String
  )

final case class Alerts
  (
    description: String,
    end: Long,
    event: String,
    sender_name: String,
    start: Long,
    tags: List[String]
  )
}
