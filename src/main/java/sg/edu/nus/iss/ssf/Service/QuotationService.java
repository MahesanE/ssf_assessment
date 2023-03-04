package sg.edu.nus.iss.ssf.Service;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.ssf.Model.Cart;
import sg.edu.nus.iss.ssf.Model.Item;
import sg.edu.nus.iss.ssf.Model.Quotation;

@Service
public class QuotationService {

    public String url = "https://quotation.chuklee.com/quotation";
    private final RestTemplate restTemplate;

    public QuotationService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Quotation getQuotations(List<String> items) {
        Quotation quote = new Quotation();

        // convert all the items to jason array 
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
        for (int i = 0; i < items.size(); i++) {
            arrBuilder.add(items.get(i));
        }
        JsonArray arr = arrBuilder.build();
        //System.out.println("JsonArray: " + arr);

        // Make HTTP Call to get quotation (POSTING Json Data)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(arr.toString(), headers);

        ResponseEntity<String> responseEntity = restTemplate.
        postForEntity(url, requestEntity, String.class);
        
        String payload = responseEntity.getBody();
        
        Integer statusCode = responseEntity.getStatusCodeValue();

        //System.out.printf(">>> Status Code: %d\n", statusCode);
        //System.out.printf(">>> Payload: %s\n", payload);

        if (statusCode == HttpStatus.OK.value()) {
            JsonObject json = Json.createReader(new StringReader(payload)).readObject();
            quote.setQuoteId(json.getString("quoteId"));
            JsonArray json2 = json.getJsonArray("quotations");

            // loop through array
            for (int i = 0; i < json2.size(); i++) {
                JsonObject json3 = json2.getJsonObject(i);
                quote.addQuotation(json3.getString("item"), (float) json3.getJsonNumber("unitPrice").doubleValue());
            }
        } else {
            quote.addQuotation("error", (float) statusCode);
        }

        return quote;
    }

    public List<String> getList(Cart cart) {
        List<Item> contents = cart.getContents();
        List<String> items = new LinkedList<>();

        for (int i = 0; i < contents.size(); i++) {
            items.add(contents.get(i).getName());
        }
        return items;
    }

    public Float calculateCost(Quotation quote, Cart cart) {
        List<Item> contents = cart.getContents();
        Float cost = 0.0f;

        for (int i = 0; i < contents.size(); i++) {
            // match cart item with quotation list
            Float unitPxOfItem = quote.getQuotation(contents.get(i).getName());
            Integer quantity = contents.get(i).getQuantity();
            cost += (unitPxOfItem * quantity);
        }
        return cost;
        
    }
}
