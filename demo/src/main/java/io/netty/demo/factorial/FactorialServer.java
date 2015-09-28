package io.netty.demo.factorial;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class FactorialServer {
	static final boolean SSL=System.getProperty("ssl")!=null;
	static final int PORT=8322;
	
	public static void main(String[] args) throws Exception{
		final SslContext sslCtx;
		if(SSL){
			SelfSignedCertificate ssc=new SelfSignedCertificate();
			sslCtx=SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		}else{
			sslCtx=null;
		}
		
		EventLoopGroup bossGroup=new NioEventLoopGroup(10);
		EventLoopGroup workerGroup=new NioEventLoopGroup();
		
		try {
			ServerBootstrap b=new ServerBootstrap();
			b.group(bossGroup, workerGroup);
			b.channel(NioServerSocketChannel.class);
			b.handler(new LoggingHandler(LogLevel.INFO));
			b.childHandler(new FactorialServerInitializer(sslCtx));
			b.bind(PORT).sync().channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
