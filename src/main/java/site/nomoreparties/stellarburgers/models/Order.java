package site.nomoreparties.stellarburgers.models;

import java.util.UUID;

public class Order {

    public String ingredients;

    public Order(String ingredients) {
        this.ingredients = ingredients;
    }

    public Order(){

    }

    public Order setIngredient(String ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public static Order createRandomIDOrderIngredient() {
        return new Order().setIngredient(String.valueOf(UUID.randomUUID().toString().replace("-","").substring(0,24)));
    }
}
