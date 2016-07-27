package utils

object Implicits {
  implicit def hexStringToByteArray(s: String) : Array[Byte] = Hex.hexStringToByteArray(s)
}