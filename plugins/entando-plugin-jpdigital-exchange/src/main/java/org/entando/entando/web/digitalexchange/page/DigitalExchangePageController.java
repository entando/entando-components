package org.entando.entando.web.digitalexchange.page;

import org.entando.entando.aps.system.jpa.portdb.DigitalExchangePage;
import org.entando.entando.aps.system.jpa.portdb.DigitalExchangePageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/digitalExchange/pages")
public class DigitalExchangePageController {

    private DigitalExchangePageRepository repo;

    @Autowired
    public DigitalExchangePageController(DigitalExchangePageRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public ResponseEntity createPage() {
        DigitalExchangePage page = new DigitalExchangePage();
        page.setId(new Random().nextLong());
        page.setName("Random page # " + page.getId().toString());

        DigitalExchangePage storedPage = repo.save(page);
        return ResponseEntity.ok(storedPage);
    }

    @GetMapping
    public ResponseEntity getAllPages() {
        List<DigitalExchangePage> pages = repo.findAll();

        return ResponseEntity.ok(pages);
    }


}
