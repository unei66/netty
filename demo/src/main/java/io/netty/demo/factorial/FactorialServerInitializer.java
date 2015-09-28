package io.netty.demo.factorial;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.ssl.SslContext;

public class FactorialServerInitializer extends ChannelInitializer<SocketChannel>{

	private final SslContext sslContext;
	
	public FactorialServerInitializer(SslContext context) {
		this.sslContext=context;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline=ch.pipeline();
		if(sslContext!=null)
			pipeline.addLast(sslContext.newHandler(ch.alloc()));
		
		pipeline.addLast(ZlibCodecFactory.newZlibEncoder(ZlibWrapper.GZIP));
		pipeline.addLast(ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP));
		
		pipeline.addLast(new BigIntegerDecoder());
		pipeline.addLast(new NumberEncoder());
		
		pipeline.addLast(new FactorialServerHandler());
	}

}
