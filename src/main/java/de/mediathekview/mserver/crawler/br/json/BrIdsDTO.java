package de.mediathekview.mserver.crawler.br.json;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class BrIdsDTO {
  private final Set<String> ids;

  public BrIdsDTO() {
    super();
    ids = ConcurrentHashMap.newKeySet();
  }

  public boolean add(final String aId) {
    return ids.add(aId);
  }

  public boolean addAll(final Collection<? extends String> aIds) {
    return ids.addAll(aIds);
  }

  public Set<String> getIds() {
    return new ConcurrentSkipListSet<>(ids);
  }


}
