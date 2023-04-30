题目1分别实现了两个版本:

# 版本1: NioServer.java
利用Nio实现, 在mac上底层会使用kqueue, 在linux上底层会使用epoll的LT mode.
本地测试`MainTest.java`受限于单机socket数量现实, 无法模拟百万级别连接, 只创建了1w个连接测试.


# 版本2: NettyServer.java
利用Netty实现, 指定底层直接使用epoll. Netty实现默认使用ET mode, 但是在mac下无法测试, 运行会报如下错误, 需要在linux环境下运行:
...
Caused by: java.lang.IllegalStateException: Only supported on Linux
at io.netty.channel.epoll.Native.loadNativeLibrary(Native.java:317)
at io.netty.channel.epoll.Native.<clinit>(Native.java:85)
... 14 more

