package org.jetlinks.community.device.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.jetlinks.community.device.configuration.RedisUtil;

public class NettyTcpServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final RedisUtil redisUtil;

    // 构造函数注入RedisUtil
    public NettyTcpServerChannelInitializer(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast("decoder", new MyDecoder());
        socketChannel.pipeline().addLast("encoder", new MyDecoder());
        socketChannel.pipeline().addLast(new ObjectEncoder());
        socketChannel.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
        socketChannel.pipeline().addLast(new NettyTcpServerHandler(redisUtil));
        ByteBuf delimiter = Unpooled.copiedBuffer("_$".getBytes());
        socketChannel.pipeline().addLast(new ChannelHandler[]{new DelimiterBasedFrameDecoder(10240, false, delimiter)});
        socketChannel.pipeline().addLast(new ChannelHandler[]{new StringDecoder()});
        socketChannel.pipeline().addLast(new ChannelHandler[]{new StringEncoder()});
    }
}
