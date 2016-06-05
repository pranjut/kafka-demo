
package Services

/**
  * Created by arpit on 18/5/16.
  */

import java.util.Properties

import kafka.consumer.{Consumer, ConsumerConfig, ConsumerTimeoutException, Whitelist}
import kafka.serializer.DefaultDecoder


class KafkaConsumer(topic: String, groupId: String, zookeeperConnect: String) {


  private val props = new Properties()

  props.put("group.id", groupId)
  props.put("zookeeper.connect", zookeeperConnect)
  props.put("auto.offset.reset", "smallest")
  props.put("consumer.timeout.ms", "500")
  props.put("auto.commit.interval.ms", "500")

  private val config = new ConsumerConfig(props)
  private val connector = Consumer.create(config)
  private val filterSpec = new Whitelist(topic)
  private val streams = connector.createMessageStreamsByFilter(filterSpec, 1, new DefaultDecoder(), new DefaultDecoder())(0)

  lazy val iterator = streams.iterator()

  def read() =
    try {
      if (hasNext) {
        println("Getting message from queue.............")
        val message = iterator.next().message()
        Some(new String(message))
      } else {
        None
      }
    } catch {
      case ex: Throwable =>
        ex.printStackTrace()
        None
    }

  private def hasNext(): Boolean =
    try
      iterator.hasNext()
    catch {
      case timeOutEx: ConsumerTimeoutException =>
        false
      case ex: Throwable =>
        println("Getting error when reading message ")
        false
    }

  def close(): Unit = connector.shutdown()

}