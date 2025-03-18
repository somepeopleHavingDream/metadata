// 这是一个 Nomad 的作业（Job）配置文件，用于部署一个名为 xxx 的服务
job "xxx" {
  // 作业基本信息

  // 指定数据中心
  datacenters = ["dc1"]
  // 指定命名空间
  namespace = "xxx"
  // 指定节点池
  node_pool = "xxx"

  // 任务组（ Group ）
  group "xxx-group" {
    // 指定任务组的实例数量
    count = 1

    // 添加约束条件，限制任务组仅运行在 hostname 为 xxx 的节点上
    constraint {
      // ${meta.hostname} 是 Nomad 的变量插值语法，用于动态获取节点的 hostname
      attribute = "${meta.hostname}"
      value     = "xxx"
    }

    // 网络配置
    network {
      // 定义容器端口映射，将宿主机的 8270 端口映射到容器内的 8270 端口
      port "xxx" {
        static = 8270
        to = 8270
      }
    }

    // 卷（Volume）配置
    volume "shared-logs" {
      // 卷类型为 host，表示使用主机上的卷
      type = "host"
      // 卷名称，需要在 Nomad client 配置中提前定义
      source = "shared-logs"
      // 卷是否只读，这里是 false，表示可读写
      read_only = false
    }

    // 任务（Task）
    task "xxx-task" {
      // 指定任务使用的驱动，这里是 docker，表示任务将运行在 Docker 容器中
      driver = "docker"

      // Docker 配置
      config {
        // 指定 Docker 镜像地址
        image = "nexus.xxx.xxx/xxx/xxx:7670b1c086969dfb8282cf658aedc1d05a4d889e"
        // 配置日志驱动和日志选项
        logging {
          // 对应 Docker 的日志驱动
          type = "json-file"
          config = {
            // 对应 Docker 的日志选项
            "max-size" = "500m"
            // 可选：日志轮转文件数
            "max-file" = "3"
          }
        }
        // 设置为 true，表示每次启动时强制拉取最新镜像
        force_pull = true
        // 指定使用的端口
        ports = ["xxx"]
      }

      // 卷挂载
      volume_mount {
        // 挂载的卷名称
        volume = "shared-logs"
        // 挂载到容器内的路径
        destination = "/logs"
        // 是否只读
        read_only = false
      }

      // 环境变量
      env {
        TZ                 = "Asia/Shanghai"
        SPRING_PROFILES_ACTIVE = "dev"
        ENV = "dev"
        JAVA_OPTS          = "-Xmx512m -Xms512m"
        MYSQL_ADDR         = "172.23.177.158:3306"
        MYSQL_USERNAME     = "root"
        MYSQL_PASSWORD     = "Lama@20210609D#"
        REDIS_HOST         = "172.23.177.158"
        REDIS_PORT         = "6379"
        REDIS_PASSWORD     = "yoki123"
        COMMON_REDIS_HOST  = "43.134.222.242"
        COMMON_REDIS_PORT  = "6379"
        COMMON_REDIS_PASSWORD = "lama123"
        REDIS_READ_ONLY_PASSWORD = "yoki123"
        MQ_ADDR                         = "172.23.177.158:9876"
        NACOS_SERVER_ADDR  = "172.23.177.158:8848"
        CLICKHOUSE_ADDR    = "clickhouse:8123"
        CLICKHOUSE_USERNAME = "default"
        CLICKHOUSE_PASSWORD = "lama1314"
        CLICKHOUSE_URL = "jdbc:clickhouse://172.23.177.158:8123/default"
        KEEWIDB_HOST       = "172.23.177.158"
        KEEWIDB_PORT       = "6379"
        KEEWIDB_PASSWORD   = "yoki123"
        DATACENTER_ID      = 1
        WORKER_ID          = 1
      }

      // 模板
      template {
        // 模板内容，动态生成 NACOS_SERVICE_IP 环境变量
        data = <<EOF
                NACOS_SERVICE_IP={{ env "NOMAD_IP_weparty_gift" }}
                EOF
        // 模板生成的文件路径
        destination = "local/env.txt"
        // 将模板内容作为环境变量
        env         = true
        // 模板变更时的处理模式，这里是 noop，表示不处理变更
        change_mode = "noop"
      }

      // 资源限制
      resources {
        // CPU 资源限制，单位是 MHz
        cpu    = 600
        // 内存资源限制，单位是 MB
        memory = 2048
      }
    }
  }
}
