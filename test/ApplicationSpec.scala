import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification
with Mockito {
  "Application" should {

    "send 404 on a bad request" in new WithApplication{
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication{
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain ("Your new application is ready.")
    }
    
    "reject non-alphabet key for data generation" in new WithApplication{
      val result = route(FakeRequest(POST, "/gen").withFormUrlEncodedBody("key" -> "blah3a")).get
      
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain ("Password is not all letters")
    }
    
    "reject bad characters in key for data get" in new WithApplication{
      val result = route(FakeRequest(GET, "/gen").withFormUrlEncodedBody("key" -> "#@$")).get
      
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain ("errors")
    }
    
    "reject escaped bad characters key for data get" in new WithApplication{
      val result = route(FakeRequest(GET, "/gen").withFormUrlEncodedBody("key" -> "a\\a")).get
      
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain ("errors")
    }
    
    "reject short key for data generation" in new WithApplication{
      val result = route(FakeRequest(POST, "/gen").withFormUrlEncodedBody("key" -> "a")).get
      
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain ("errors")
    }
    
    "reject long key for data generation" in new WithApplication{
      val result = route(FakeRequest(POST, "/gen").withFormUrlEncodedBody("key" -> "fjadlfjsalfjlkdsfjljfksdflksdlfjdslfs")).get
      
      status(result) must equalTo(BAD_REQUEST)
      contentType(result) must beSome.which(_ == "application/json")
      contentAsString(result) must contain ("errors")
    }
  }
}
