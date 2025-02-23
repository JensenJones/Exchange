package org.jj;

public record Order (int orderId, String product, double price, long quantity, long quantityFilled, BuySell buySell) {
    public Service.Order toProto() {
        return Service.Order.newBuilder().setId(orderId)
                                         .setProductSymbol(product)
                                         .setPrice(price)
                                         .setQuantity(quantity)
                                         .setQuantityFilled(quantityFilled)
                                         .setBuySell(Service.BuySell.valueOf(buySell.toString())).build();
    }
}
