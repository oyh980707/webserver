# webserver
创建一个简单的webserver项目

## 过程记录

1.  创建主类WebServer测试浏览器通过地址访问服务端，检查是否连接正常并通过Socket读取客户端发过来的内容，将其输出到控制台进行查看实际上客户端发过来的内容是一个标准的HTTP请求内容

2.  在core包中添加一个类：ClientHandler,使用线程处理客户端交互。
   
    在WebServer中当接受一个客户端连接后，启动线程处理该客户端请求
   
    在ClientHandler中添加一个readLine方法实现读取一行字符长的操作（以CRLF结尾的一行字符串），因为一个请求中的请求行与消息头的共同特点都是以CRLF结尾的一行字符串

3.  1:创建一个新的包，在这个包中存放所有与HTTP相关处理的类

    2:在http包中定义一个类；HttpRequest，该类的每一个实例用于保存客户端发过来的一个具体的请求内容
	
	3:在HttpRequest中定义构造方法，用来解析一个请求，并保存在对应的属性上
	
	4:在ClientHandler中实例化HttpRequest，用来解析请求。
	
	在处理请求行时，要注意对空请求的处理，当遇到空请求时，对外抛出一个EmptyRequestException,直接抛给ClientHandler。
    所以我们在http包中定义一个自定义异常：EmptyRequestException，一旦ClientHandler在实例化HttpRequest时遇到了空请求异常，就
    不再做后续处理，直接与用户断开连接即可。
    
4.  进一步解析Request，将消息头部分解析出来
    
    1：在HttpRequest中定义属性：headers，他是一个Map类型的。其中key保存消息头的名字，value保存消息头的值
    
    2：在parseHeaders方法中解析所有的消息头并保存到headers中