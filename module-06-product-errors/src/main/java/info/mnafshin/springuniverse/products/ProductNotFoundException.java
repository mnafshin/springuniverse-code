package info.mnafshin.springuniverse.products;

class ProductNotFoundException extends RuntimeException {

    ProductNotFoundException(Long id) {
        super("Product " + id + " was not found");
    }
}
