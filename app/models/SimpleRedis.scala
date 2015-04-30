package models

trait DataGenerator {
  def generateData(key: String): Int
  def getData(key: String): Option[List[String]]
}

object SimpleRedis extends DataGenerator {
  import play.api.Play.current
  import com.typesafe.plugin.RedisPlugin
  import scala.math
  
  private val alphabet = List('a', 'b', 'c', 'd', 'e',
                              'f', 'g', 'h', 'i', 'j')
                              
  private def randInt(m: Int, n: Int): Int = {
    math.floor(math.random * (n - m)).toInt + m
  }
  
  override def generateData(key: String): Int = {
    val pool = current.plugin[RedisPlugin].get.sedisPool
    pool.withClient { client =>
      val length = randInt(1, 10)
      client.del(key)
      (0 until length).foreach { i =>
        client.lpush(key, alphabet(i).toString)
      }
      
      length
    }
  }
  
  override def getData(key: String): Option[List[String]] = {
    val pool = current.plugin[RedisPlugin].get.sedisPool
    pool.withClient { client =>
      if (client.exists(key)) {
        val length = client.llen(key)
        Some(client.lrange(key, 0, length))
      }
      else None
    }
  }
}