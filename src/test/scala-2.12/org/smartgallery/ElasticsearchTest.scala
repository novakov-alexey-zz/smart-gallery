package org.smartgallery

import com.sksamuel.elastic4s.http.ElasticDsl
import org.scalatest.{AsyncFlatSpec, Matchers}

class ElasticsearchTest extends AsyncFlatSpec with Matchers with ElasticDsl {

  it should "insert to Elasticsearch" in {
    //given
    val concepts = List("dog", "animal", "family", "hund", "friend")
    val path = "some path"
    //when
    val insert = Elasticsearch.insert(path, concepts)
    //then
    insert flatMap { _ =>
        Elasticsearch.searchFor("dog")
          .map(l => {
            l.head.right.get.path should be(path)
          })
    }
  }
}
