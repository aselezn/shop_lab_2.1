package models;

import java.sql.Date;
import java.util.Objects;

public class Order {

    private long id;
    private Date createDate;
    private String customerFullName;
    private String phone;
    private String email;
    private String customerAddress;
    private String status;
    private Date shippingDate;



    public Order(long id, Date createDate, String customerFullName, String phone, String email,
                 String customerAddress, String status, Date shippingDate) {
        this.id = id;
        this.createDate = createDate;
        this.customerFullName = customerFullName;
        this.phone = phone;
        this.email = email;
        this.customerAddress = customerAddress;
        this.status = status;
        this.shippingDate = shippingDate;
    }

    public Order(String customerFullName, String phone, String email,
                 String customerAddress) {
        this.customerFullName = customerFullName;
        this.phone = phone;
        this.email = email;
        this.customerAddress = customerAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCustomerFullName() {
        return customerFullName;
    }

    public void setCustomerFullName(String customerFullName) {
        this.customerFullName = customerFullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order orders = (Order) o;
        return id == orders.id && Objects.equals(createDate, orders.createDate) && Objects.equals(customerFullName, orders.customerFullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, customerFullName);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", createDate=" + createDate +
                ", customerFullName='" + customerFullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", status='" + status + '\'' +
                ", shippingDate=" + shippingDate +
                '}';
    }


}
