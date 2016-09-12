package com.jerry.bedpad.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import com.jerry.bedpad.constant.Constant;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Jerry on 2016/8/15.
 */
public class TcpUtil {

    /**
     * 通信接口
     */
    private Socket mSocket;

    private static TcpUtil mInstance;


    /**
     * 上线指令接口
     */
    public static final byte[] ON_LINE_CODE = new byte[]{0x55, 0x50, 0x00, 0x00, 0x00, 0x55, 0x5f};
    /**
     * 结束指令
     */
    public static final byte[] END_CODE = new byte[]{0x55, 0x50, 0x05, 0x06, (byte) 0xf5, 0x55, 0x5f};

    /**
     * 白名单
     */
    private static final byte WHITE_LIST = 0x02;
    /**
     * 开始广播
     */
    private static final byte START_BROADCAST = 0x04;
    /**
     * 发送广播
     */
    private static final byte START_BROADCAST_SEND = 0x00;
    /**
     * 解绑地址
     */
    private static final byte UNBIND_ADDRESS = 0x10;
    /**
     * 绑定地址
     */
    private static final byte BIND_ADDRESS = 0x16;
    /**
     * 特性发现
     */
    private static final byte CHARACTER_FOUND = 0x1A;
    /**
     * 写入配对设备
     */
    private static final byte WRITE_PAIRED_DEVICE = 0x20;
    /**
     * 写入失败
     */
    private static final byte WRITE_FAILURE = 0x23;
    /**
     * 加载配对设备
     */
    private static final byte LOAD_PAIRED_DEVICE = 0x24;
    /**
     * Notify
     */
    private static final byte NOTIFY = 0x25;
    /**
     * 加载失败
     */
    private static final byte LOAD_FAILURE = 0x27;
    /**
     * 蜂鸣控制
     */
    private static final byte BUZZ_CONTROL = (byte) 0x81;

    /**
     * 超时时间
     */
    private static final int TIME_OUT = 10 * 1000;

    /**
     * 每隔60s检测连接是否断开
     */
    private static final int KEEP_ALIVE_TIME_OUT = 60 * 1000;

    private Context mContext;

    public interface OnCheckClose {
        void checkClose(boolean isClose);
    }

    private OnCheckClose mOnCheckClose;

    public void setOnCheckClose(OnCheckClose onCheckClose) {
        mOnCheckClose = onCheckClose;
    }

    private TcpUtil(Context context) {
        mContext = context;
    }


    /**
     * 每过10s检测一次是否连接已经断开
     */
    private Handler mHandler = new Handler();
    private Runnable mKeepAlive = new Runnable() {

        @Override
        public void run() {
            try {
                L.i("检测服务是否连接。。。");
                if (mOnCheckClose != null) {
                    if (isServerClose()) {
                        L.i("服务连接已经断开。。。");
                        mOnCheckClose.checkClose(true);
                        if (mSocket != null) {
                            mSocket.close();
                        }
                        mHandler.removeCallbacks(mKeepAlive);
                        L.i("重新连接。。。");
                        connect();
                    } else {
                        L.i("服务连接正常。。。");
                        mOnCheckClose.checkClose(false);
                        mHandler.postDelayed(this, KEEP_ALIVE_TIME_OUT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 获取协议解析单例
     *
     * @return
     */
    public static TcpUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (TcpUtil.class) {
                if (mInstance == null) {
                    mInstance = new TcpUtil(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * TCP连接
     */
    public void connect() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //服务端IP地址和端口号
                    L.i("正在连接..." + Constant.TEMPERATURE_SERVER_IP + ":" + Constant.TEMPERATURE_SERVER_PORT);
                    mSocket = new Socket(Constant.TEMPERATURE_SERVER_IP, Constant.TEMPERATURE_SERVER_PORT);
                    //开启保持活动状态的套接字
                    mSocket.setKeepAlive(true);
                    //设置超时时间（设置超时时间会导致无法在超过时间之后接受消息，报异常）
                    //   mSocket.setSoTimeout(TIME_OUT);
                    L.i("已连接...");


                    // 发送上线消息
                    send(mSocket, addMac(TcpUtil.ON_LINE_CODE));
                    L.i("发送上线信息：" + printHexToString(TcpUtil.ON_LINE_CODE, TcpUtil.ON_LINE_CODE.length));

                    // 获得输入流
                    InputStream is = mSocket.getInputStream();

                    // 不断循环是否有输入流
                    while (true) {
                        byte[] buffer = new byte[1024];

                        int length = is.read(buffer);

                        String result = printHexToString(buffer, length);
                        L.i("接收到消息：" + result);
                        parseResult(buffer, length);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        mHandler.postDelayed(mKeepAlive, KEEP_ALIVE_TIME_OUT); //每隔10执行
    }

    /**
     * 上线信息添加MAC地址
     *
     * @param onLineCode
     * @return
     */
    private byte[] addMac(byte[] onLineCode) {
        byte[] code = new byte[13];
        String[] mac = getMacAddress().split(":");
        for (int i = 0; i < onLineCode.length - 2; i++) {
            code[i] = onLineCode[i];
        }
        for (int i = 5; i <= 10; i++) {
            code[i] = (byte) Integer.parseInt((mac[i - 5]), 16);
        }
        code[11] = ON_LINE_CODE[5];
        code[12] = ON_LINE_CODE[6];
        return code;
    }


    /**
     * 获取设备的MAC地址
     *
     * @return
     */
    public String getMacAddress() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();

        return info.getMacAddress();
    }


    /**
     * 向服务端发送消息
     *
     * @param socket
     * @param buffer
     * @throws IOException
     */
    private void send(Socket socket, byte[] buffer) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        // 发送上线信息
        outputStream.write(buffer);
        String result = printHexToString(buffer, buffer.length);
    }


    /**
     * 将16进制byte数组转成字符串
     *
     * @param buffer 字节数组
     * @param length 有效长度
     */
    public String printHexToString(byte[] buffer, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(buffer[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase() + " ");
        }
        return sb.toString();
    }


    /**
     * 解析服务端返回的数据
     *
     * @param buffer 数据
     * @param length 有效长度
     */
    private void parseResult(byte[] buffer, int length) {
        if (!(buffer[0] == 0x55 && buffer[1] == 0x50 && buffer[length - 2] == 0x55 && buffer[length - 1] == 0x5f)) {
            L.i("内容不符合");
            return;
        } else {
            if (length != 7) {
                L.i("长度不符合，有粘包");
            }
            // 根据不同的指令进行分类
            switch (buffer[2]) {
                /**
                 * 启动广播监听
                 */
                case START_BROADCAST:
                    // 发送一次广播请求
                    if (buffer[3] == START_BROADCAST_SEND) {
                        // 拼接报文内容，并发送
                        generatedMessage();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 向服务器回传广播
     */
    private void generatedMessage() {

        try {
            if (Constant.TEMPERATURE_DEVICE != null) {

                int receiveLength = Constant.TEMPERATURE_DEVICE.getReceive().length + 9;
                int length = receiveLength + 6;
                /***********************
                 * 构建数据，发送设备蓝牙数据
                 ************************/
                byte[] message = new byte[length];
                // 报文开头
                message[0] = 0x55;
                message[1] = 0x50;
                // 消息地址
                message[2] = 0x04;
                // 数据长度
                message[3] = (byte) receiveLength;
                /**
                 * 构建data数据（0位：信号强度，1位：是否开启白名单，2位：MAC类型，3~8位：MAC地址，其它：数据）
                 */
                // 信号强度
                message[4] = (byte) Math.abs(Constant.TEMPERATURE_DEVICE.getRssi());
                // 是否开启白名单
                message[5] = 0x01;
                // MAC类型
                message[6] = (byte) Constant.TEMPERATURE_DEVICE.getMacType();
                // MAC地址
                String[] mac = Constant.TEMPERATURE_DEVICE.getAddress().split(":");
                for (int i = 7; i <= 12; i++) {
                    message[i] = (byte) Integer.parseInt((mac[i - 7]), 16);
                }
                // data数据
                for (int i = 13; i <= length - 3; i++) {
                    message[i] = Constant.TEMPERATURE_DEVICE.getReceive()[i - 13];
                }
                // 报文结尾
                message[length - 2] = 0x55;
                message[length - 1] = 0x5f;
                /************************
                 * 数据构造完毕
                 ***********************/

                // 发送报文
                send(mSocket, message);
                L.i("发送数据：" + printHexToString(message, message.length));
                // 发送结束信息
                send(mSocket, TcpUtil.END_CODE);
                L.i("发送结束码：" + printHexToString(TcpUtil.END_CODE, TcpUtil.END_CODE.length));
            } else {
                // 如果没有数据也要发送结束码
                // 发送结束信息
                send(mSocket, TcpUtil.END_CODE);
                L.i("发送结束码：" + printHexToString(TcpUtil.END_CODE, TcpUtil.END_CODE.length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否断开连接，断开返回true,没有返回false
     *
     * @return
     */
    public Boolean isServerClose() {
        try {
            mSocket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }


    /**
     * 关闭TCP连接
     */
    public void close() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mHandler.removeCallbacks(mKeepAlive);
                    mSocket.close();
                    L.i("TCP连接已关闭");
                } catch (Exception e) {
                    L.i("TCP连接关闭异常");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean isClose() {
        return mSocket.isClosed();
    }

    public boolean isConnected() {
        return mSocket.isConnected();
    }
}
