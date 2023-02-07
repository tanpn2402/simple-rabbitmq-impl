# simple-rabbitmq
A simple RabbitMQ sample with Java to implement the Stock Trading API.\
This project use RabbitMQ as:
1. A pub/sub: the way of communication between services (APIService and StockService)
2. A queue: Limit update DB request/second with queue


## 1. Setup RabbitMQ
```sh
$ cd ./rabbitmq && docker-compose up -d
```