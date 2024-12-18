package product;

public class Product {
    private String productNo;
    private String productName;
    private int stock;
    private int unitPrice;
    private String manufacturer;

    public Product(String productNo, String productName, int stock, int unitPrice, String manufacturer) {
        this.productNo = productNo;
        this.productName = productName;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.manufacturer = manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productNo='" + productNo + '\'' +
                ", productName='" + productName + '\'' +
                ", stock=" + stock +
                ", unitPrice=" + unitPrice +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }
}
