
sealed  trait DomainError {
  val msg: String
}

object DomainError {
  type Result[A] = Either[DomainError, A]
  final case class ParsingError(msg: String) extends DomainError
  final case class DecodingError(msg: String) extends DomainError
}

