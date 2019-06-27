package com.kevin.activemq.runner;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 持久化主题测试
 * @author: wangyong
 * @date: 2019/6/22 18:39
 */
public class PersistentTopicTests {


	private final static String ACTIVEMQ_URL = "tcp://118.24.16.109:61616";

	private final static String PERSISTENT_TOPIC_NAME = "PERSISTENT_TOPIC";

	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";


	/**
	 * 生产者
	 */
	@Test
	public void produceTest() throws JMSException {

		MessageProducer producer = null;
		Session session = null;
		Connection connection = null;

		try {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
					ACTIVEMQ_URL);
			connection = activeMQConnectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(PERSISTENT_TOPIC_NAME);
			producer = session.createProducer(topic);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);

			connection.start();

			for (int i = 1; i < 101; i++) {
				TextMessage textMessage = session.createTextMessage(String.format("持久化消息内容第%s条", i));
				producer.send(textMessage);
			}

			System.out.println("消息发送成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			producer.close();
			session.close();
			connection.close();
		}
	}


	/**
	 * 订阅
	 * @param args
	 */
	public static void main(String[] args) throws JMSException {

		Session session = null;
		Connection connection = null;
		TopicSubscriber topicSubscriber = null;
		try {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
					ACTIVEMQ_URL);
			connection = activeMQConnectionFactory.createConnection();
			connection.setClientID("kevin");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(PERSISTENT_TOPIC_NAME);
			topicSubscriber = session.createDurableSubscriber(topic, "持久化主题订阅");

			connection.start();
			Message receive = topicSubscriber.receive();
			while (receive != null) {
				TextMessage textMessage = (TextMessage) receive;
				String msg = String.format("消息ID:%s,消息内容%s", textMessage.getJMSMessageID(), textMessage.getText());
				System.out.println(msg);
				receive = topicSubscriber.receive(1000L);
			}


		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			topicSubscriber.close();
			session.close();
			connection.close();
		}


	}


}
