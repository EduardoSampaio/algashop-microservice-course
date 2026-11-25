package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.exception.CustomerArchivedException;
import lombok.Builder;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Customer implements Serializable {
    private UUID id;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String document;
    private Boolean promotionNotificationsAllowed;
    private Boolean archived;
    private OffsetDateTime registeredAt;
    private OffsetDateTime archivedAt;
    private Integer loyaltyPoints;

    @Builder(builderClassName = "BrandNewCustomerBuild", builderMethodName = "brandNewCustomer")
    private static Customer brandNew(String fullName, LocalDate birthDate, String email, String phone,
                                    String document, Boolean promotionNotificationsAllowed, Boolean archived,
                                    OffsetDateTime registeredAt, OffsetDateTime archivedAt, Integer loyaltyPoints) {

        return new Customer(UUID.randomUUID(), fullName, birthDate, email, phone,
                document, promotionNotificationsAllowed, false,
                OffsetDateTime.now(), null, 0);
    }

    private Customer(UUID id, String fullName, LocalDate birthDate, String email, String phone,
                    String document, Boolean promotionNotificationsAllowed, Boolean archived,
                    OffsetDateTime registeredAt, OffsetDateTime archivedAt, Integer loyaltyPoints) {
        this.setId(id);
        this.setFullName(fullName);
        this.setBirthDate(birthDate);
        this.setEmail(email);
        this.setPhone(phone);
        this.setDocument(document);
        this.setPromotionNotificationsAllowed(promotionNotificationsAllowed);
        this.setArchived(archived);
        this.setRegisteredAt(registeredAt);
        this.setArchivedAt(archivedAt);
        this.setLoyaltyPoints(loyaltyPoints);
    }

    public void addLoyaltyPoints(Integer points) {
        verifyChangeable();
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points cannot be null or negative");
        }
        this.setLoyaltyPoints(this.loyaltyPoints + points);
    }

    public void archive() {
        verifyChangeable();
        this.setArchived(true);
        this.setArchivedAt(OffsetDateTime.now());
        this.setFullName("Anonymous");
        this.setPhone("000000000");
        this.setEmail(UUID.randomUUID() + "@example.com");
        this.setBirthDate(null);
    }

    public void enablePromotionNotifications() {
        verifyChangeable();
        this.setPromotionNotificationsAllowed(true);
    }

    public void disablePromotionNotifications() {
        verifyChangeable();
        this.setPromotionNotificationsAllowed(false);
    }

    public void changeName(String fullName) {
        verifyChangeable();
        Objects.requireNonNull(fullName);
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }
        this.setFullName(fullName);
    }

    public void changeEmail(String email) {
        verifyChangeable();
        this.setEmail(email);
    }

    public void changePhone(String phone) {
        verifyChangeable();
        this.setPhone(phone);
    }

    private void setId(UUID id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setFullName(String fullName) {
        Objects.requireNonNull(fullName);
        if (fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be blank");
        }
        this.fullName = fullName;
    }

    private void setBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            this.birthDate = null;
            return;
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }

        this.birthDate = birthDate;
    }

    private void setEmail(String email) {
        Objects.requireNonNull(email);
        if (email.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }

        this.email = email;
    }

    private void setPhone(String phone) {
        Objects.requireNonNull(phone);
        this.phone = phone;
    }

    private void setDocument(String document) {
        Objects.requireNonNull(document);
        this.document = document;
    }

    private void setPromotionNotificationsAllowed(Boolean promotionNotificationsAllowed) {
        verifyChangeable();
        this.promotionNotificationsAllowed = promotionNotificationsAllowed;
    }

    private void setArchived(Boolean archived) {
        Objects.requireNonNull(archived);
        this.archived = archived;
    }

    private void setRegisteredAt(OffsetDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }

    private void setArchivedAt(OffsetDateTime archivedAt) {
        this.archivedAt = archivedAt;
    }

    private void setLoyaltyPoints(Integer loyaltyPoints) {
        Objects.requireNonNull(loyaltyPoints);
        if (loyaltyPoints < 0) {
            throw new IllegalArgumentException("Loyalty points cannot be negative");
        }
        this.loyaltyPoints = loyaltyPoints;
    }

    private void verifyChangeable() {
        if (this.archived) {
            throw new CustomerArchivedException("Customer is already archived");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
