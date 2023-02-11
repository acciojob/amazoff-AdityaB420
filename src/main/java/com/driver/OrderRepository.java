package com.driver;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
@Repository

public class OrderRepository {
    private HashMap<String,Order> orderMap;
    private HashMap<String,DeliveryPartner> deliveryPartnerMap;
    private HashMap<String,String> orderPartnerMap;
    private HashMap<String, HashSet<String>> partnerOrderMap;

    public OrderRepository() {
        this.orderMap = new HashMap<>();
        this.deliveryPartnerMap = new HashMap<>();
        this.orderPartnerMap = new HashMap<>();
        this.partnerOrderMap = new HashMap<>();
    }
    //01

    public void addOrder(Order order){
        orderMap.put(order.getId(),order);

    }
    //02
    public void addPartner(String partnerId){
        deliveryPartnerMap.put(partnerId, new DeliveryPartner(partnerId));

    }
    //03
    public void addOrderPartnerPair(String orderId,String partnerId){
        if(orderMap.containsKey(orderId)&&deliveryPartnerMap.containsKey(partnerId)){
            HashSet<String> orders=new HashSet<>();
            if(partnerOrderMap.containsKey(partnerId)){
                orders=partnerOrderMap.get(partnerId);
            }
            orders.add(orderId);
            partnerOrderMap.put(partnerId,orders);
            DeliveryPartner partner=deliveryPartnerMap.get(partnerId);
            partner.setNumberOfOrders(orders.size());
            deliveryPartnerMap.put(partnerId,partner);
            orderPartnerMap.put(orderId,partnerId);
        }
    }
    //04
    public Order getOrderById(String orderId){
        return orderMap.get(orderId);
    }
    //05
    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerMap.get(partnerId);
    }
    public int getOrderCountByPartnerId(String partnerId){
        return (partnerOrderMap.get(partnerId)).size();
    }
    //08
    public List<String> getAllOrders(){
        List<String> list=new ArrayList<>();
        for(String order:orderMap.keySet()){
            list.add(order);
        }
        return list;
    }
    //12
    public void deletePartnerById(String partnerId){
        HashSet<String> orders=new HashSet<>();
        if(partnerOrderMap.containsKey(partnerId)){
            orders=partnerOrderMap.get(partnerId);
            for(String order:orders){
                if(orderPartnerMap.containsKey(order)){
                    orderPartnerMap.remove(order);
                }
            }
            partnerOrderMap.remove(partnerId);
        }
        if(deliveryPartnerMap.containsKey(partnerId)){
            deliveryPartnerMap.remove(partnerId);
        }
    }
    //09
    public int getCountOfUnassignedOrders(){
        int count=0;
        List<String> orders =  new ArrayList<>(orderMap.keySet());
        for(String order:orders){
            if(!orderPartnerMap.containsKey(order)){
                count++;
            }
        }
        return count;
    }
    //11
    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        int time = 0;

        if(partnerOrderMap.containsKey(partnerId)){
            HashSet<String> orders = partnerOrderMap.get(partnerId);
            for(String order: orders){
                if(orderMap.containsKey(order)){
                    Order currOrder = orderMap.get(order);
                    time = Math.max(time, currOrder.getDeliveryTime());
                }
            }
        }

        int hour = time/60;
        int minutes = time%60;

        String hourInString = String.valueOf(hour);
        String minInString = String.valueOf(minutes);
        if(hourInString.length() == 1){
            hourInString = "0" + hourInString;
        }
        if(minInString.length() == 1){
            minInString = "0" + minInString;
        }

        return  hourInString + ":" + minInString;
    }
    //10
    public int getOrdersLeftAfterGivenTimeByPartnerId(String timeS, String partnerId) {
        int hour = Integer.valueOf(timeS.substring(0, 2));
        int minutes = Integer.valueOf(timeS.substring(3));
        int time = hour*60 + minutes;

        int countOfOrders = 0;
        if(partnerOrderMap.containsKey(partnerId)){
            HashSet<String> orders = partnerOrderMap.get(partnerId);
            for(String order: orders){
                if(orderMap.containsKey(order)){
                    Order currOrder = orderMap.get(order);
                    if(time < currOrder.getDeliveryTime()){
                        countOfOrders++;
                    }
                }
            }
        }
        return countOfOrders;
    }
    //13
    public String deleteOrderById(String orderId) {
        if(orderPartnerMap.containsKey(orderId)){
            String partnerId = orderPartnerMap.get(orderId);
            HashSet<String> orders = partnerOrderMap.get(partnerId);
            orders.remove(orderId);
            partnerOrderMap.put(partnerId, orders);

            //change order count of partner
            DeliveryPartner partner = deliveryPartnerMap.get(partnerId);
            partner.setNumberOfOrders(orders.size());
        }

        if(orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
        return "Success!";

    }
    //07
    public List<String> getOrdersByPartnerId(String partnerId){
        HashSet<String> orderList = new HashSet<>();
        if(partnerOrderMap.containsKey(partnerId)) orderList = partnerOrderMap.get(partnerId);
        return new ArrayList<>(orderList);
    }
    //06





}
