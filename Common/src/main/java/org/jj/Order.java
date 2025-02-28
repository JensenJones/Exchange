package org.jj;

public record Order (int orderId, String product, double price, long quantity, long quantityFilled, Expiry expiry, BuySell buySell) {
    public Service.Order toProto() {
        return Service.Order.newBuilder().setId(orderId)
                                         .setProductSymbol(product)
                                         .setPrice(price)
                                         .setQuantity(quantity)
                                         .setQuantityFilled(quantityFilled)
                                         .setExpiry(Service.Expiry.valueOf(expiry.toString()))
                                         .setBuySell(Service.BuySell.valueOf(buySell.toString())).build();
    }
}
