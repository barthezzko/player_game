package com.barthezzko.playergame;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.barthezzko.playergame.model.Message;
import com.barthezzko.playergame.model.PlayGround;
import com.barthezzko.playergame.multithread.MultiThreadedPlayGroundImpl;

public class MultiThreadedGameTests {

	private PlayGround playGround;

	@Before
	public void before() {
		playGround = new MultiThreadedPlayGroundImpl();
	}

	@Test
	public void testSimple() {
		playGround.initialize("start");
		List<Message> messages = playGround.getMessages();
		assertEquals("start112233445566778899", messages.get(messages.size() - 1).getPayload());
	}

}
