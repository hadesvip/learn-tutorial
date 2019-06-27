package com.kevin.activemq.runner;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;
import java.io.IOException;
import java.util.Optional;

/**
 * activemq测试
 *
 * topic模式：先要启动订阅者后启动生产者，要不然生产者发送的消息都是废消息
 *
 * @author: wangyong
 * @date: 2019/6/22 15:03
 */
public class BasicActiveMQTests {

	private static final String ACTIVEMQ_URL = "tcp://118.24.16.109:61616";

	private static final String QUEUE_NAME = "QUEUE-DEMO";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "root";


	/**
	 * 生产者
	 * @throws JMSException
	 */
	@Test
	public void produceMessage() throws JMSException {

		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
				ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		//队列
		Queue queue = session.createQueue(QUEUE_NAME);

		//生产者
		MessageProducer producer = session.createProducer(queue);

		for (int i = 1; i <= 6; i++) {
			TextMessage textMessage = session.createTextMessage(String.format("发送第%s条消息", i));
			producer.send(textMessage);
		}

		producer.close();
		session.close();
		connection.close();
	}


	/**
	 * 消费者
	 */
	@Test
	public void consumeMessageTest() throws JMSException {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
				ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		//队列
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);
		while (true) {
			TextMessage receive = (TextMessage) consumer.receive(4000l);
			if (receive != null) {
				String text = String.format("消息ID:%s,消息内容:%s", receive.getJMSMessageID(), receive.getText());
				System.out.println(text);
			}

		}
	}


	/**
	 * 使用监听器消费消息
	 */
	@Test
	public void consumeListenerTest() throws JMSException, IOException {

		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
				ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		//队列
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);

		//监听方式消费消息
		consumer.setMessageListener(message -> {
			TextMessage textMessage = (TextMessage) message;

			Optional.ofNullable(textMessage).ifPresent(text -> {

				try {
					String msg = String.format("消息ID:%s,消息内容:%s", text.getJMSMessageID(), text.getText());
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


	public static void main(String[] args) throws JMSException, IOException {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD,
				ACTIVEMQ_URL);
		Connection connection = activeMQConnectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		//队列
		Queue queue = session.createQueue(QUEUE_NAME);
		MessageConsumer consumer = session.createConsumer(queue);

		//监听方式消费消息
		consumer.setMessageListener(message -> {
			TextMessage textMessage = (TextMessage) message;

			Optional.ofNullable(textMessage).ifPresent(text -> {
				try {
					String msg = String.format("消息ID:%s,消息内容:%s", text.getJMSMessageID(), text.getText());
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
