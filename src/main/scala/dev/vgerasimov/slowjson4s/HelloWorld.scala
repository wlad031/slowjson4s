package dev.vgerasimov.slowjson4s

import dev.vgerasimov.slowparse.POut.*
import dev.vgerasimov.slowparse.*
import dev.vgerasimov.slowparse.Parsers.given
import dev.vgerasimov.slowparse.Parsers.*

import java.nio.file.{ Files, Path, Paths }
import java.io.PrintWriter

object Hello:
  def apply(s: String): String = s"Hello $s!"

sealed trait Json
case object JNull extends Json
case class JBoolean(v: Boolean) extends Json
case class JNumber(v: Double) extends Json
case class JString(v: String) extends Json
case class JArray(v: List[Json]) extends Json
case class JObject(v: Map[String, Json]) extends Json

val pNull: P[JNull.type] = P("null").map(_ => JNull)
val pBool: P[JBoolean] = (P("true").! | P("false").!).map(_.toBoolean).map(JBoolean(_))
val pNum: P[JNumber] =
  (anyFrom("+-").? ~ d.+ ~ (P(".") ~ d.*).? ~ (anyFrom("Ee") ~ anyFrom("+-").? ~ d.*).?).!.map(_.toDouble)
    .map(JNumber(_))
val pStr: P[JString] = (P("\"") ~ until(!P("\\") ~ P("\""), (P("\\\"") | anyChar)).! ~ (!P("\\") ~ P("\""))).map(JString(_))
val pChoice: P[Json] = P(choice(pNull, pBool, pNum, pStr, pArr, pObj))
val pArr: P[JArray] = P("[") ~~ pChoice.rep(sep = Some(ws0 ~ P(",") ~ ws0)).map(JArray(_)) ~~ P("]")
val pObj: P[JObject] =
  val pair: P[(String, Json)] = pStr.map(_.v) ~~ P(":") ~~ pChoice
  val pairs: P[List[(String, Json)]] = pair.rep(sep = Some(ws0 ~ P(",") ~ ws0))
  P("{") ~~ pairs.map(_.toMap).map(JObject(_)) ~~ P("}")

val json: P[Json] = P(pObj | pArr)

// @main 
def run = 
  println(Hello("world"))
  val pathStr = "/Users/vgerasimov/Projects/test.json"
  val path = Paths.get(pathStr)
  val content = Files.readString(path)
  // val content = """{ "hello": "w\"or\"ld" }"""
  val cur = System.currentTimeMillis()
  val res = json(content)
  println(s"Time: ${System.currentTimeMillis() - cur}ms")
  // val res = pStr(""""w\"or\"ld"""")
  println(res)

@main
def run1 = 
  val pathStr = "/Users/vgerasimov/Projects/img.ppm"
  val path = Paths.get(pathStr)
  val pw = new PrintWriter(path.toFile())
  pw.write("""|P3
              |# feep.ppm
              |48 48
              |15
              | 0  0  0    0  0  0    0  0  0   15  0 15
              | 0  0  0    0 15  7    0  0  0    0  0  0
              | 0  0  0    0  0  0    0 15  7    0  0  0
              |15  0 15    0  0  0    0  0  0    0  0  0
              | """.stripMargin)
  pw.close()
  println("finished")