package com.kevin.service;

import com.kevin.domain.OrderInfo;
import java.util.List;

/**
 * 订单服务
 *
 * @author wangyong
 */
public interface IOrderService {

  List<OrderInfo> getOrderList();

}
