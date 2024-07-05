package proj.concert.service.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String label;

    @Version
    private Long version;

    @Column(name = "is_booked")
    private boolean isBooked;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "cost")
    private BigDecimal cost;

    public Seat(String label, boolean isBooked, LocalDateTime date, BigDecimal cost) {
        this.label = label;
        this.isBooked = isBooked;
        this.date = date;
        this.cost = cost;
    }

    public Seat() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat seat = (Seat) o;

        return new EqualsBuilder()
                .append(label, seat.label)
                .append(isBooked, seat.isBooked)
                .append(date, seat.date)
                .append(cost, seat.cost)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(label)
                .append(isBooked)
                .append(date)
                .append(cost)
                .toHashCode();
    }
}
