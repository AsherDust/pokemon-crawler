package com.noash.poke.dao;

import com.noash.poke.domain.Pokemon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PokemonDaoTest {
    @Autowired
    private PokemonDao pokemonDao;

    @Test
    @Transactional
    public void insert() throws Exception {
        Pokemon pokemon = new Pokemon(999, 999, 0, "fsds", "fsdfs", "rur", "zxc", 1, 0, 1, 2, 3, 1, 2, 100, 100, 100, 100, 100, 100, 1, 1, 1, 0, 0, 0, "wg", 1, BigDecimal.TEN, BigDecimal.TEN, 1, 0, 50, 2, 2, 20, 100, "sfd", "sfd", "sf", "");

        pokemonDao.insert(pokemon);
    }

    @Test
    public void selectByNatioalId() throws Exception {
        pokemonDao.selectByNationalId(6).forEach(p -> System.out.println("id: " + p.getId() + ", sub_id: " + p.getSubId()));
    }

    @Test
    public void selectIdByNationalIdAndSubId() throws Exception {
        System.out.println(pokemonDao.selectIdByNationalIdAndSubId(6, 2));
    }
}
