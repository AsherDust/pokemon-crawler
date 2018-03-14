package com.noash.poke.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataParserTest {
    @Autowired
    private DataParser dataParser;

    @Test
    public void test() throws Exception {
        System.out.println("ignore testing data parser");
    }

    public void parseAbilities() throws Exception {
        dataParser.parseAbilities();
    }

    public void parseItems() throws Exception {
        dataParser.parseItems();
    }

    public void parseItemAccesses() throws Exception {
        dataParser.parseItemAccesses();
    }

    public void parseMoves() throws Exception {
        dataParser.parseMoves();
    }

    public void parsePokedexIds() throws Exception {
        dataParser.parsePokedexIds();
    }

    public void parsePokemons() throws Exception {
        dataParser.parsePokemons();
    }

    public void parsePokemonLevelMoves() throws Exception {
        dataParser.parsePokemonLevelMoves();
    }

    public void parsePokemonTmMoves() throws Exception {
        dataParser.parsePokemonTmMoves();
    }

    public void parsePokemonFixedMoves() throws Exception {
        dataParser.parsePokemonFixedMoves();
    }

    public void parseConstants() throws Exception {
        dataParser.parseConstants();
    }

    public void parseEvolutionChains() throws Exception {
        dataParser.parseEvolutionChains();
    }

    @Test
    public void repairAlolaIds() throws Exception {
        dataParser.repairAlolaIds();
    }
}
