package service

import com.sksamuel.elastic4s.http.ElasticDsl
import org.scalatest.{AsyncFlatSpec, Matchers}

class ElasticsearchTest extends AsyncFlatSpec with Matchers with ElasticDsl {

  it should "insert and then find that data from Elasticsearch" in {
    //given
    val concepts = List("dog", "animal", "family", "nature", "friend")
    val path = "some path"
    //when
    val insert = Elasticsearch.insert(path, concepts)
    //then
    insert flatMap { _ =>
      Elasticsearch.searchFor("dog").map(_.head.path should be(path))
    }
  }
}
