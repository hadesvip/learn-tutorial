package com.kevin.service.impl;

import com.google.common.collect.Lists;
import com.kevin.domain.OrderInfo;
import com.kevin.service.IOrderService;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单服务
 *
 * @author wangyong
 */
public class OrderServiceImpl implements IOrderService {

  private static final Logger logger = LoggerFactory.getLogger(IOrderService.class);

  @Override
  public List<OrderInfo> getOrderList() {
    List<OrderInfo> orderInfoList = Lists.newArrayList();

    OrderInfo orderInfo = null;

    for (int i = 0; i < 100; i++) {
      orderInfo =
          new OrderInfo(i + 1, String.valueOf(i * 2), "1",
              new Date());
      orderInfoList.add(orderInfo);
    }

    return orderInfoList;
  }
}
