package com.noash.poke.dao;

import com.noash.poke.domain.PokemonMove;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PokemonMoveDaoTest {
    @Autowired
    private PokemonMoveDao pokemonMoveDao;

    @Test
    @Transactional
    public void insertLevel() throws Exception {
        pokemonMoveDao.insertLevel(createInstance());
    }

    @Test
    @Transactional
    public void insertTm() throws Exception {
        pokemonMoveDao.insertTm(createInstance());
    }

    @Test
    @Transactional
    public void insertFixed() throws Exception {
        pokemonMoveDao.insertFixed(createInstance());
    }

    private PokemonMove createInstance() {
        PokemonMove pokemonMove = new PokemonMove();
        pokemonMove.setVersion(8);
        pokemonMove.setPokemonId(999);
        pokemonMove.setMoveId(999);
        pokemonMove.setLearnLevel(100);
        pokemonMove.setTm(99);
        return pokemonMove;
    }
}
