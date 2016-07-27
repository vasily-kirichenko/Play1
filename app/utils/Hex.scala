package utils

object Hex {
  def arrayToHexString(buf: Array[Byte]): String = buf.map("%02X" format _).mkString

  def hexStringToByteArray(s : String) = {
    val len = s.length()
    val buff = new Array[Byte](len / 2)

    for (i <- 0 until len by 2) {
      val b = (Character.digit(s charAt i, 16) << 4) + Character.digit(s charAt (i + 1), 16)
      buff(i / 2) = b.asInstanceOf[Byte]
    }

    buff
  }
}