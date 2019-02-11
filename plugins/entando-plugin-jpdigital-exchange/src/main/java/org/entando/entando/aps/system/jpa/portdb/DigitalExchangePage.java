package org.entando.entando.aps.system.jpa.portdb;

import javax.persistence.*;

@Entity
@Table(name = "digital_exchange_pages" )
public class DigitalExchangePage {

    @Id
    @GeneratedValue
    private Long id;

    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
