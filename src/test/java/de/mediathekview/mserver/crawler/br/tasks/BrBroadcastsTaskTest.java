package de.mediathekview.mserver.crawler.br.tasks;

import de.mediathekview.mserver.crawler.br.BrClipQueryDto;
import de.mediathekview.mserver.crawler.br.BrConstants;
import de.mediathekview.mserver.crawler.br.BrQueryDto;
import de.mediathekview.mserver.crawler.br.data.BrClipType;
import de.mediathekview.mserver.crawler.br.data.BrID;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.Assertions.assertThat;

class BrBroadcastsTaskTest extends BrTaskTestBase {

  protected Queue<BrQueryDto> createQueryDto(final String requestUrl) {
    final Queue<BrQueryDto> input = new ConcurrentLinkedQueue<>();
    final LocalDate day = LocalDate.of(2021, 3, 15);
    input.add(
        new BrQueryDto(
            wireMockServer.baseUrl() + requestUrl,
            BrConstants.BROADCAST_SERVICE_BR,
            day,
            day,
            10,
            Optional.empty()));
    return input;
  }

  public Set<BrClipQueryDto> executeTask(String request) {
    return new BrBroadcastsTask(createCrawler(), createQueryDto(request)).invoke();
  }

  @Test
  void noBroadcasts() {
    final String request = "/br/empty";
    setupSuccessfulJsonPostResponse(request, "/br/br_broadcast_empty.json");

    final Set<BrClipQueryDto> actual = executeTask(request);

    assertThat(actual).isNotNull().isEmpty();
  }

  @Test
  void singlePage() {
    final String request = "/br/single";
    setupSuccessfulJsonPostResponse(request, "/br/br_broadcast_single_page.json");

    List<BrClipQueryDto> expected =
        List.of(
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:60813b5f0b25e4000731b248")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:604b4c5f3ba50f001a1e64e0")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:604b4c2973b5b70013facd0f")));

    final Set<BrClipQueryDto> actual = executeTask(request);

    assertThat(actual).isNotNull().hasSize(3).containsAll(expected);
  }

  @Test
  void multiplePages() {
    final String request = "/br/single";
    setupSuccessfulJsonPostResponse(
        request, "/br/br_broadcast_multiple_pages_1.json", "$programmeFilter)", null);
    setupSuccessfulJsonPostResponse(
        request, "/br/br_broadcast_multiple_pages_2.json", "$programmeFilter, after", null);

    List<BrClipQueryDto> expected =
        List.of(
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:6053887946ee90001a872665")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:6021371101483100133f889d")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:6019326ea636b2001a16d491")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:601932287b541b001316ce4b")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:5e3aa3378583a30013890d58")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:5d3875f52035ed001a6f3bb5")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:5d3831932bd8f200136c9242")),
            new BrClipQueryDto(
                BrConstants.GRAPHQL_API,
                new BrID(BrClipType.PROGRAMME, "av:5d24b2f44b36a5001a8e093e")));

    final Set<BrClipQueryDto> actual = executeTask(request);

    assertThat(actual).isNotNull().hasSize(8).containsAll(expected);
  }
}
