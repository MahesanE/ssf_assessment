package sg.edu.nus.iss.ssf.Model;

import java.util.LinkedList;
import java.util.List;

public class Cart {
    private List<Item> contents = new LinkedList<>();

    public List<Item> getContents() {
        return contents;
    }

    public void setContents(List<Item> contents) {
        this.contents = contents;
    }

     public void addItem(Item item) {
        boolean itemExists = false;
        for (Item existingItem : contents) {
            if (existingItem.getName().equals(item.getName())) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            contents.add(item);
        }
    }


    @Override
    public String toString() {
        return "Cart [contents=" + contents + "]";
    }

    
}
