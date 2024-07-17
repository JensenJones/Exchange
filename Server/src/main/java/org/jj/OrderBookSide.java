package org.jj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class OrderBookSide {
    protected static final Logger LOGGER = LoggerFactory.getLogger(OrderBookSide.class);

    protected final List<OrdersAtPrice> ordersByPrice = new LinkedList<>();
    protected final Map<UUID, Node> uuidToNodeMap = new HashMap<>();

    public OrderBookSide() {
    }

    public void addOrder(UUID uuid, long quantity, long quantityFilled, long price) {
        Node node = new Node(uuid, quantity, quantityFilled, price);
        uuidToNodeMap.put(uuid, node);

        ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator();

        boolean added = false;
        while (iterator.hasNext()) {
            OrdersAtPrice current = iterator.next();
            if (current.price == price) {
                current.add(node);
                return;
            } else if (current.price > price){ // TODO HERE ************************* TODO WHAT HERE IDK
                OrdersAtPrice newList = new OrdersAtPrice(node);
                iterator.previous();
                iterator.add(newList);
                return;
            }
        }

        if (!added) {
            ordersByPrice.add(new OrdersAtPrice(node));
        } else {
            LOGGER.error("ORDER NEVER ADDED TO ORDER BOOK | uuid = {} | quantity = {} | price = {} |", uuid, quantity, price);
        }
    }

    public boolean hasOrder(UUID uuid) {
        return uuidToNodeMap.containsKey(uuid);
    }

    public boolean removeOrder(UUID uuid) {
        Node node = uuidToNodeMap.remove(uuid);

        assert (node != null);
        ListIterator<OrdersAtPrice> iterator = ordersByPrice.listIterator();
        while (iterator.hasNext()) {
            OrdersAtPrice ordersAtPrice = iterator.next();
            if (ordersAtPrice.price == node.price) {
                ordersAtPrice.removeNode(node);
                if (ordersAtPrice.head == null) {
                    iterator.remove();
                }
                return true;
            }
        }
        return false;
    }

    public abstract long matchOrder(UUID uuid, long quantity, long price);

    protected long trade(UUID uuid, long quantity, long quantityTraded, Node current, OrdersAtPrice ordersAtPrice, ListIterator<OrdersAtPrice> iterator) {
        long quantityTrading = Math.min(quantity - quantityTraded, current.getQuantityLeft());
        new Match(uuid, current.getUuid(), quantityTrading, current.getPrice(), new TimestampProviderImplEpochNano());
        if (current.trade(quantityTrading)) {
            uuidToNodeMap.remove(current.getUuid());
            ordersAtPrice.removeNode(current);
            if (ordersAtPrice.gethead() == null) {
                iterator.remove();
            }
        }
        quantityTraded += quantityTrading;
        return quantityTraded;
    }

    protected static class OrdersAtPrice {
        private Node head;
        private Node tail;
        private final long price;

        public OrdersAtPrice(Node head) {
            this.head = head;
            this.tail = head;
            price = head.price;
        }

        public void add(Node node) {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }

        public long getPrice() {
            return price;
        }

        // Ensures Head and Tail of OrdersAtPrice are kept when either head or tail node are removed
        public void removeNode(Node node) {
            if (head == node) {
                head = node.next;
            }
            if (tail == node) {
                tail = node.prev;
            }
            node.unlink();
        }

        public Node gethead() {
            return head;
        }
    }

    protected static class Node {
        private final UUID uuid;
        private final long quantity;
        private long quantityFilled = 0;
        private final long price;
        private Node next;
        private Node prev;

        public Node(UUID uuid, long quantity, long quantityFilled, long price) {
            this.quantity = quantity;
            this.price = price;
            this.quantityFilled = quantityFilled;
            this.uuid = uuid;
        }

        public void unlink() {
            if (next != null) {
                next.prev = prev;
            }
            if (prev != null) {
                prev.next = next;
            }
            prev = next = null;
        }

        public void fillOrder(long quantity) {
            quantityFilled += quantity;
        }

        public Node getNext() {
            return next;
        }

        public long getQuantityLeft() {
            return quantity - quantityFilled;
        }

        public UUID getUuid() {
            return uuid;
        }

        public boolean trade(long tradingQuantity) {
            quantityFilled += tradingQuantity;
            return quantityFilled == quantity;
        }

        public long getPrice() {
            return price;
        }
    }
}
