import org.junit.runner._
import org.scalatest.{FlatSpec, WordSpec}
import org.scalatest.junit.JUnitRunner
import play.api.test._
import play.api.test.Helpers._
import play.test.WithApplication

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
//@RunWith(classOf[JUnitRunner])
//class ApplicationSpec extends WordSpec {
//
//  "Application" should {
//
//    "send 404 on a bad request" in new WithApplication{
//      route(FakeRequest(GET, "/boum")) must beNone
//    }
//
//    "render the index page" in new WithApplication{
//      val home = route(FakeRequest(GET, "/")).get
//
//      status(home) must equalTo(OK)
//      contentType(home) must beSome.which(_ == "text/html")
//      contentAsString(home) must contain ("Your new application is ready.")
//    }
//  }
//}