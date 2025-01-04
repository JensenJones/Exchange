package org.jj.product;

public class Product {
    private final String symbol;
    private final int id;
    private final String name;

    public Product(int id, String symbol, String name) {
        this.id = id;
        this.symbol = symbol;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }
}
