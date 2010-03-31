package org.jbehave.examples.trader.i18n;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.jbehave.Ensure.ensureThat;

import java.util.Locale;

import org.jbehave.examples.trader.model.Stock;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.KeyWords;
import org.jbehave.core.i18n.I18nKeyWords;
import org.jbehave.core.i18n.StringEncoder;
import org.jbehave.core.steps.Steps;
import org.jbehave.core.steps.StepsConfiguration;

public class ItTraderSteps {

    private Stock stock;

    @Given("ho un'azione con simbolo $symbol e una soglia di $threshold")
    public void aStock(@Named("symbol") String symbol, @Named("threshold") double threshold) {
        stock = new Stock(symbol, threshold);
    }

    @When("l'azione e' scambiata al prezzo di $price")
    public void stockIsTraded(@Named("price") double price) {
        stock.tradeAt(price);
    }

    @Then("lo status di allerta e' $status")
    public void alertStatusIs(@Named("status") String status) {
        ensureThat(stock.getStatus().name(), equalTo(status));
    }



}
