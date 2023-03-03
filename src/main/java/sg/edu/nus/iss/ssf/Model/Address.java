package sg.edu.nus.iss.ssf.Model;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Address {
    @NotNull
    @Size(min = 2, message = "Your name has to be longer than 2 characters...")
    private String name;

    @NotNull(message = "Please state your address")
    @NotEmpty(message = "Please state your address")
    @NotBlank(message = "Please enter a relevant address")
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Address [name=" + name + ", address=" + address + "]";
    }

    public static Address create(JsonObject json) {
        Address address = new Address();
        address.setName(json.getString("name"));
        address.setAddress(json.getString("address"));

        return address;
    }
    

    
    
}
