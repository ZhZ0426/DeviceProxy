package com.zyl.coder;

import com.zyl.common.Message;
import com.zyl.common.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ProxyMessageEncoder extends MessageToByteEncoder<Message> {
  @Override
  protected void encode(
      ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf)
      throws Exception {
    byteBuf.writeByte(message.getType());
    byteBuf.writeInt(message.getDataLength());
    byteBuf.writeInt(message.getSignLength());
    if (message.getSignData() != null) {
      byteBuf.writeBytes(message.getSignData());
    }
    byteBuf.writeBytes(message.getData());
  }
}
