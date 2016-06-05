import javax.inject.Inject

import play.api.Application
import services.KafkaProducer

class Global @Inject()(app: Application) {

    println("<<<<<<<<<<In producerRun>>>>>>>>>>")
    val topic = "demo_topic1"

    val producer = new KafkaProducer("localhost:9092")

    // batch sending
    val batchSize = 50

    (1 to 1000000).toList.map(no => "Message " + no).grouped(batchSize).foreach { message =>
      producer.send(topic, message)
    }

}