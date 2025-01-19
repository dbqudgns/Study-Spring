package hellojpa;

import jakarta.persistence.*;

@Entity
@Table(name = "ADDRESS")
public class AddressEntity {

    @Id @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity() {

    }

    public AddressEntity(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}
