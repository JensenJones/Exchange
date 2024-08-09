package org.jj;

import org.jj.MatchingEngine.MatchingEngineImpl;
import org.jj.Product.Product;
import org.jj.Product.ProductStore;
import org.jj.Providers.IntIdProvider;
import org.jj.Providers.SystemTimestampProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MatchingEngineProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchingEngineProvider.class);

    private final Map<Product, MatchingEngineImpl> productToMatchingEngine = new HashMap<>();
    private final ProductStore productStore;


    public MatchingEngineProvider(ProductStore productStore) {
        this.productStore = productStore;

        for (Product product : productStore.getAllProducts()) {
            productToMatchingEngine.put(product, new MatchingEngineImpl(new SystemTimestampProvider(), new IntIdProvider()));
        }
    }

    public MatchingEngineImpl getMatchingEngine(int productId) {
        Product product = productStore.getProduct(productId);

        if (product == null) {
            LOGGER.error("Product Does Not Exist");
            return null;
        }

        return productToMatchingEngine.computeIfAbsent(product,
                key -> new MatchingEngineImpl(new SystemTimestampProvider(), new IntIdProvider()));
    }
}
