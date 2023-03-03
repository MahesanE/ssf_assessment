package sg.edu.nus.iss.ssf.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import sg.edu.nus.iss.ssf.Model.Address;
//import sg.edu.nus.iss.ssf.Model.Address;
import sg.edu.nus.iss.ssf.Model.Cart;
import sg.edu.nus.iss.ssf.Model.Item;

@Controller
public class ShoppingCartController {

    @GetMapping(path = { "/" })
    public String getCart(Model model, HttpSession session) {

        // check if it is a new session or nahh
        Cart cart = (Cart) session.getAttribute("cart");
        if (null == cart) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("item", new Item());
        model.addAttribute("cart", cart);

        return "view1";
    }

    @PostMapping(path = "/")
    public String postData(@Valid Item item, BindingResult result, Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (result.hasErrors()) {
            model.addAttribute("item", item);
            model.addAttribute("cart", cart);

            return "view1";
        }

        cart.addItem(item);
        model.addAttribute("item", item);
        model.addAttribute("cart", cart);
        return "view1";

    }

    @GetMapping(path = { "/shippingaddress" })
    public String getShippingAddress(Model model) {
        model.addAttribute("address", new Address());
        return "view2";
    }

    @PostMapping(path = { "/shippingaddress" })
    public String postShippingAddress(@Valid Address address, BindingResult result, HttpSession session) {
        if (result.hasErrors()) {
            return "view2";
        }
        session.setAttribute("address", address);
        return "redirect:/checkout";
    }

    @GetMapping(path = { "/checkout" })
    public String getCheckout(Model model, HttpSession session) {
        // Retrieve the cart and address from session
        Cart cart = (Cart) session.getAttribute("cart");
        Address address = (Address) session.getAttribute("address");

        // Check if they exist in the session
        if (cart == null || address == null) {
            return "redirect:/"; // Redirect to the cart page if they don't exist
        }

        // Add the cart and address to the model and clear the session
        model.addAttribute("cart", cart);
        model.addAttribute("address", address);
        session.removeAttribute("cart");
        session.removeAttribute("address");

        return "view3";
    }

    // @PostMapping(path="/shippingaddress")
    // public String postCartorder(Model model, HttpSession session, @Valid Address
    // address, BindingResult result){
    // if(result.hasErrors()){
    // return "view2";
    // }
    // }

    // @PostMapping("/add")
    // public String addItem(@Valid @ModelAttribute("item") Item item, BindingResult
    // result, Model model) {
    // if (result.hasErrors()) {
    // return "view1";
    // }

    // if (items.containsKey(item.getName())) {
    // // if the item already exists in the cart, add to the quantity
    // items.get(item.getName()).setQuantity(items.get(item.getName()).getQuantity()
    // + item.getQuantity());
    // } else {
    // // if the item doesn't exist in the cart, add it to the cart
    // items.put(item.getName(), item);
    // }

    // // add the shopping cart items to the model
    // model.addAttribute("items", items.values());

    // // redirect the user back to the view1 page
    // return "redirect:/add";
    // }
    // @GetMapping("/cart")
    // public String cart(Model model) {
    // model.addAttribute("items", items.values());
    // return "cart";
}
