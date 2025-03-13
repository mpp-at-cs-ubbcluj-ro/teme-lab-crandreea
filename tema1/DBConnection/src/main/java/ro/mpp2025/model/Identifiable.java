package ro.mpp2025.model;

public interface Identifiable<ID> {
    void setId(ID id);
    ID getId();
}
