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
    public void parseAbilities() throws Exception {
        dataParser.parseAbilities();
    }

    @Test
    public void parseItems() throws Exception {
        dataParser.parseItems();
    }

    @Test
    public void parseItemAccesses() throws Exception {
        dataParser.parseItemAccesses();
    }

    @Test
    public void parseMoves() throws Exception {
        dataParser.parseMoves();
    }

    @Test
    public void parsePokedexIds() throws Exception {
        dataParser.parsePokedexIds();
    }

    @Test
    public void parsePokemons() throws Exception {
        dataParser.parsePokemons();
    }

    @Test
    public void parsePokemonLevelMoves() throws Exception {
        dataParser.parsePokemonLevelMoves();
    }

    @Test
    public void parsePokemonTmMoves() throws Exception {
        dataParser.parsePokemonTmMoves();
    }

    @Test
    public void parsePokemonFixedMoves() throws Exception {
        dataParser.parsePokemonFixedMoves();
    }

    @Test
    public void parseConstants() throws Exception {
        dataParser.parseConstants();
    }

    @Test
    public void parseEvolutionChains() throws Exception {
        dataParser.parseEvolutionChains();
    }
}
