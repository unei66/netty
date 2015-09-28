package io.netty.demo.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {
	private int port;
	public DiscardServer(int port){
		this.port=port;
	}
	
	public void run() throws Exception{
		//accepts an incoming connection
		EventLoopGroup bossGroup=new NioEventLoopGroup();	//step 1
		//handles the traffic of the accepted connection once the boss 
		//accepts the connection and registers the accepted connection to the worker
		EventLoopGroup workerGroup=new NioEventLoopGroup(); //step 2
		
		try{
			//ServerBootstrap is a helper class that sets up a server. 
			//You can set up the server using a Channel directly. 
			//However, please note that this is a tedious process, and you do not need to do that in most cases.
			ServerBootstrap b=new ServerBootstrap(); //step 3
			b.group(bossGroup,workerGroup) //step 4
			//Here, we specify to use the NioServerSocketChannel class 
			//which is used to instantiate a new Channel to accept incoming connections.
			.channel(NioServerSocketChannel.class) //step 5
			.childHandler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new DiscardServerHandler());
				}
				
			}) //6
			//Did you notice option() and childOption()? option() is for the NioServerSocketChannel 
			//that accepts incoming connections. childOption() is for the Channels accepted by the 
			//parent ServerChannel, which is NioServerSocketChannel in this case
			.option(ChannelOption.SO_BACKLOG, 128) //step 7
			.childOption(ChannelOption.SO_KEEPALIVE, true); //step 8
			
			//Bind and start to accept incoming connections
			ChannelFuture f=b.bind(port).sync(); //step 9
			
			//Wait until the server socket is closed.
			//In this example,this does not happen,but you can do that to gracefully
			//shut down your server
			f.channel() //step 10
			.closeFuture() //step 11
			.sync(); //step 12
		}finally{
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		int port=10001;
		new DiscardServer(port).run();
	}
}
