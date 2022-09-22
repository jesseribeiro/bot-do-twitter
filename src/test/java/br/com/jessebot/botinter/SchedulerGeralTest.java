package br.com.jessebot.botinter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class SchedulerGeralTest {

    @Autowired
    private SchedulerGeral schedulerGeral;

    @Test
    public void validaScheduler() throws IOException {
        schedulerGeral.meiaNoite();
    }
}
