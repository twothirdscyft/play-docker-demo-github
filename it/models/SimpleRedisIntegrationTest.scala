package models

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class SimpleRedisIntegrationTest extends Specification {
  "SimpleRedis" should {
    "generate random data" in new WithApplication {
      val alphabet = List('j', 'i', 'h', 'g', 'f',
                          'e', 'd', 'c', 'b', 'a')
      
                          
      val length = SimpleRedis.generateData("test")
      
      val expected = alphabet.takeRight(length).map(_.toString)
      
      val result = SimpleRedis.getData("test")
      result must beSome(expected)
    }
    
    "get nothing on bad key" in new WithApplication {
      val length = SimpleRedis.generateData("test")
      val result = SimpleRedis.getData("blah")
      result must beNone
    }
  }
}