package com.github.wreulicke.socket.section1

import java.net.ServerSocket
import java.nio.file.{Files, Paths}

import com.github.wreulicke.socket.section1.Resources.withClosable

import scala.io.Source

class TcpServer(port: Int) {

  def open() {
    withClosable(new ServerSocket(port)) { server =>
      val socket = server.accept()
      val src = socket.getInputStream
      withClosable(Files.newOutputStream(Paths.get("server_recv.txt"))) {
        dest =>
          Source.fromInputStream(src).takeWhile(_ != 0).foreach(dest.write(_))
      }
      val dest = socket.getOutputStream
      withClosable(Files.newInputStream(Paths.get("server_send.txt"))) {
        src =>
          Source.fromInputStream(src).takeWhile(_ != -1).foreach(dest.write(_))
      }
      socket.close()
    }
  }
}

object TcpServer extends App {
  new TcpServer(8001).open()
}