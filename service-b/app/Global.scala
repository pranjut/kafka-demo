

import Services.KafkaConsumer
import com.google.inject.Inject
import play.api.Application

class Global @Inject()(app: Application) {

    println("<<<<<<<<<<In consumerRun>>>>>>>>>>")
    val topic = "demo_topic1"
    val groupId = "group-1"
    val consumer = new KafkaConsumer(topic, groupId, "localhost:2181")

    while (true) {
      consumer.read() match {
        case Some(message) =>
          println("Getting message.......................  " + message)
          // wait for 100 milli second for another read
          //Thread.sleep(100)
        case None =>
          println("Queue is empty.......................  ")
          // wait for 2 second
          Thread.sleep(2 * 1000)
      }
    }
}