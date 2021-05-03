package de.mediathekview.mserver.crawler.br;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.daten.Sender;
import de.mediathekview.mlib.messages.listener.MessageListener;
import de.mediathekview.mserver.base.config.MServerConfigManager;
import de.mediathekview.mserver.base.messages.ServerMessages;
import de.mediathekview.mserver.crawler.basic.AbstractCrawler;
import de.mediathekview.mserver.crawler.br.tasks.BrBroadcastsTask;
import de.mediathekview.mserver.crawler.br.tasks.BrGetClipDetailsTask;
import de.mediathekview.mserver.progress.listeners.SenderProgressListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class BrCrawler extends AbstractCrawler {
  private static final Logger LOG = LogManager.getLogger(BrCrawler.class);
  public static final String BASE_URL = "https://www.br.de/mediathek/";

  public BrCrawler(
      final ForkJoinPool aForkJoinPool,
      final Collection<MessageListener> aMessageListeners,
      final Collection<SenderProgressListener> aProgressListeners,
      final MServerConfigManager rootConfig) {
    super(aForkJoinPool, aMessageListeners, aProgressListeners, rootConfig);
  }

  @Override
  public Sender getSender() {
    return Sender.BR;
  }

  @Override
  protected RecursiveTask<Set<Film>> createCrawlerTask() {

    final RecursiveTask<Set<BrClipQueryDto>> createCompleteClipListTask =
        createGetClipListCrawler();
    ConcurrentLinkedQueue<BrClipQueryDto> idList = null;

    try {
      final Set<BrClipQueryDto> completeClipList =
          forkJoinPool.submit(createCompleteClipListTask).get();

      idList = new ConcurrentLinkedQueue<>(completeClipList);
      incrementMaxCountBySizeAndGetNewSize(idList.size());
      printMessage(
          ServerMessages.DEBUG_MSSING_SENDUNGFOLGEN_COUNT, getSender().getName(), idList.size());
    } catch (final InterruptedException | ExecutionException exception) {
      LOG.fatal("Something went terrible wrong collecting the clip details", exception);
      printErrorMessage();
    }

    return new BrGetClipDetailsTask(this, idList);
  }

  private RecursiveTask<Set<BrClipQueryDto>> createGetClipListCrawler() {
    final LocalDate now = LocalDate.now();
    final Queue<BrQueryDto> input = new ConcurrentLinkedQueue<>();

    for (int i = 0; i <= crawlerConfig.getMaximumDaysForSendungVerpasstSection(); i++) {
      final LocalDate day = now.minusDays(i);
      input.add(
          new BrQueryDto(
              BrConstants.GRAPHQL_API,
              BrConstants.BROADCAST_SERVICE_BR,
              day,
              day,
              BrConstants.PAGE_SIZE,
              Optional.empty()));
      input.add(
          new BrQueryDto(
              BrConstants.GRAPHQL_API,
              BrConstants.BROADCAST_SERVICE_ALPHA,
              day,
              day,
              BrConstants.PAGE_SIZE,
              Optional.empty()));
    }
    for (int i = 1; i <= crawlerConfig.getMaximumDaysForSendungVerpasstSectionFuture(); i++) {
      final LocalDate day = now.plusDays(i);
      input.add(
          new BrQueryDto(
              BrConstants.GRAPHQL_API,
              BrConstants.BROADCAST_SERVICE_BR,
              day,
              day,
              BrConstants.PAGE_SIZE,
              Optional.empty()));
      input.add(
          new BrQueryDto(
              BrConstants.GRAPHQL_API,
              BrConstants.BROADCAST_SERVICE_ALPHA,
              day,
              day,
              BrConstants.PAGE_SIZE,
              Optional.empty()));
    }
    return new BrBroadcastsTask(this, input);
  }
}
