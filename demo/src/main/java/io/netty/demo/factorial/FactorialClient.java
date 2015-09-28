package io.netty.demo.factorial;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class FactorialClient {
	static final int COUNT=1000;
	static final String HOST="";
	static final int PORT=8322;
	static final boolean SSL=System.getProperty("ssl")!=null;
	
	public static void main(String[] args) throws Exception{
		final SslContext sslCtx;
		
		if(SSL){
			sslCtx=SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
		}else{
			sslCtx=null;
		}
		
		EventLoopGroup group=new NioEventLoopGroup();
		try{
			Bootstrap b=new Bootstrap();
			b.group(group);
			b.channel(NioSocketChannel.class);
			b.handler(new FactorialClientInitializer(sslCtx));
			
			ChannelFuture f=b.connect(HOST,PORT).sync();
			FactorialClientHandler handler=(FactorialClientHandler)f.channel().pipeline().last();
			
			System.err.format("Factorial of %,d is:%,d",COUNT,handler.getFactorial());
		}finally{
			group.shutdownGracefully();
		}
	}
}
