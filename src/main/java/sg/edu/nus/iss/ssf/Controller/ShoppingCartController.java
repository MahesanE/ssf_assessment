package sg.edu.nus.iss.ssf.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import sg.edu.nus.iss.ssf.Model.Invoice;
import sg.edu.nus.iss.ssf.Model.Item;
import sg.edu.nus.iss.ssf.Model.Quotation;
import sg.edu.nus.iss.ssf.Service.InvoiceService;
import sg.edu.nus.iss.ssf.Service.QuotationService;

@Controller
public class ShoppingCartController {

    @Autowired
    private QuotationService quotationService;

    @Autowired
    private InvoiceService invoiceService;




    @GetMapping(path = {"/", "view1.html"} )
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
    public String getShippingAddress(Model model, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart.getContents().isEmpty()){
            model.addAttribute("cart", cart);
            model.addAttribute("item", new Item());
        } else {
            model.addAttribute("address", new Address());
            Address address = (Address) session.getAttribute("address");
            if (null==address){
                address = new Address();
                session.setAttribute("address", address);

            }
            model.addAttribute("address", address);

        }

        return "view2";



        // model.addAttribute("address", new Address());
        // return "view2";
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

        List<String> items = quotationService.getList(cart);
        Quotation quotation = quotationService.getQuotations(items);


        //Total cost calculate
        Float totalCost = quotationService.calculateCost(quotation, cart);

        //Create the invioce to display on view3
        Invoice invoice = invoiceService.generateInvoice(cart, address);



        // Add the cart and address to the model and clear the session
        model.addAttribute("cart", cart);
        model.addAttribute("address", address);
        model.addAttribute("quotation", quotation);
        model.addAttribute("totalCost", totalCost);
        model.addAttribute("invoice", invoice);
        session.removeAttribute("cart");
        session.removeAttribute("address");

        return "view3";

     //Sorry teacher i dont know already 
    }


}
