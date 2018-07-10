### 项目的参考资料与文档

* [Git官网中文文档](https://git-scm.com/book/zh/v2)
* [在线工具 - 程序员的工具箱](http://tool.lu/)

###### 前端

* [angular.js API](https://docs.angularjs.org/api)
* [AngularJS-Learning](https://github.com/jmcunningham/AngularJS-Learning/blob/master/ZH-CN.md)
* [bootstrap3在线手册](http://v3.bootcss.com/css/)
    * [bootstrap在线表单构造器](http://www.bootcss.com/p/bootstrap-form-builder/)
    * [bootstrap日期时间选择器](http://www.bootcss.com/p/bootstrap-datetimepicker/)
* [bootstrap table在线手册](http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/)
* [zTree API文档](http://www.treejs.cn/v3/api.php)
* [font-awesome](http://www.bootcss.com/p/font-awesome/#whats-new)
* ECharts3.x [文档](http://echarts.baidu.com/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts) [例子](http://echarts.baidu.com/examples.html)
* 百度地图 [DEMO](http://lbsyun.baidu.com/jsdemo.htm)

###### 后端

* [Spring Boot教程](https://gitee.com/didispace/SpringBoot-Learning)
* [跟我学SpringMVC](http://jinnianshilongnian.iteye.com/blog/1752171)
* [Freemarker在线手册](http://freemarker.foofun.cn/)
* [跟我学Shiro](http://jinnianshilongnian.iteye.com/blog/2018398)

###### [sn-core](http://218.65.179.55:8080/tfs/DefaultCollection/%E7%B3%BB%E7%BB%9F%E5%9F%BA%E7%A1%80%E6%A1%86%E6%9E%B6)

* 下载项目源码

        git clone http://218.65.179.55:8080/tfs/DefaultCollection/_git/%E7%B3%BB%E7%BB%9F%E5%9F%BA%E7%A1%80%E6%A1%86%E6%9E%B6

* 在IDEA中导入项目

    参照：[IDEA 导入多个Module，多个Module在同一个Project 下显示](http://blog.csdn.net/yyym520/article/details/77527976)

###### Shiro 权限注解

**注意：** 你需要根据自己的业务添加shiro注解，实现请求控制。

目前对**用户**和**授权**部分添加了** @RequiresRoles("admin") **注解，防止部分无聊的朋友修改数据！

* 控制层的权限注解如下：

    > @RequiresAuthentication
    
    表示当前Subject已经通过login进行了身份验证；即Subject. isAuthenticated()返回true。
    
    > @RequiresUser
    
    表示当前Subject已经身份验证或者通过记住我登录的。
    
    > @RequiresGuest
    
    表示当前Subject没有身份验证或通过记住我登录过，即是游客身份。
    
    > @RequiresRoles(value={"admin", "user"}, logical= Logical.AND)
    
    表示当前Subject需要角色admin和user。
    
    > @RequiresPermissions(value={"user:a", "user:b"}, logical= Logical.OR)
    
    表示当前Subject需要权限user:a或user:b。
   
* 页面freemarker模板的权限标签如下：

    > <@shiro.hasPermission name="user:a"></@shiro.hasPermission>
    
    表示当前Subject需要权限user:a，标签内的内容才能显示
    
    > <@shiro.lacksPermission name="user:a"></@shiro.lacksPermission>
        
    表示当前Subject没有权限user:a，标签内的内容才能显示
    
    > <@shiro.hasRole name="admin"></@shiro.hasRole>
            
    表示当前Subject需要角色admin，标签内的内容才能显示
    
    > <@shiro.lacksRole name="admin"></@shiro.lacksRole>
                
    表示当前Subject没有角色admin，标签内的内容才能显示
    

###### 后台校验的使用方法

* 在实体中为需要校验的字段添加注解，如：项目信息实体 [ProjectInfo](src\main\java\com\sn\framework\module\project\domain\ProjectInfo.java)

        public class ProjectInfo {
            @NotBlank(message = "项目名不能为空")
            private String projectName;
        }
        
    * 校验注解说明：
        
    | 约束注解名称              | 约束注解说明                                                       |
    |---------------------------|--------------------------------------------------------------------|
    | @Null                     | 验证对象是否为空                                                   |
    | @NotNull                  | 验证对象是否为非空                                                 |
    | @NotEmpty                 | 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0） |
    | @NotBlank                 | 验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格 |
    | @AssertTrue               | 验证 boolean 对象是否为 true                                       |
    | @AssertFalse              | 验证 boolean 对象是否为 false                                      |
    | @Max(value)               | 验证 number 和 string 对象是否小等于指定的值                       |
    | @Min(value)               | 验证 number 和 string 对象是否大等于指定的值                       |
    | @DecimalMax(value)        | 限制必须为一个不大于指定值的数字                                   |
    | @DecimalMin(value)        | 验证 number 和 string 对象是否大等于指定的值，小数存在精度         |
    | @Digits(integer,fraction) | 限制必须为一个不小于指定值的数字                                   |
    | @Size(max,min)            | 验证对象（array,collection,map,string）长度是否在给定的范围之内    |
    | @Past                     | 验证 date 和 calendar 对象是否在当前时间之前                       |
    | @Future                   | 验证 date 和 calendar 对象是否在当前时间之后                       |
    | @Pattern(value)           | 验证 string 对象是否符合正则表达式的规则                           |
    | @Email                    | 验证邮箱                                                           |

* 校验信息的国际化配置
    
    * 在[ValidationMessages.properties](src\main\resources\ValidationMessages.properties)中添加校验信息：
    
            project.name.notBlank=项目名不能为空
    
    * 实体的校验消息需要改为配置文件的索引值：
    
            public class ProjectInfo {
                @NotBlank(message = "{project.name.notBlank}")
                private String projectName;
            }
    
        
* 在控制器中为需要要紧的方法添加校验的注解，如：[ProjectController](src\main\java\com\sn\framework\module\project\controller\ProjectController.java)

        @RequestMapping(name = "新增项目", path = "", method = RequestMethod.POST)
        @ResponseStatus(value = HttpStatus.CREATED)
        public void save(@Validated @RequestBody ProjectDto projectDto) {
            projectDto.setProjectStatus("2");
            projectService.create(projectDto);
        }
    
    * [参考资料](http://build.iteye.com/blog/2320555)


###### [odata](http://www.odata.org/documentation/) 的使用说明

* 什么是 OData 协议?

    OData 全名"开放数据协议(Open Data Protocol)", 是一个用于 web 的数据访问协议. OData 提供了一个统一的 CRUD (create, read, update, and delete) 操作来查询和维护数据集.

* OData 查询使用说明：

    * 使用 [$.toOdata](src\main\resources\static\app\common\common.odata.js) 的方式：

            $http.get("获取数据的URL", {
                params: $.toOdata({
                    filter: [
                        {field: "fieldName1", operator: "eq", value: "text"}, 
                        {field: "fieldName2", operator: "contains", value: "text"}, {
                            logic: "or"
                            filters: [
                                {field: "fieldName3", operator: "neq", value: "text"},
                                {field: "fieldName4", operator: "isnull"}
                            ]
                        }
                    ],
                    sort: [{ field: "fieldName1", dir: "asc" }, { field: "fieldName2", dir: "desc" }],
                    take: 10,
                    skip: 5
                })
            }).success(funciton(data) {
                console.log(data);
            });
    
        或者使用直接写参数的方式：
        
            $http.get("获取数据的URL", {
                params: {
                    "$filter": "fieldName1 eq 'text' and substringof('text',fieldName2) and (fieldName3 ne 'text' or fieldName4 eq null)",
                    "$orderby": "fieldName1 asc, fieldName2 desc",
                    "$top": 10,
                    "$skip": 5,
                    "$inlinecount": "allpages"
                }
            }).success(funciton(data) {
                console.log(data);
            });
    
    * 参数 **$filter** 为过滤条件，格式说明：
    
            fieldName1 eq 'text' and substringof('text',fieldName2) and (fieldName3 ne 'text' or fieldName4 eq null)
    
        后台会转换为：
        
            select * from tableName where fieldName1='text' and fieldName2 like '%text%' and (fieldName2<>'text' or fieldName4 is null)
    
    *  **$filter** 比较操作符说明：
            
        | 操作符 |  描述 |  列子 | 
        |--------|-------|-------|
        | eq     | 等于 (Equal) | genre eq 'Fantasy' |
        | ne     | 不等于 (Not equal) | author ne 'Kevin Kelly' |
        | gt     | 大于 (Greater than) | price gt 20 |
        | ge     | 大于等于 (Greater than or equal) | price ge 10 |
        | lt     | 小于 (Less than)	| price lt 20 |
        | le     |小于等于 (Less than or equal) | price le 100 |
        | contains | 包含 | contains('Alfreds',fieldName) |
        | substringof | 包含 | substringof('Alfreds',fieldName) |
        | indexof | 不包含 | indexof('Alfreds',fieldName) eq -1 |
        | endswith | 结束于 | endswith('Alfreds',fieldName) |
        | startswith | 开始于 | startswith('Alfreds',fieldName) |
            
    *  **$filter** 逻辑操作符说明：
        
        | 操作符 |  描述 |  列子 | 
        |--------|-------|-------|
        | and    |	逻辑与 (Logical and) | Price le 200 and Price gt 3.5 |
        | or     |	逻辑与 (Logical or) | Price le 200 or Price gt 3.5 |
                
    * 参数 **$orderby** 为排序字段及方式，排序的字段与对应查询的实体属性一致，格式为
    
            fieldName1 asc, fieldName2 desc
    
    * 参数 **$top** 与 **$skip** 为分页条件
    
    * 参数 **$inlinecount** 为是否统计数据的总数

###### 关于前端代码的注意事项

* **$http.delete** 在IE8下不能正常使用，应改为 **$http["delete"]** ，或者

        $http({
            method: 'delete',
            url: '/someUrl'
        })
        
###### 使用uglifyjs压缩JS

* 在命令行安装 uglifyjs 插件（必要条件是，电脑中已安装 node.js ）

        npm install uglify-js -g
    
* 从命令行进入到需要压缩的 js 文件所在的目录，然后再执行压缩目录，如下：

        uglifyjs bootstrap-table.js -m -o bootstrap-table.min.js
    

    