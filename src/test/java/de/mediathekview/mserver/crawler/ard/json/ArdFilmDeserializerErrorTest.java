package de.mediathekview.mserver.crawler.ard.json;

import com.google.gson.JsonElement;
import de.mediathekview.mlib.messages.listener.MessageListener;
import de.mediathekview.mserver.base.config.MServerConfigManager;
import de.mediathekview.mserver.crawler.ard.ArdCrawler;
import de.mediathekview.mserver.crawler.ard.ArdFilmDto;
import de.mediathekview.mserver.progress.listeners.SenderProgressListener;
import de.mediathekview.mserver.testhelper.JsonFileReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ArdFilmDeserializerErrorTest {

  protected MServerConfigManager rootConfig =
    MServerConfigManager.getInstance("MServer-JUnit-Config.yaml");

  @Test
  public void testFskDayEntry() {

    final JsonElement jsonElement = JsonFileReader.readJson("/ard/ard_film_page_fsk_day11.json");

    final ArdFilmDeserializer target = new ArdFilmDeserializer(createCrawler());
    final List<ArdFilmDto> actualFilms = target.deserialize(jsonElement, null, null);

    assertThat(actualFilms.size(), equalTo(0));

  }

  protected ArdCrawler createCrawler() {
    final ForkJoinPool forkJoinPool = new ForkJoinPool();
    final Collection<MessageListener> nachrichten = new ArrayList<>();
    final Collection<SenderProgressListener> fortschritte = new ArrayList<>();

    return new ArdCrawler(forkJoinPool, nachrichten, fortschritte, rootConfig);
  }
}