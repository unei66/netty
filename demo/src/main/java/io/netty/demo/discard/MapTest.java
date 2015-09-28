package io.netty.demo.discard;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapTest {

	@Test
	public void test(){
		Map<Integer,Integer> map=new ConcurrentHashMap<Integer,Integer>();
		for(int i=0;i<1000;i++)
			map.put(i, i);
		
	}
}
