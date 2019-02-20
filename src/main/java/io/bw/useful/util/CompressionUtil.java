package io.bw.useful.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Component;

/**
 * @author Byungwook lee on 2019-02-20
 * quddnr153@gmail.com
 * https://github.com/quddnr153
 */
@Component
public class CompressionUtil {
	/**
	 * decompress the compressed source file to target directory.
	 *
	 * @param source          the compressed source path
	 * @param targetDirectory the target directory path
	 * @return target directory path
	 */
	public Path decompress(final Path source, final Path targetDirectory) {
		try (final ZipFile zipFile = new ZipFile(source.toFile())) {
			final Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				final ZipEntry entry = entries.nextElement();

				final Path decompressedFile = Path.of(targetDirectory.toString(), entry.getName());

				if (entry.isDirectory()) {
					Files.createDirectories(decompressedFile);
				} else {
					Files.createDirectories(decompressedFile.getParent());

					try (
						final InputStream is = zipFile.getInputStream(entry);
						final OutputStream os = Files.newOutputStream(decompressedFile)) {

						IOUtils.copy(is, os);
					}
				}
			}

		} catch (final ZipException zipException) {
			throw new IllegalStateException(
				"The source (" + source.toString() + ") is not suitable zip format.", zipException);
		} catch (IOException ioException) {
			throw new IllegalArgumentException(
				"Check your source and target status, source = "
					+ source.toString() + ", target = " + targetDirectory.toString(), ioException);
		}

		return targetDirectory;
	}
}
