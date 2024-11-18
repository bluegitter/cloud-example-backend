## 部署安装说明

1. 修改nacos目录下的yml配置文件，主要修改数据源信息（默认使用mysql）为你们项目中实际使用数据源信息

2. 在注册中心（nacos）上建立与nacos目录下相同名称的yml文件

3. 拷贝route.json内容到系统管理-动态路由最后，加入动态路由信息

4. 服务启动命令中添加启动参数：-Djasypt.encryptor.password=CLOUDX@tellhow.2022,因为bootstrap.yml中的nacos密码为加密密码，需要使用密钥解密# cloud-example-backend
