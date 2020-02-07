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
    
5.  实现用户请求一个页面，服务端找到该资源后响应客户端

        浏览器中输入请求的资源路径通常如下：
        http://localhost:8088/myweb/index.html
        
        其中：
        http://localhost:8088
        是用来指定服务端的地址信息
        /myweb/index.html
        是指定向请求的资源路径，而资源路径是一个相对路径，相对的位置可以有我们服务端来指定
        由于我们将所有web应用全部放在了webapps目录中，那么我们就可以指定相对的就是webapps目录
        所以用户请求/myweb/index.html时，我们就去webapps中找到对应子目录及资源：webapps/myweb/index.html
        
    1：在ClientHandler中完成处理客户端请求的工作
    分析request中的url部分。然后从webapps目录中中安到对应的资源
    
    2：若找到资源后，直接通过socket获取输出流，将浏览器请求的资源
        以一个标准的响应格式发送回去
    
    注：在页面index.heml中加入一张图片，测试可知，当一个页面中包含其他素材时
        浏览器会发起多次请求，当得到所有素材后才会将该页面完整显示出来。

6.  创建相应对象完成相应客户端具体内容操作
    
    1：在http包中定义HttpResponse，使用该类的每一个实例表示一个具体要给用户端发送的响应

    2：在HttpResponse中定义相关相应的各个属性

    3：提供flush方法，用于将该响应发送给客户端

7.  完成用户请求不同资源时可以根据其类型分返回正确的响应头
    
        问题：
        由于现在HttpResponse的sendHeaders中发送的Content-Type时
        发送固定值：text/html,这就导致无论客户端请求什么资源服务端
        都告知浏览器该资源是一个html页面，那么浏览器就无法正确的理解其请求的资源
        导致显示出现异常现象
    
    1:在HttpRewponse中添加响应头对应的属性
    	Map<String,String> headers;
    	Key:响应头的名字,value:响应头对应的值
    	
    2:对于发送响应头Content-Type与Content-Length的操作，不能为必须发送
    	因为这两个头是告知浏览器该响应包含一个响应正文内容，他的类型是什么，长度是多少。
    	如果一个响应中不含有响应正文时，那么响应头中就不应当包含这两项
    	而HttpResponse我们提供一个方法：setEntity，用来让外界
    	向当前响应对象中设置要给客户端相应的文件（响应正文内容），
    	这时，我们才应当在响应中包含上述两个头，所以在setEntity方法中我们可以根据响应的文件
    	对应的设置着两个头到headers这个Map中。
    	
    3:修改sendHeaders方法，遍历headers这个Map，该Map中有几个头信息，就发送几个。
    	
    注：由于不同文件有对应的介质类型定义，w3c有对其规定。也可直接参考tomcat安装目录下conf目录中的web.xml，其中包含大量的
    	<mime-mapping>标签，每个标签定义了一种介质类型
    	我们要在HttpContext中将这些介质类型包含进来，以便于在响应时可以根据对应的资源正确响应其类型
    	
8.  将HttpContext中初始化介质类型的方法：initMimeMapping方法修改
    
    为通过解析config/web.xml文件中所有的<mime-mapping>标签来初始化
    这样我们可以支持所有介质类型

9.  完成处理注册用户的业务操作，通过本版本，我们要了解浏览器是如何提交用户数据的,并且服务端是如何解析提交数据并如何进行业务处理的。
    
    1：准备注册页面
    	在web/myweb中添加注册页面reg.html
      
    2:当页面的form表示以GET（地址栏形式）形式提交时，所有用户输入的信息会被拼在请求路径的后面，这是url中以？分割为两部分前面的请求部分，后面的参数部分
    	如：http://localhost:8088/myweb/reg.html?username=name&password=123456&passwordtoo=123456&nickname=jeck&age=20
     	我们在服务端解析请求中请求行的url部分会得到
     	/myweb/reg.html?username=name&password=123456&passwordtoo=123456&nickname=jeck&age=20
      	为此，url可能出现的两种状况为：带参数，不带参数
      	带参数如：/myweb/reg.html?username=name&password=123456&passwordtoo=123456&nickname=jeck&age=20
      	那么针对带参数的url，我们要将请求与参数部分进一步处理
      
      	在HttpRequest中定义相关属性
      	1：String requestUrI，用于保存url中的请求部分
      	2：String queryString，用于保存url中的参数部分
      	3：Map parameters，用于保存每一个参数
    
    3： 在parseRequestLine解析请求行时，要对url进行更进一步解析
    	所以单独定义一个parseUrl方法，在解析请求行时得到url后调用这个方法进一步解析。 	
      	
     4:新建一个包：servlet
     	再该包中定义用于处理用户注册的业务类：RegServlet
     	并实现service方法，用来处理注册业务
     	
10. 完成处理用户登录的业务操作
    
    1：准备登录页面，在web/myweb下创建login.html页面中form表单action指定路径为action="login"
    	from表单中应当包含两个输入框，一个输入用户名，一个输入密码
    	
    2：在ClientHander添加一个分支，判断请求路径是否为请求登录业务，
    	如果是，则实例化LoginServlet，并调用service方法处理登录
    	
    3：在servlet包中定义LoginServlet并定义service方法。
    
    4：service方法中完成登录业务
    	4.1：通过request获取表单中提交的用户名及密码
    	4.2：使用RandomAccessFile读取user.dat文件，比较每条记录中是否有与提交的用户名，密码匹配的记录
    	4.3：若找到匹配记录，则跳转登陆成功页面：login_success.html
    	4.4；若没有找到匹配记录则跳转失败页面：login_fail.html
    	
11. 问题：当我们在页面form表单输入框中输入中文信息时，若该表单以GET形式提交（地址栏形式提交），表单内容会被拼接在URL中）
    这时会发现服务端在接受是所有的中文都已若干“%xx”的形式出现
    
    原因：HTTP协议要求，客户端发送给服务端的请求行与消息头等处的字符只能是ISO8859-1编码内容，这意味着本身是不能直接发送中文的
    
    解决方法通常为：
    1：先将中文按照对应字符集装换成一组字节
    2：再将每个字节以2位16进制形式表示，前面添加一个%，所以是"%xx"这样三个字符的组合形式表示一个字节的数据
    3：再将这些“%xx”的内容拼接到URL的相应位置传递给服务端，而服务端在读取到后，应当将每个“%xx”的内容对应转换为字节
    	再将这些字节按照对应字符集还原字符串即可
    
    例如：
    在浏览器输入框中输入了一个汉字："范"
    那么当提交表单时，由于URL中不得直接出现中文，所以浏览器会先将“范”这个字按照对应字符集（谷歌浏览器默认按照UTF-8》将其
    转换成三个字节，UTF-8中的“范”对应的三个字节分别为：
    二进制	10进制	16进制
    11101000 -24	E8
    10001100 -116	8C
    10000011 -125	83
    然后将这三个字节中的每个字节都以2位16进制形式表示：
    E8 8C 83
    再将每个字节对应的这两位16进制前面加上"%"：
    %E8%8C%83
    以这样的形式表示“范”，然后凭借在URL中并传递给服务端
    
    所以当URL中包含中文时，原始URL如果为：
    http://localhost:8088/myweb/login?username=范&password=123....
    那么实际URL传递时为：
    http://localhost:8088/myweb/login?username=%E8%8C%83&password=123....
    	
    服务端将对应的%xx这样的内容还原为字符串可以直接使用Java提供的API:URLDecoder即可
    	
    修改HttpRequest,在进一步解析URL时，将参数部分使用URLDecoder转换一下，
    将所有%xx的内容还原为对应文字，这样我们就可以支持浏览器传中文了

12. 在servlet中抽象一个HttpServlet，作为所有Servlet的超类
    一是可以规定，要求所有Servlet必须包含有service方法用于处理业务
    二是可以将这些Servlet跳转页面的代码公用，后期可以将其他公用代码定义到超类中	

