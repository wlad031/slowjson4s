package dev.vgerasimov.slowjson4s

import dev.vgerasimov.slowparse.POut.*
import dev.vgerasimov.slowparse.*
import dev.vgerasimov.slowparse.Parsers.given
import dev.vgerasimov.slowparse.Parsers.*

sealed trait Json
case object JNull extends Json
case class JBoolean(v: Boolean) extends Json
case class JNumber(v: Double) extends Json
case class JString(v: String) extends Json
case class JArray(v: List[Json]) extends Json
case class JObject(v: Map[String, Json]) extends Json

private object parsers:
  val pNull: P[JNull.type] = P("null").map(_ => JNull)
  val pBool: P[JBoolean] = (P("true").! | P("false").!)
    .map(_.toBoolean)
    .map(JBoolean(_))
  val pNum: P[JNumber] =
    (anyFrom("+-").? ~ d.+ ~ (P(".") ~ d.*).? ~ (anyFrom("Ee") ~ anyFrom("+-").? ~ d.*).?).!.map(_.toDouble)
      .map(JNumber(_))
  val pStr: P[JString] =
    (P("\"") ~ until(!P("\\") ~ P("\""), (P("\\\"") | anyChar)).! ~ (!P("\\") ~ P("\"")))
      .map(JString(_))
  val pChoice: P[Json] = P(choice(pNull, pBool, pNum, pStr, pArr, pObj))
  val pArr: P[JArray] = P("[") ~~ pChoice
    .rep(sep = Some(ws0 ~ P(",") ~ ws0))
    .map(JArray(_)) ~~ P("]")
  val pObj: P[JObject] =
    val pair: P[(String, Json)] = pStr.map(_.v) ~~ P(":") ~~ pChoice
    val pairs: P[List[(String, Json)]] = pair.rep(sep = Some(ws0 ~ P(",") ~ ws0))
    P("{") ~~ pairs.map(_.toMap).map(JObject(_)) ~~ P("}")

  val json: P[Json] = P(pObj | pArr)

val parser = parsers.json
