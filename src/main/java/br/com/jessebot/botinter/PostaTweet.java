
package br.com.jessebot.botinter;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostaTweet {

    // ---- INÍCIO CONFIGS ----
    private static final int INTERVALO_MINIMO_HORAS = 0; // compara com o último tweet feito pelo bot
    private static final boolean MOSTRAR_NO_NAVEGADOR = false; // abre navegador com texto do tweet
    private static final boolean POSTAR_TWEET = true; // para poder testar sem postar de verdade
    // ---- FIM CONFIGS ----

    private static final Logger LOG = LoggerFactory.getLogger(PostaTweet.class);

    private static final String JSON_CREDENCIAIS = "keys.json";
    private static final TwitterCredentials TWITTER_CREDENTIALS;
    private static final TwitterClient TWITTER_CLIENT;

    private static final Duration INTERVALO_MINIMO = Duration.ofHours(INTERVALO_MINIMO_HORAS).minusMinutes(9);

    static {
        try {
            URL json = ResourceUtil.getResource(JSON_CREDENCIAIS, PostaTweet.class);

            TWITTER_CREDENTIALS = TwitterClient.OBJECT_MAPPER.readValue(json, TwitterCredentials.class);
            TWITTER_CLIENT = new TwitterClient(TWITTER_CREDENTIALS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        executar();
    }

    public static void executar() throws IOException {
        Instant agora = Instant.now();

        LOG.debug("Postando para a conta {}.", TWITTER_CLIENT.getUserFromUserId(TWITTER_CLIENT.getUserIdFromAccessToken()).getName());

        Tweet dadosUltimoTweet = ultimoTweet();
        /*if (!passouIntervaloMinimo(dadosUltimoTweet)) {
            LOG.info("Finalizou porque não passou o intervalo mínimo desde o último tweet.");
            return;
        }*/

        String novoTweet = montarNovoTweet();

        mostrarNoNavegador(novoTweet);

        if (POSTAR_TWEET) {
            TWITTER_CLIENT.postTweet(novoTweet);
            LOG.info(novoTweet);
            LOG.info("Tweet postado.");
        } else {
            LOG.info("Não postou tweet porque está configurado assim.");
        }

        LOG.debug("Acabou em {}ms.", Duration.between(agora, Instant.now()).toMillis());
    }

    private static String montarNovoTweet() {
        List<Integer> opcoes = new ArrayList<>();
        for (int i=1;i<41;i++) {
            opcoes.add(i);
        }

        Collections.shuffle(opcoes);

        Integer count = 0;
        boolean flag = false;
        for (Integer str : opcoes) {
            if (flag == false) {
                count = str;
                flag = true;
            }
        }

        StringBuilder sb = informacoes(count);

        String novoTweet = sb.toString();

        LOG.debug("\n" + novoTweet);
        return novoTweet;
    }

    private static void mostrarNoNavegador(String novoTweet) throws IOException {
        if (MOSTRAR_NO_NAVEGADOR) {
            File file2 = new File("test.html");
            Files.write(file2.toPath(), novoTweet.replaceAll("\n", "<br>").getBytes());
            Desktop.getDesktop().browse(file2.toURI());
        }
    }

    private static Boolean passouIntervaloMinimo(Tweet dadosUltimoTweet) {
        LOG.debug("Checando intervalo mínimo.");
        LocalDateTime dataUltimoTweet = dadosUltimoTweet.getCreatedAt();
        LocalDateTime agora = LocalDateTime.now();
        Duration between = Duration.between(dataUltimoTweet, agora);
        boolean passouIntervaloMinimo = between.toMinutes() > INTERVALO_MINIMO.toMinutes();
        LOG.debug("Passou intervalo mínimo? {}", passouIntervaloMinimo);
        return passouIntervaloMinimo;
    }

    private static Tweet ultimoTweet() {
        List<Tweet> userTimeline = TWITTER_CLIENT.getUserTimeline(TWITTER_CLIENT.getUserIdFromAccessToken(), 5);
        Tweet dadosUltimoTweet = userTimeline.get(0);
        LOG.debug("Último tweet recuperado. Feito em {}.", dadosUltimoTweet.getCreatedAt());
        return dadosUltimoTweet;
    }

    private static StringBuilder informacoes (Integer count) {
        StringBuilder sb = new StringBuilder(280);

        if (count == 8 || count == 40) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }
            sb.append(min + "'/2T: Vermelho! o jogador " + jogador + " foi expulso após dar uma voadora no adversário.");
        }

        if (count == 2) {
            Dados dados = new Dados();
            List<String> listaInf = dados.influencers();

            String jogador = "";
            boolean flag = false;
            for (String str : listaInf) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " se revolta com os xingamentos e mostra o dedo do meio pra torcida Colorada. ");
        }

        if (count == 3 || count == 39) {
            Dados dados = new Dados();
            List<String> listaTimes = dados.listaTimes();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaTimes) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }
            sb.append(min + "'/2T: Gol d" + jogador + ". Com esse resultado o Internacional está sendo eliminado da competição. ");
        }

        if (count == 4 || count == 38) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();
            List<String> listaTimes = dados.listaTimes();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }

            String time = "";
            flag = false;
            for (String str : listaTimes) {
                if (flag == false) {
                    time = str;
                    flag = true;
                }
            }

            int index = time.length();
            time = time.substring(2,index);

            sb.append("GOOOOOOL DO INTER!!! aos " + min + "'/2T, o jogador " + jogador + " dá um belo chute de fora da área."
            + " INTERNACIONAL 1-0 " + time);
        }

        if (count == 1) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }
            sb.append(min + "'/2T: " + jogador + " recebe cartão amarelo do árbitro após apontar para o seu orgão sexual masculino e dizer:" +
                    " Apita aqui também!");
        }

        if (count == 6 || count == 37) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " afirma estar com saudades de jogar pelo Internacional!");
        }

        if (count == 7 || count == 36) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append("O Internacional anuncia a contratação do jogador " + jogador);
        }

        if (count == 9) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }
            sb.append(min + "/2T: " + "UUUUUHHH!! " + jogador + " cabeceia forte e a bola passa perto do gol." +
                    " Segue 0x0");
        }

        if (count == 10 || count == 35) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " sente que essa será a melhor temporada dele pelo Internacional");
        }

        if (count == 11) {
            Dados dados = new Dados();
            List<String> listaJog = dados.jogadoresClassicos();


            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " reafirma que quer encerrar a carreira pelo Internacional");
        }

        if (count == 12 || count == 34) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }
            sb.append(min + "'/2T: " + jogador + " arremata da entrada da área. Goleiro deles fez um milagre");
        }

        if (count == 13) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " conversa com a direção e diz que time precisa de um novo centroavante");
        }

        if (count == 14 || count == 33) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " comanda seu primeiro treino como técnico do Internacional");
        }

        if (count == 15 || count == 32) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " diz que o Internacional está deixando ele doente");
        }

        if (count == 16) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }

            sb.append(min + "'/2T: " + jogador + " se empolga na comemoração do gol, fica nu e vai pra torcida");
        }

        if (count == 17) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }

            sb.append(min + "'/2T: Torcedor se irrita com " + jogador + " e arremessa tênis em direção do jogador");
        }

        if (count == 18) {
            Dados dados = new Dados();
            List<String> listaJog = dados.compet();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append("Com esse resultado, o Internacional está eliminado da " + jogador);
        }

        if (count == 19 || count == 31) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();
            List<String> listaInf = dados.influencers();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            String influe = "";
            flag = false;
            for (String str : listaInf) {
                if (flag == false) {
                    influe = str;
                    flag = true;
                }
            }

            sb.append("Em coletiva, " + jogador + " se irrita com pergunta e joga microfone na cabeça de " + influe);
        }

        if (count == 20) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append("Após dar um soco em seu companheiro, " + jogador + " diz que violência não é solução");
        }

        if (count == 21 || count == 30) {
            Dados dados = new Dados();
            List<String> listaTimes = dados.listaTimes();

            String jogador = "";
            boolean flag = false;
            for (String str : listaTimes) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append("Final de jogo: Após muito tempo, o Internacional volta a vencer o time d" + jogador + ".");
        }

        if (count == 22) {

            sb.append("Saci enlouquece na comemoração do gol e faz dança sensual para a torcida");
        }

        if (count == 23 || count == 29) {
            Dados dados = new Dados();
            List<String> listaJog = dados.listaJogadores();
            List<Integer> listaMin = dados.minutagem();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            Integer min = 1;
            flag = false;
            for (Integer inte : listaMin) {
                if (flag == false) {
                    min = inte;
                    flag = true;
                }
            }

            sb.append(min + "'/2T: Penâlti para eles. " + jogador + " erra o bote na área e comete penâlti.");
        }

        if (count == 24) {

            sb.append("Torcedor protesta com faixa: 'Se não vai pelo amor, vai pela dor'");
        }

        if (count == 25) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            sb.append(jogador + " afirma que número de gols contra pelo Internacional não é proposital!");
        }

        if (count == 26) {

            sb.append("Após todas as bolas irem parar no Guaíba, treino coletivo pela parte da manhã é encerrado");
        }

        if (count == 27 || count == 28) {
            Dados dados = new Dados();
            List<String> listaJog = dados.tecnicos();
            List<String> listaInf = dados.listaTimes();

            String jogador = "";
            boolean flag = false;
            for (String str : listaJog) {
                if (flag == false) {
                    jogador = str;
                    flag = true;
                }
            }

            String influe = "";
            flag = false;
            for (String str : listaInf) {
                if (flag == false) {
                    influe = str;
                    flag = true;
                }
            }

            sb.append("Internacional empresta o " + jogador + " para " + influe);
        }
        return sb;
    }

}
