package com.shopme.common.entity.order;

import com.shopme.common.entity.IdBasedEntity;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "order_track")
public class OrderTrack extends IdBasedEntity {
    @Column(length = 256)
    private String notes;
    private Date updatedTime;
    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
    @Transient
    public String getUpdatedTimeOnForm(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return dateFormatter.format(this.updatedTime);
    }

    public void setUpdatedTimeOnForm(String dateString){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

        try{
            this.updatedTime = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
