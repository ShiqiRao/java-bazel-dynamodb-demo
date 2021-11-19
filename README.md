# java-bazel-dynamodb-demo

# 配置相关
创建IAM账号以及其他Setup工作详见：https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/getting-started.html

# DynamoDB本地部署
参考链接：https://docs.aws.amazon.com/zh_cn/amazondynamodb/latest/developerguide/DynamoDBLocal.DownloadingAndRunning.html

# 安装AWS CLI
pip3 install awscli

# AWS 配置
```text
aws configure
```

# 使用方法
```shell
bazel build //...
./bazel-bin/src/main/java/com/example/demo/app
```


