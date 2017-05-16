package com.github.wreulicke.socket.section1

object Resources {

  def withClosable[T <: AutoCloseable](resource: T)(r: T => Unit): Unit = {
    try {
      r(resource)
    } finally {
      resource.close()
    }
  }
}
