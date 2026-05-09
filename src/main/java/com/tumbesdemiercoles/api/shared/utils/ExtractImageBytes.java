package com.tumbesdemiercoles.api.shared.utils;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

/**
 * Utilidad para extraer el contenido binario de una imagen recibida
 * como archivo multipart en aplicaciones reactivas.
 *
 * <p>Está pensada para usarse con {@link FilePart} de Spring WebFlux,
 * devolviendo los bytes del archivo como un {@link Mono}.</p>
 */
public class ExtractImageBytes {

  /**
   * Extrae los bytes de un archivo de imagen representado por un {@link FilePart}.
   *
   * @param file archivo recibido como parte de una petición multipart
   * @return un {@link Mono} que emite un arreglo de bytes con el contenido del archivo
   */
  public static Mono<byte[]> extractImageBytes(FilePart file) {

    return DataBufferUtils.join(file.content())
            .map(buf -> {
              byte[] bytes = new byte[buf.readableByteCount()];
              buf.read(bytes);
              DataBufferUtils.release(buf);
              return bytes;
            });
  }
}
