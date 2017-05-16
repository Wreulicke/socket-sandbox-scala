package com.github.wreulicke.socket.section1

import java.net.Socket
import java.nio.file.{Files, Paths}

import com.github.wreulicke.socket.section1.Resources.withClosable

import scala.io.Source

class TcpClient(host: String, port: Int) {
  def open() {
    withClosable(new Socket(host, port)) {
      socket => {
        val dest = socket.getOutputStream
        withClosable(Files.newInputStream(Paths.get("client_send.txt"))) {
          src =>
            Source.fromInputStream(src).takeWhile(_ != -1).foreach(dest.write(_))
        }
        dest.write(0)
        val src = socket.getInputStream
        withClosable(Files.newOutputStream(Paths.get("client_recv.txt"))) {
          dest =>
            Source.fromInputStream(src).takeWhile(_ != 0).foreach(dest.write(_))
        }
      }
    }
  }
}

object TcpClient extends App {
  new TcpClient("localhost", 8001).open()
}
