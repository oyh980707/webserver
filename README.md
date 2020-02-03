# webserver
创建一个简单的webserver项目

## 过程记录
1. 创建主类WebServer测试浏览器通过地址访问服务端，检查是否连接正常并通过Socket读取客户端发过来的内容，将其输出到控制台进行查看实际上客户端发过来的内容是一个标准的HTTP请求内容

2. 在core包中添加一个类：ClientHandler,使用线程处理客户端交互。
   
   在WebServer中当接受一个客户端连接后，启动线程处理该客户端请求
   
   在ClientHandler中添加一个readLine方法实现读取一行字符长的操作（以CRLF结尾的一行字符串），因为一个请求中的请求行与消息头的共同特点都是以CRLF结尾的一行字符串