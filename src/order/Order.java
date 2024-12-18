package order;

import java.util.Date;

public class Order {

    private String orderNo;
    private String customerId;
    private String customerName;
    private String productNo;
    private String productName;
    private int quantity;
    private String destination;
    private Date orderDate;

    public Order(final String orderNo, final String customerId, final String customerName, final String productNo,
                 final String productName, final int quantity, final String destination, final Date orderDate) {
        this.orderNo = orderNo;
        this.customerId = customerId;
        this.customerName = customerName;
        this.productNo = productNo;
        this.productName = productName;
        this.quantity = quantity;
        this.destination = destination;
        this.orderDate = orderDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProductNo() {
        return productNo;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDestination() {
        return destination;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return "[orderNo=" + orderNo + ", customerId=" + customerId + ", customerName=" + customerName
                + ", productNo=" + productNo + ", productName=" + productName + ", quantity=" + quantity
                + ", destination=" + destination + ", orderDate=" + orderDate + "]";
    }

}