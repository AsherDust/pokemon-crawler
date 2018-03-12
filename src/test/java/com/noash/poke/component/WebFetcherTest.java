package com.noash.poke.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebFetcherTest {
    @Autowired
    private WebFetcher webFetcher;

    public void fetchConstants() throws Exception {
        webFetcher.fetchConstants();
    }

    public void fetchAbilityList() throws Exception {
        webFetcher.fetchAbilityList();
    }

    public void fetchItemList() throws Exception {
        webFetcher.fetchItemList();
    }

    public void fetchMoveList() throws Exception {
        webFetcher.fetchMoveList();
    }

    public void fetchPokemonList() throws Exception {
        webFetcher.fetchPokemonList();
    }

    @Test
    public void fetchAbilityDetails() throws Exception {
        webFetcher.fetchAbilityDetails();
    }

    @Test
    public void fetchItemDetails() throws Exception {
        webFetcher.fetchItemDetails();
    }

    @Test
    public void fetchMoveDetails() throws Exception {
        webFetcher.fetchMoveDetails();
    }

    public void renameKeyForMoveDetails() throws Exception {
        webFetcher.renameKeyForMoveDetails();
    }

    public void saveProgressForPokemonDetails() throws Exception {
        webFetcher.saveProgressForPokemonDetails();
    }

    @Test
    public void fetchPokemonDetails() throws Exception {
        webFetcher.fetchPokemonDetails();
    }

    public void createIndexForPokemonDetails() throws Exception {
        webFetcher.createIndexForPokemonDetails();
    }
}
