package sg.edu.nus.iss.ssf.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.ssf.Model.Address;
import sg.edu.nus.iss.ssf.Model.Cart;
import sg.edu.nus.iss.ssf.Model.Invoice;
import sg.edu.nus.iss.ssf.Model.Quotation;

@Service
public class InvoiceService {

    @Autowired
    private QuotationService quotationService;

    public Invoice generateInvoice(Cart cart, Address address) {

        // Get the list of items in the cart
        List<String> items = quotationService.getList(cart);

        // Get the quotation for the items
        Quotation quotation = quotationService.getQuotations(items);

        // Calculate the total cost
        Float totalCost = quotationService.calculateCost(quotation, cart);

        // Create the invoice
        Invoice invoice = new Invoice();
        invoice.setCart(cart);
        invoice.setAddress(address);
        invoice.setQuotation(quotation);
        invoice.setTotalCost(totalCost);

        return invoice;
    }
}

