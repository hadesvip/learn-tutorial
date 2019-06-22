package com.kevin.activemq.runner;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;
import java.util.Optional;

/**
 * activeMQ主题测试
 * @author: wangyong
 * @date: 2019/6/22 16:38
 */
public class TopicTests {

	private static final String ACTIVEMQ_URL = "tcp://118.24.16.109:61616";

	private static final String TOPIC_NAME = "TOPIC_NAME";

	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";


	@Test
	public void produceTest() throws JMSException {

		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
				ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Topic topic = session.createTopic(TOPIC_NAME);
		MessageProducer producer = session.createProducer(topic);

		for (int i = 1; i < 6; i++) {
			TextMessage textMessage = session.createTextMessage(String.format("发送消息第%s条", i));

			//消息属性
			textMessage.setStringProperty("sender", "kevin");

			//非持久化
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.send(textMessage);
		}

		System.out.println("消息发送成功");
		producer.close();
		session.close();
		connection.close();
	}


	/**
	 * 消费者
	 * @param args
	 * @throws JMSException
	 */
	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic topic = session.createTopic(TOPIC_NAME);
		MessageConsumer consumer = session.createConsumer(topic);
		consumer.setMessageListener(message -> {
			TextMessage textMessage = (TextMessage) message;
			Optional.ofNullable(textMessage).ifPresent(text -> {
				try {
					String msg = String.format("消息ID:%s,消息内容:%s,消息发送人:%s", text.getJMSMessageID(), text.getText(),
							text.getStringProperty("sender"));
					System.out.println(msg);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			});
		});

		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}

}
