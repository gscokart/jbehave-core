package org.jbehave.examples.trader.i18n.stories;

import org.jbehave.examples.trader.i18n.ItTraderStory;


public class ItTraderIsAlertedOfStatus extends ItTraderStory {

    public ItTraderIsAlertedOfStatus() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ItTraderIsAlertedOfStatus(final ClassLoader classLoader) {
    	super(classLoader);
    }

}
