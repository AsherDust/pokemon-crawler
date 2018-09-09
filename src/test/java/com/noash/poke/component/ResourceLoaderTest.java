package com.noash.poke.component;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceLoaderTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void loadPokemonImages() throws Exception {
        resourceLoader.loadPokemonImages();
    }

    @Test
    public void loadItemImages() throws Exception {
        resourceLoader.loadItemImages();
    }

    @Test
    public void loadPokemonCries() throws Exception {
        resourceLoader.loadPokemonCries();
    }

    @Test
    public void downloadAllFiles() throws Exception {
        resourceLoader.downloadAllFiles();
    }
}
