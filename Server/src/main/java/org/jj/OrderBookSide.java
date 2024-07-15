package org.jj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class OrderBookSide {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderBookSide.class);

    private List<OrdersAtPrice> ordersByPrice = new LinkedList<>();
    private final Map<UUID, Node> uuidToNodeMap = new HashMap<>();

    public OrderBookSide() {
    }

    public void addOrder(Order order) {
        long price = order.getPrice();
        UUID uuid = order.getUuid();
        Node node = new Node(order);
        uuidToNodeMap.put(uuid, node);

        ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator();

        while (iterator.hasNext()) {
            OrdersAtPrice current = iterator.next();
            if (current.price == price) {
                current.add(node);
                return;
            } else if (current.price > price) {
                OrdersAtPrice newList = new OrdersAtPrice(node);
                iterator.previous();
                iterator.add(newList);
                return;
            }
        }

        if (ordersByPrice.isEmpty()) {
            ordersByPrice.add(new OrdersAtPrice(node));
        } else {
            LOGGER.error("ORDER NEVER ADDED TO ORDER BOOK");
        }
    }

    public boolean hasOrder(UUID uuid) {
        return uuidToNodeMap.containsKey(uuid);
    }

    public Order getOrder(UUID uuid) {
        Node node = uuidToNodeMap.get(uuid);
        if (node == null) {
            return null;
        }
        return uuidToNodeMap.get(uuid).order;
    }

    public Order removeOrder(UUID uuid) {
        Node node = uuidToNodeMap.remove(uuid);
        assert (node != null);
        for (OrdersAtPrice ordersAtPrice : ordersByPrice) {
            if (ordersAtPrice.price == node.order.getPrice()) {
                ordersAtPrice.removeNodeSafety(node);
                break;
            }
        }
        node.removeFromOrdersAtPrice();
        return node.order;
    }

    public boolean matchOrder(Order order) {
        if (order.getBuySell() == BuySell.BUY) {
            for (OrdersAtPrice ordersAtPrice : ordersByPrice) {
                if (ordersAtPrice.price > order.getPrice()) {
                    break;
                }
                if (ordersAtPrice.head != null) {
                    Node current = ordersAtPrice.head;
                    while (current != null) {
                        order.trade(current.order);
                        Node tradedWith = current;
                        current = current.next;
                        if (tradedWith.order.getQuantityFilled() == tradedWith.order.getQuantity()) {
                            removeOrder(tradedWith.order.getUuid());
                        }
                    }
                    if (order.getQuantityFilled() == order.getQuantity()) {
                        return true;
                    }
                }
            }
        } else {
            ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator(ordersByPrice.size());
            while (iterator.hasPrevious()) {
                OrdersAtPrice ordersAtPrice = iterator.previous();
                if (ordersAtPrice.price < order.getPrice()) {
                    break;
                }
                if (ordersAtPrice.head != null) {
                    Node current = ordersAtPrice.head;
                    while (current != null) {
                        order.trade(current.order);
                        Node tradedWith = current;
                        current = current.next;
                        if (tradedWith.order.getQuantityFilled() == tradedWith.order.getQuantity()) {
                            removeOrder(tradedWith.order.getUuid());
                        }
                    }
                    if (order.getQuantityFilled() == order.getQuantity()) {
                        return true;
                    }
                }
            }
        }
        return order.getQuantityFilled() == order.getQuantity();
    }

    private static class OrdersAtPrice {
        private Node head;
        private Node tail;
        private final Long price;

        public OrdersAtPrice(Node head) {
            this.head = head;
            this.tail = head;
            price = head.order.getPrice();
        }

        public void add(Node node) {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        // Ensures Head and Tail of OrdersAtPrice are kept when either head or tail node are removed
        public void removeNodeSafety(Node node) {
            if (head == node) {
                if (node.next != null) {
                    head = node.next;
                }
            }
            if (tail == node) {
                if (node.prev != null) {
                    tail = node.prev;
                }
            }
        }
    }

    private static class Node {
        private final Order order;
        private Node next;
        private Node prev;

        public Node(Order value) {
            this.order = value;
        }

        public void removeFromOrdersAtPrice() {
            if (prev == null && next == null) {
                return;
            }
            if (next == null) {
                prev.next = null;
            } else if (prev == null) {
                next.prev = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
        }
    }
}
