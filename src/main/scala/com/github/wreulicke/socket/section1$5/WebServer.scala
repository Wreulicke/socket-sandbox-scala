package com.github.wreulicke.socket.section1$5

import java.net.ServerSocket
import java.nio.file.{Files, Paths}
import java.time.format.DateTimeFormatter
import java.time.{ZoneId, ZonedDateTime}

import resource._

import scala.io.Source

class WebServer(port: Int) {

  def start() {
    for (
      server <- managed(new ServerSocket(port));
      socket <- managed(server.accept)) {
      val startLine = Source.fromInputStream(socket.getInputStream).getLines().take(1)
      startLine.next().split(" ") match {
        case Array("GET", path, "HTTP/1.1") =>
          val output = socket.getOutputStream
          writeHeader.foreach(output.write(_))
          for (src <- managed(Source.fromInputStream(Files.newInputStream(Paths.get(".", path))))) {
            src.foreach(output.write(_))
          }
        case _ => sys.error("invalid")
      }
    }
  }

  def writeHeader: String =
    s"""HTTP/1.1 200 OK
       |Date: $now
       |Server: TestWebServer/0.1
       |Connection: close
       |Content-Type: text/html
       |
       |""".stripMargin


  def now: String = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")))

}


object WebServer extends App {
  new WebServer(8001).start()
}
