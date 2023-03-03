package sg.edu.nus.iss.ssf.Model;

import java.io.Serializable;

import jakarta.json.JsonObject;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Item implements Serializable{

    @NotEmpty(message = "Please select an item")
    private String name;

    @NotNull(message = "Please enter a quantity")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    

    public Item(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item [name=" + name + ", quantity=" + quantity + "]";
    }
    
    public static Item create(JsonObject json){
        Item item = new Item();
        item.setName(json.getString("name"));
        item.setQuantity(json.getInt("quantity"));
        return item;
    }

}
