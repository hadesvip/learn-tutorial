package com.kevin.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.kevin.domain.OrderInfo;
import com.kevin.service.IOrderService;
import com.kevin.service.impl.OrderServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 抓取数据->执行处理->抓取数据.... 适用于不间歇的数据
 *
 * @author wangyong
 */
public class MyDataFlow implements DataflowJob<OrderInfo> {

  private IOrderService orderService = new OrderServiceImpl();


  @Override
  public List<OrderInfo> fetchData(ShardingContext shardingContext) {
    List<OrderInfo> orderList =
        orderService.getOrderList().stream().filter(
            orderInfo -> orderInfo.getOrderId() % shardingContext.getShardingTotalCount()
                == shardingContext.getShardingItem()).collect(Collectors.toList());

    Optional<List<OrderInfo>> orderListOptional = Optional.of(orderList);
    orderList = orderListOptional.get().subList(0, 20);
    return orderList;
  }

  @Override
  public void processData(ShardingContext shardingContext, List<OrderInfo> data) {
    LocalDateTime now = LocalDateTime.now();
    System.out.println(now + ":" + "正在处理数据");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    data.forEach(orderInfo -> {
      System.out.println("开始处理订单" + orderInfo);
      System.out.println("订单:" + orderInfo.getOrderId() + "处理完成.");
    });
  }
}
