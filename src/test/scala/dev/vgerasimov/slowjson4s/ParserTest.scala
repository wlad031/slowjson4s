package dev.vgerasimov.slowjson4s

import org.scalacheck.Prop.*
import dev.vgerasimov.slowparse.POut.Success
import dev.vgerasimov.slowparse.POut.Failure

class ParserTest extends munit.ScalaCheckSuite:

  test("parser should parse some randomly generated JSON [1]") {
    val s = """{
      "ago": [
        true,
        [
          -181185423,
          true,
          511809567,
          -812639609,
          false,
          -776000012
        ],
        "customs",
        "equal",
        [
          236562473.09021592,
          "actual",
          false,
          "light",
          true,
          true
        ],
        4772093
      ],
      "higher": false,
      "composed": -1191714991.0433655,
      "hollow": true,
      "voice": -1870552424,
      "proper": false
    }
    """.mkString
    val expected =
      JObject(
        Map(
          "proper"   -> JBoolean(false),
          "higher"   -> JBoolean(false),
          "voice"    -> JNumber(-1870552424),
          "composed" -> JNumber(-1191714991.0433655),
          "ago" -> JArray(
            List(
              JBoolean(true),
              JArray(
                List(
                  JNumber(-181185423),
                  JBoolean(true),
                  JNumber(511809567),
                  JNumber(-812639609),
                  JBoolean(false),
                  JNumber(-776000012)
                )
              ),
              JString("customs"),
              JString("equal"),
              JArray(
                List(
                  JNumber(236562473.09021592),
                  JString("actual"),
                  JBoolean(false),
                  JString("light"),
                  JBoolean(true),
                  JBoolean(true)
                )
              ),
              JNumber(4772093)
            )
          ),
          "hollow" -> JBoolean(true)
        )
      )
    parser(s) match
      case Success(value, _, _, _) =>
        assertEquals(value, expected)
      case Failure(message, _) => fail(s"Failed to parse string: $message")
  }
