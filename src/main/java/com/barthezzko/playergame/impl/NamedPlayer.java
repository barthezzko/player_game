package com.barthezzko.playergame.impl;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.barthezzko.playergame.model.Listener;
import com.barthezzko.playergame.model.Message;
import com.barthezzko.playergame.model.Publisher;

public class NamedPlayer implements Listener {

	private final String name;
	private final Publisher publisher;
	private AtomicInteger messageCounter = new AtomicInteger(0);
	private static final int THRESHOLD = 10;
	private Logger logger = Logger.getLogger(NamedPlayer.class);
	
	
	public NamedPlayer(Publisher publisher, String name) {
		this.publisher = publisher;
		this.name = name;
	}

	@Override
	public void onMessage(Message msg) {
		if (isActive()){
			Message reversed = msg.reverseAndAppend(String.valueOf(messageCounter.getAndIncrement()));
			logger.info("[" + name +"] : " + msg + ", received: " + messageCount());
			publisher.publish(reversed);
		} else {
			logger.info("[" + name +"] stopped sending back messages");
		}
		
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isActive() {
		return messageCounter.get() < THRESHOLD;
	}

	
	public int messageCount() {
		return messageCounter.get();
	}
}
