package club.friendsgame.cardrift.services;


import club.friendsgame.cardrift.models.CardType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MyCardManager {

    @Autowired
    private ApplicationContext applicationContext;

    public List<CardPlayed> getServicesByType(CardType cardType) {
        // Retrieve all MyService instances
        Map<String, CardPlayed> myServiceBeans = applicationContext.getBeansOfType(CardPlayed.class);

        // Filter by type
        return myServiceBeans.values()
                .stream()
                .filter(service -> service instanceof CardPlayed && cardType.equals(((CardPlayed) service).getCardType()))
                .collect(Collectors.toList());
    }
}
