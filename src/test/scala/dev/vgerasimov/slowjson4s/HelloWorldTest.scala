package dev.vgerasimov.slowjson4s

import org.scalacheck.Prop.*

class HelloWorldTest extends munit.ScalaCheckSuite:

  test("Hello should return correct string") {
    forAll { (s: String) =>
      {
        val hello = Hello(s)
        assertEquals(hello, s"Hello $s!")
      }
    }
  }
