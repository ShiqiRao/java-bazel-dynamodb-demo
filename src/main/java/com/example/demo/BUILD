load("@rules_java//java:defs.bzl", "java_binary", "java_library")

java_library(
    name="java-maven-lib",
    srcs = glob(["**/*.java"]),
    deps = [
        "@maven//:com_amazonaws_aws_java_sdk",
        "@maven//:com_amazonaws_aws_java_sdk_dynamodb",
        "@maven//:com_amazonaws_aws_java_sdk_core",
        ],
)

java_binary(
    name = "app",
    main_class = "com.example.demo.App",
    runtime_deps=[":java-maven-lib"],
)

