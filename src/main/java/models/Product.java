package models;

import java.util.Objects;

public class Product {

    private String id;
    private String productName;
    private String color;
    private String price;
    private Integer balance;

    public Product(String id, String productName, String color, String price, Integer balance){
        this.id = id;
        this.productName = productName;
        this.color = color;
        this.price = price;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productname) {
        this.productName = productname;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(productName, product.productName) && Objects.equals(color, product.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, color);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", color='" + color + '\'' +
                ", price='" + price + '\'' +
                ", balance=" + balance +
                '}';
    }
}
