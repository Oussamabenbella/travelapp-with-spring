package com.example.travelappv2.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit être dans le futur")
    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int numberOfPeople;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false)
    private boolean confirmed = false;

    // Constructeurs
    public Reservation() {
    }

    public Reservation(User user, Destination destination, LocalDate startDate, LocalDate endDate, int numberOfPeople) {
        this.user = user;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfPeople = numberOfPeople;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
