package com.zyl.common;

/**
 * 自定义数据类
 */
public class Message {
    /**
     * 数据包类型
     */
    private byte type;
    /**
     * 传输的数据
     */
    private byte[] data;
    /**
     * 传输数据的长度
     */
    private int dataLength;
    /**
     * 自定义数据长度
     */
    private int signLength;
    /**
     * 自定义数据
     */
    private byte[] signData;

    public int getSignLength() {
        return signLength;
    }

    public void setSignLength(int signLength) {
        this.signLength = signLength;
    }

    public byte[] getSignData() {
        return signData;
    }

    public void setSignData(byte[] signData) {
        this.signData = signData;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }
    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
