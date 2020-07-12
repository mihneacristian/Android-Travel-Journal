package com.mihneacristian.traveljournal.FirebaseDB;

public class Trip {

    String tripId;
    String tripName;
    String tripDestination;
    String tripType;
    int tripPrice;
    float tripRating;
    String startDate;
    String endDate;
    String photoURL;

    public Trip() {
    }

    public Trip(String tripId, String tripName, String tripDestination, String tripType, int tripPrice, float tripRating, String startDate, String endDate, String photoURL) {
        this.tripId = tripId;
        this.tripName = tripName;
        this.tripDestination = tripDestination;
        this.tripType = tripType;
        this.tripPrice = tripPrice;
        this.tripRating = tripRating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.photoURL = photoURL;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getTripDestination() {
        return tripDestination;
    }

    public void setTripDestination(String tripDestination) {
        this.tripDestination = tripDestination;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public int getTripPrice() {
        return tripPrice;
    }

    public void setTripPrice(int tripPrice) {
        this.tripPrice = tripPrice;
    }

    public float getTripRating() {
        return tripRating;
    }

    public void setTripRating(float tripRating) {
        this.tripRating = tripRating;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId='" + tripId + '\'' +
                ", tripName='" + tripName + '\'' +
                ", tripDestination='" + tripDestination + '\'' +
                ", tripType='" + tripType + '\'' +
                ", tripPrice=" + tripPrice +
                ", tripRating=" + tripRating +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", photoURL='" + photoURL + '\'' +
                '}';
    }
}
