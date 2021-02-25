# exunion

[![build](https://github.com/Robothy/exunion/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/Robothy/exunion/actions/workflows/build.yml)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8f2a3cdd2123424babc2a1d5e2806e01)](https://www.codacy.com/manual/robothyluo/exunion?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Robothy/exunion&amp;utm_campaign=Badge_Grade)
[![codecov](https://codecov.io/gh/Robothy/exunion/branch/master/graph/badge.svg?token=OVxYsn9I2j)](https://codecov.io/gh/Robothy/exunion)

exunion is a Java lib that aggregate the main cryptocurrency exchanges' API and provide uniform interfaces. 
You can easily apply your excellent quantitative trading program on different cryptocurrency exchanges based on this library.

## Installation

todo

## Usages

`ExchangeServiceProvider` is the core API of exunion, it takes the exchange service generation responsibility. 
You can get most services' instance through `ExchangeServiceProvider.newInstance(exchange, serviceClazz, options)`. 

The `newInstance` method has three parameters:

+ `exchange`  the exchange that provide the service.
+ `serviceClazz`  the exchange service clazz. For example: DepthService.class
+ `options`  the options to initialize the exchange service instance.

Here are more concrete details and samples about the usages of exunion. 

### Account

```java
```

### Market Data

```java
```

## Contribution

todo