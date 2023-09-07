package org.folio.mylibrary.util;

import java.util.Objects;
import java.util.UUID;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class StringUtil {

  public static String uuidToStringSafe(UUID uuid) {
    return Objects.nonNull(uuid) ? uuid.toString() : null;
  }

  public static UUID toUuidSafe(String uuid) {
    return Objects.nonNull(uuid) ? UUID.fromString(uuid) : null;
  }

  public static UUID toUuidSafe(Object uuid) {
    return Objects.nonNull(uuid) ? UUID.fromString(uuid.toString()) : null;
  }
}
